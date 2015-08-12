/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.dbHelpers;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

/**
 *
 * @author schettino
 */
public class IndexesDBUtils {

    private static final int SIMPLE = 0;
    private static final int UNIQUE = 1;
    private static final int SPARTIAL = 2;

    private static Statement stamt;

    public static void dropIndexes(Statement stmt, Connection con) throws SQLException {
        stamt = stmt;
        System.out.println("Removing indexes");
        dropIndex("index_loaded_files_on_start_time_and_filename");

        dropIndex("index_bus_positions_on_time_and_bus_id");
        dropIndex("index_bus_positions_on_time");
        dropIndex("index_bus_positions_on_bus_id");
        dropIndex("index_bus_positions_on_line_id");
        dropIndex("index_bus_positions_on_position");

        dropIndex("index_disposals_on_time");
        dropIndex("index_disposals_on_bus_id");
        dropIndex("index_disposals_on_line_id");
        dropIndex("index_disposals_on_position");
        dropIndex("index_disposals_on_disposal_reason");

        dropIndex("index_lines_on_line_number");

        dropIndex("index_buses_on_bus_number");

        System.out.println("Removing foreign keys");

        dropFK("lines", "lines_pkey");
        dropFK("buses", "buses_pkey");
        dropFK("loaded_files", "loaded_files_pkey");
        dropFK("bus_positions", "bus_positions_pkey");
        dropFK("disposals", "disposals_pkey");

        con.commit();

//        stmt.execute("DROP INDEX lines_pkey");
//        stmt.execute("DROP INDEX buses_pkey");        
//        stmt.execute("DROP INDEX loaded_files_pkey");
//        stmt.execute("DROP INDEX bus_positions_pkey");
//        stmt.execute("DROP INDEX disposals_pkey");
//        stmt.execute("DROP INDEX index_lines_on_linha ON lines");
//        stmt.execute("DROP INDEX index_loaded_files_on_start_time_and_filename ON loaded_files");
//        stmt.execute("DROP INDEX index_bus_positions_on_time_and_bus_number_id ON bus_positions");
//        stmt.execute("DROP INDEX fk__bus_positions_line_id ON bus_positions");
//        stmt.execute("DROP INDEX fk__bus_positions_loaded_file_id ON bus_positions");
//        stmt.execute("DROP INDEX fk__bus_positions_bus_number_id ON bus_positions");
//        stmt.execute("ALTER TABLE disposals DROP FOREIGN KEY fk_disposals_loaded_file_id");
//        stmt.execute("ALTER TABLE disposals DROP FOREIGN KEY fk_disposals_line_id");
//        stmt.execute("ALTER TABLE disposals DROP FOREIGN KEY fk_disposals_bus_number_id");
//        stmt.execute("ALTER TABLE disposals DROP FOREIGN KEY fk_disposals_last_postion_id");
//        stmt.execute("DROP INDEX fk__disposals_line_id ON disposals");
//        stmt.execute("DROP INDEX fk__disposals_loaded_file_id ON disposals");
//        stmt.execute("DROP INDEX fk__disposals_bus_number_id ON disposals");
//        stmt.execute("DROP INDEX fk__disposals_last_postion_id ON disposals");
        System.out.println("Starting");
    }
    public static void enos(Statement stmt, Connection con) throws SQLException {
        stamt = stmt;
        System.out.println("Re-creating indexes");
         createSimpleIndex("index_disposals_on_disposal_reason", "disposals", new String[]{"disposal_reason"});
        con.commit();
    }

