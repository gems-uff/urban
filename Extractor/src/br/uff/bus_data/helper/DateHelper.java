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
import java.io.IOException;
// Adaptado de: http://www.mkyong.com/java/how-to-delete-directory-in-java/

public class DeleteDirectory {

    public static void delete(String path)
            throws IOException {
        File file = new File(path);

        if (file.isDirectory()) {

            //directory is empty, then delete it
            if (file.list().length == 0) {

                file.delete();
                System.out.println("Directory is deleted : "
                        + file.getAbsolutePath());

            } else {

                //list all the directory contents
                String files[] = file.list();

                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);

                    //recursive delete
                    delete(fileDelete.getAbsolutePath());
                }

                //check the directory again, if empty then delete it
                if (file.list().length == 0) {
                    file.delete();
                    System.out.println("Directory is deleted : "
                            + file.getAbsolutePath());
                }
            }

        } else {
            //if file, then delete it
            file.delete();
            System.out.println("File is deleted : " + file.getAbsolutePath());
        }
    }
}


