package analyser.database;

import analyser.util.GeoSample;
import analyser.util.Grid;
import org.boon.json.implementation.JsonFastParser;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataBase {
    private static File RESOURCES = new File("resources");

    public DataBase() {
        DataBase.RESOURCES.mkdir();
    }

    public Grid collect(String route, double distance) {
        JsonFastParser parser = new JsonFastParser();
        Grid g = new Grid(distance);
        for (File folder : DataBase.RESOURCES.listFiles(File::isDirectory)) {
            File routeFile = folder.listFiles((x) -> x.getName().equals(route + ".json")).length == 1 ? folder.listFiles((x) -> x.getName().equals(route + ".json"))[0] : null;
            if (routeFile != null) {
                try (RandomAccessFile in = new RandomAccessFile(routeFile, "r"); FileChannel inChannel = in.getChannel()) {
                    MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
                    buffer.load();
                    List<Map<String, ?>> json = (List<Map<String, ?>>) parser.parse(StandardCharsets.UTF_8.decode(buffer));
                    for (Map<String, ?> bus : json) {
                        List<GeoSample> samples = new ArrayList<>();
                        for (Map<String, ?> s : (List<Map<String, ?>>) bus.get("samples")) {
                            samples.add(new GeoSample((Double) s.get("lat"), (Double) s.get("lng"), (Double) s.get("vel")));
                        }
                        g.addAll(samples);
                    }
                    buffer.clear();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return g;
    }
}
