package cleaner.io;


import cleaner.ui.View;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Cleaner {
    protected static final File OUTPUT = new File("resources");
    protected static final File INPUT = new File("input");

    protected static final FileFilter JSON_FILTER = (file -> file.isFile() && file.length() > 0 && file.getName().matches("\\d{2}-\\d{2}\\.json$"));
    protected static final FileFilter SIMPLE_JSON_FILTER = (file -> file.isFile() && file.length() > 0 && file.getName().endsWith(".json"));
    protected static final FileFilter FOLDER_FILTER = (file -> file.isDirectory() && file.getName().matches("\\d{4}-\\d{2}-\\d{2}$"));


    public static final String JSON_DATA_FORMAT = "MM-dd-yyyy HH:mm:ss";
    public static final String DIRECTORY_DATA_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String CORRECT_DATA_FORMAT =  "yyyy/MM/dd HH:mm:ss";
    public static final String COMPACT_DATA_FORMAT =  "HH:mm:ss";

    protected static final Double TIME_WINDOW = 15.0;

    private View view;


    public Cleaner() {
        Cleaner.OUTPUT.mkdir();
        Cleaner.INPUT.mkdir();
        this.view = new View();
    }

    public void clean() {
        Collection<Thread> filters = new ArrayList<>();
        Arrays.asList(Cleaner.INPUT.listFiles(Cleaner.FOLDER_FILTER)).forEach(folder -> {
            Filter filter = new Filter(folder);
            this.view.addObservable(filter);
            filters.add(new Thread(filter));
        });

        filters.forEach(Thread::start);

        filters.forEach(filter -> {
            try {
                filter.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }
}
