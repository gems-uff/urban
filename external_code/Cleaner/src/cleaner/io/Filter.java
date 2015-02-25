package cleaner.io;

import cleaner.util.*;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Filter implements Runnable, cleaner.util.Observable {
    private static final Pattern SCAN_PATTERN = Pattern.compile("\\[\"(.{19})\",\"([^\"]+)\",([^,]+),(-\\d{2}\\.\\d+),(-\\d{2}\\.\\d+),(\\d+\\.\\d+)\\].");
    private static final Pattern FILTER_PATTERN = Pattern.compile("\\{\"busID\": \"([^\"]+)\", \"date\": \"([^\"]{19})\", \"lat\": (-\\d+\\.\\d+), \"lng\": (-\\d+\\.\\d+), \"vel\": (\\d+\\.\\d+)\\}");

    private File target;
    private double status;
    private Set<cleaner.util.Observer> observers;
    private File destination;

    public Filter(File target) {
        this.target = target;
        this.status = 0;
        this.observers = new HashSet<>();
        this.destination = new File(Cleaner.OUTPUT.getAbsolutePath() + "/" + target.getName());
        this.destination.mkdir();
    }


    public double status() {
        return status;
    }

    private void filterData() {
        SimpleDateFormat directoryDF = new SimpleDateFormat(Cleaner.DIRECTORY_DATA_FORMAT);
        SimpleDateFormat jsonDF = new SimpleDateFormat(Cleaner.JSON_DATA_FORMAT);
        File[] targetJson = this.target.listFiles(Cleaner.JSON_FILTER);

        for (int i = 0; i < targetJson.length; i++) {
            File json = targetJson[i];
            this.status = i * 0.6 / targetJson.length;
            this.updateAll();

            Map<String, Set<OldEntry>> data = new HashMap<>(500);
            try (RandomAccessFile in = new RandomAccessFile(json, "r"); FileChannel inChannel = in.getChannel()) {
                MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
                buffer.load();

                Calendar targetDate = Calendar.getInstance();
                targetDate.setTime(directoryDF.parse(destination.getName() + " " + json.getName().replace("-", ":").substring(0, json.getName().length() - 5) + ":00"));
                Matcher matcher = Filter.SCAN_PATTERN.matcher(StandardCharsets.UTF_8.decode(buffer));
                while (matcher.find()) {
                    String date = matcher.group(1);
                    String routeID = matcher.group(3).replaceAll("\"", "").replaceAll("\\.0", "");
                    String busID = matcher.group(2).replaceAll("\"", "").replaceAll("\\.0", "");
                    String lat = matcher.group(4);
                    String lng = matcher.group(5);
                    String vel = matcher.group(6);
                    if (!routeID.isEmpty() && !busID.isEmpty()) {
                        Calendar actualDate = Calendar.getInstance();
                        actualDate.setTime(jsonDF.parse(date));
                        int diff = (int) ((targetDate.getTimeInMillis() - actualDate.getTimeInMillis()) / 60000);
                        if (actualDate.get(Calendar.DAY_OF_MONTH) == targetDate.get(Calendar.DAY_OF_MONTH) && diff >= 0 && diff <= Cleaner.TIME_WINDOW) {
                            data.putIfAbsent(routeID, new HashSet<>(500));
                            data.get(routeID).add(new OldEntry(actualDate.getTime(), busID, Double.parseDouble(lat), Double.parseDouble(lng), Double.parseDouble(vel)));
                        }
                    }
                }
                buffer.clear();
            } catch (ParseException | IOException e) {
                e.printStackTrace();
            }

            data.forEach((routeId, bus) -> {
                try (RandomAccessFile out = new RandomAccessFile(new File(destination.getAbsolutePath() + "/" + routeId + ".json"), "rw");
                     FileChannel outChannel = out.getChannel()) {
                    StringBuilder tempJson = new StringBuilder(bus.toString());
                    tempJson.delete(0, 1).delete(tempJson.length() - 1, tempJson.length()).append(", ");
                    out.seek(out.length());
                    ByteBuffer buffer = StandardCharsets.UTF_8.encode(tempJson.toString());
                    outChannel.write(buffer);
                    buffer.clear();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void rebuildData() {
        SimpleDateFormat correctDF = new SimpleDateFormat(Cleaner.CORRECT_DATA_FORMAT);
        File[] targetJson = destination.listFiles(Cleaner.SIMPLE_JSON_FILTER);
        for (int i = 0; i < targetJson.length; i++) {
            this.status = 0.6 + i * 0.4 / targetJson.length;
            this.updateAll();

            File json = targetJson[i];
            Map<String, Set<NewEntry>> data = new HashMap<>();
            StringBuilder input = new StringBuilder("[");

            try (RandomAccessFile in = new RandomAccessFile(json, "r");
            FileChannel inChannel = in.getChannel()) {
                MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
                buffer.load();
                input.append(StandardCharsets.UTF_8.decode(buffer)).delete(input.length() - 2, input.length()).append("]");
                buffer.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
            json.delete();
            Matcher matcher = Filter.FILTER_PATTERN.matcher(input);
            try {
                while (matcher.find()) {
                    String busID = matcher.group(1);
                    Date date = correctDF.parse(matcher.group(2));
                    double lat = Double.parseDouble(matcher.group(3));
                    double lng = Double.parseDouble(matcher.group(4));
                    double vel = Double.parseDouble(matcher.group(5));

                    data.putIfAbsent(busID, new HashSet<>(100));
                    data.get(busID).add(new NewEntry(date, lat, lng, vel));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            StringBuilder output = new StringBuilder("[");
            data.forEach((busID, entries) -> {
                List<NewEntry> sorted = new ArrayList<>(entries);
                sorted.sort((a, b) -> a.date().compareTo(b.date()));
                output.append("{\"busID\": \"").append(busID).append("\", \"samples\": ").append(sorted).append("}, ");
            });
            output.delete(output.length() - 2, output.length()).append("]");
            try (RandomAccessFile out = new RandomAccessFile(json, "rw");
            FileChannel outChannel = out.getChannel()) {
                ByteBuffer buffer = StandardCharsets.UTF_8.encode(output.toString());
                outChannel.write(buffer);
                buffer.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        this.filterData();
        this.status = 0.6;
        this.updateAll();
        this.rebuildData();
        this.status = 1.0;
        this.updateAll();
    }

    @Override
    public void registerObserver(cleaner.util.Observer o) {
        this.observers.add(o);
    }

    public void updateAll() {
        this.observers.forEach(cleaner.util.Observer::update);
    }

    @Override
    public void removeObserser(cleaner.util.Observer o) {
        this.observers.remove(o);
    }

    @Override
    public String toString() {
        return this.target.getName();
    }
}