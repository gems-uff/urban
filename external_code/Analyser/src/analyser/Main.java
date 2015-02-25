package analyser;

import analyser.database.DataBase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by mateus on 21/02/15.
 */
public class Main {
    public static void main(String[] args) {
        Calendar a = Calendar.getInstance();
        DataBase db = new DataBase();
        try (BufferedWriter out = new BufferedWriter(new FileWriter(new File("points.js")))) {
            out.write("var points = " + db.collect("371", 50).route() + ";");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Calendar b = Calendar.getInstance();
        long sec = (b.getTimeInMillis() - a.getTimeInMillis()) / (1000);
        long min = sec / 60;
        sec -= min * 60;
        System.out.printf("%02d:%02d", min, sec);
    }
}
