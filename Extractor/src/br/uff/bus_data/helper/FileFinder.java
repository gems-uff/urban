/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.helper;

/**
 *
 * @author gerente
 */
import java.io.File;
import java.io.FilenameFilter;

public class FileFinder {


    public static File[] finder(String dirName, final String extension) {
        File dir = new File(dirName);

        return dir.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String filename) {
                return filename.endsWith(extension);
            }
        });

    }
}


