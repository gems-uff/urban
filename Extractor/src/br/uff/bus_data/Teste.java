/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data;

import br.uff.bus_data.dao.DadoRJDAO;
import br.uff.bus_data.helper.DBConnection;
import br.uff.bus_data.helper.LatLongConvertion;
import br.uff.bus_data.models.DadoRJ;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gerente
 */
public class Teste {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            System.out.println("Iniciando");
            Class.forName("com.mysql.jdbc.Driver");
            String connectionUrl = "jdbc:" + DBConnection.JDBC + ":" + DBConnection.DATABASE + "?"
                    + "user=" + DBConnection.USER + "&password=" + DBConnection.PASSWORD;
            Connection con = DriverManager.getConnection(connectionUrl);
            Statement stmt = con.createStatement();

            DadoRJDAO dadoRJDao = new DadoRJDAO();
            dadoRJDao.setStatement(stmt);
//            int[] ordens = {21, 32, 43, 57, 87, 91, 102, 154, 158, 196}; //menores velocidades medias

//            int[] ordens = {7610, 6832, 5123, 7255, 4678, 556, 7211, 3072, 5321, 2767 }; //maiores velocidades medias

            int[] ordens = {7735, 7667, 7640, 7565, 6792, 704, 3305, 2835, 735, 2161, 7618, 7791, 7638, 6927, 7526, 7355}; //maiores velocidades
            for (int o : ordens) {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("ordem_id", String.valueOf(o));
                List<DadoRJ> dados = dadoRJDao.select(params);
                double min = Double.MAX_VALUE;
                double max = -1;
                double sum = 0;
                int count = 0;
                for (int i = 0; i < dados.size() - 1; i++) {
                    DadoRJ dadoRJ = dados.get(i);
                    DadoRJ dadoRJ2 = dados.get(i + 1);
                    double distance = LatLongConvertion.distance(dadoRJ.getLatitude(),
                            dadoRJ.getLongitude(), dadoRJ2.getLatitude(),
                            dadoRJ2.getLongitude(), 'K');
                    if (!Double.isNaN(distance)) {
                        count++;
                        sum += distance;
                        if (distance < min) {
                            min = distance;
                        } else if (distance > max) {
                            max = distance;
                        }
                    }
                }
                double avg = sum / count;
                System.out.println("Ordem " + o + ":");
                System.out.println("Min: " + min);
                System.out.println("Max: " + max);
                System.out.println("Count: " + count);
                System.out.println("AVG: " + avg);
                System.out.println("");

            }
            stmt.close();
            con.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Teste.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Teste.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//    public static void main(String[] args) throws ParseException {
//
//        SimpleDateFormat dt = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
//        SimpleDateFormat dt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = dt.parse("06-04-2014 12:02:03");
//        String data = dt2.format(date);
//        System.out.println("data " + data);
//
//    }
}