    public static void createIndexes(Statement stmt, Connection con) throws SQLException {
        stamt = stmt;
        System.out.println("Re-creating indexes");

        createUniqueIndex("index_buses_on_bus_number", "buses", new String[]{"bus_number"});

        createUniqueIndex("index_lines_on_line_number", "lines", new String[]{"line_number"});

        createUniqueIndex("index_loaded_files_on_start_time_and_filename", "loaded_files", new String[]{"start_time", "filename"});

        createSimpleIndex("index_bus_positions_on_time_and_bus_id", "bus_positions", new String[]{"time", "bus_id"});
        createSimpleIndex("index_bus_positions_on_time", "bus_positions", new String[]{"time"});
        createSimpleIndex("index_bus_positions_on_bus_id", "bus_positions", new String[]{"bus_id"});
        createSimpleIndex("index_bus_positions_on_line_id", "bus_positions", new String[]{"line_id"});
        con.commit();
        createSpartialIndex("index_bus_positions_on_position", "bus_positions", new String[]{"position"});
        con.commit();

        createSimpleIndex("index_disposals_on_time", "disposals", new String[]{"time"});
        createSimpleIndex("index_disposals_on_bus_id", "disposals", new String[]{"bus_id"});
        createSimpleIndex("index_disposals_on_line_id", "disposals", new String[]{"line_id"});
        createSimpleIndex("index_disposals_on_disposal_reason", "disposals", new String[]{"disposal_reason"});
        con.commit();
        createSpartialIndex("index_disposals_on_position", "disposals", new String[]{"position"});
        con.commit();

        System.out.println("Re-creating FKs");
        createUniqueIndex("lines_pkey", "lines", new String[]{"id"});
        createFK("lines", "lines_pkey", "lines_pkey");

        createUniqueIndex("buses_pkey", "buses", new String[]{"id"});
        createFK("buses", "buses_pkey", "buses_pkey");

        createUniqueIndex("loaded_files_pkey", "loaded_files", new String[]{"id"});
        createFK("loaded_files", "loaded_files_pkey", "loaded_files_pkey");

        createUniqueIndex("bus_positions_pkey", "bus_positions", new String[]{"id"});
        createFK("bus_positions", "bus_positions_pkey", "bus_positions_pkey");

        createUniqueIndex("disposals_pkey", "disposals", new String[]{"id"});
        createFK("disposals", "disposals_pkey", "disposals_pkey");

        con.commit();
//        System.out.println("Re-Creating FKs");
//        stmt.execute("ALTER TABLE bus_positions ADD FOREIGN KEY (loaded_file_id) REFERENCES loaded_files(id)");
//        System.out.println("FK bus_positions loaded_file_id OK");
//        stmt.execute("ALTER TABLE bus_positions ADD FOREIGN KEY (line_id) REFERENCES lines(id)");
//        System.out.println("FK bus_positions line_id OK");
//        stmt.execute("ALTER TABLE bus_positions ADD FOREIGN KEY (bus_number_id) REFERENCES buses(id)");
//        System.out.println("FK bus_positions bus_number_id OK");
//        stmt.execute("ALTER TABLE disposals ADD FOREIGN KEY (loaded_file_id) REFERENCES loaded_files(id)");
//        System.out.println("FK disposals loaded_file_id OK");
//        stmt.execute("ALTER TABLE disposals ADD FOREIGN KEY (line_id) REFERENCES lines(id)");
//        System.out.println("FK disposals line_id OK");
//        stmt.execute("ALTER TABLE disposals ADD FOREIGN KEY (bus_number_id) REFERENCES buses(id)");
//        System.out.println("FK disposals bus_number_id OK");
//        stmt.execute("ALTER TABLE disposals ADD FOREIGN KEY (last_postion_id) REFERENCES bus_positions(id)");
//        System.out.println("FK disposals last_postion_id OK");
//        System.out.println("Re-Creating FKs Indexes");
//        stmt.execute("CREATE INDEX fk__bus_positions_bus_number_id ON bus_positions (bus_number_id)");
//        System.out.println("FK index bus_positions bus_number_id OK");
//        stmt.execute("CREATE INDEX fk__bus_positions_line_id ON bus_positions (line_id)");
//        System.out.println("FK index bus_positions line_id OK");
//        stmt.execute("CREATE INDEX fk__bus_positions_loaded_file_id ON bus_positions (loaded_file_id)");
//        System.out.println("FK index bus_positions loaded_file_id OK");
//        stmt.execute("CREATE INDEX fk__disposals_bus_number_id ON disposals (bus_number_id)");
//        System.out.println("FK index disposals bus_number_id OK");
//        stmt.execute("CREATE INDEX fk__disposals_line_id ON disposals (line_id)");
//        System.out.println("FK index disposals line_id OK");
//        stmt.execute("CREATE INDEX fk__disposals_loaded_file_id ON disposals (loaded_file_id)");
//        System.out.println("FK index disposals loaded_file_id OK");
//        stmt.execute("CREATE INDEX fk__disposals_last_postion_id ON disposals (last_postion_id)");
//        System.out.println("FK index disposals last_postion_id OK");
    }

    private static void dropIndex(String indexName) throws SQLException {
        stamt.execute("DROP INDEX " + indexName);
        System.out.println(indexName + " OK");
    }

    private static void createIndex(String indexName, String tableName, String[] columns, int type) throws SQLException {
        String query = "CREATE ";
        
        if (type == UNIQUE) {
            query += "UNIQUE ";
        }
        query += "INDEX " + indexName + " ON " + tableName;
        if (type == SPARTIAL){
            query += " USING GIST ";
        }
        query += "(";
        for (String column : columns) {
            query += column + ",";
        }
        query = query.substring(0, query.length() - 1);
        query += ")";
        System.out.println("Query index " + query);
        stamt.execute(query);
        System.out.println(indexName + " OK");
    }

    private static void createUniqueIndex(String indexName, String tableName, String[] columns) throws SQLException {
        createIndex(indexName, tableName, columns, UNIQUE);
    }

    private static void createSimpleIndex(String indexName, String tableName, String[] columns) throws SQLException {
        createIndex(indexName, tableName, columns, SIMPLE);
    }

    private static void createSpartialIndex(String indexName, String tableName, String[] columns) throws SQLException {
        createIndex(indexName, tableName, columns, SPARTIAL);
    }

    private static void dropFK(String tableName, String cName) throws SQLException {
        stamt.execute("ALTER TABLE " + tableName + " DROP CONSTRAINT " + cName);
        System.out.println(cName + " OK");
    }

    private static void createFK(String tableName, String cName, String indexName) throws SQLException {
        stamt.execute("ALTER TABLE " + tableName + " ADD CONSTRAINT " + cName + " PRIMARY KEY USING INDEX " + indexName);
        System.out.println(cName + " OK");
    }
}
