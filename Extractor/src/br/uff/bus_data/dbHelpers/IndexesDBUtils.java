/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.dbHelpers;

import java.sql.Statement;
import java.sql.SQLException;

/**
 *
 * @author schettino
 */
public class IndexesDBUtils {

    public static void dropIndexes(Statement stmt) throws SQLException {
        System.out.println("Removing indexes");
        stmt.execute("DROP INDEX index_ordens_on_ordem ON ordens");
        stmt.execute("DROP INDEX index_linhas_on_linha ON linhas");
        stmt.execute("DROP INDEX index_coletas_on_data_hora_inicio_and_filename ON coletas");
        stmt.execute("DROP INDEX index_dados_rj_on_data_hora_and_ordem_id ON dados_rj");
        System.out.println("Removing foreign keys");
        stmt.execute("ALTER TABLE dados_rj DROP FOREIGN KEY fk_dados_rj_coleta_id");
        stmt.execute("ALTER TABLE dados_rj DROP FOREIGN KEY fk_dados_rj_linha_id");
        stmt.execute("ALTER TABLE dados_rj DROP FOREIGN KEY fk_dados_rj_ordem_id");
        stmt.execute("DROP INDEX fk__dados_rj_linha_id ON dados_rj");
        stmt.execute("DROP INDEX fk__dados_rj_coleta_id ON dados_rj");
        stmt.execute("DROP INDEX fk__dados_rj_ordem_id ON dados_rj");
        System.out.println("Starting");
    }

    public static void createIndexes(Statement stmt) throws SQLException {
        System.out.println("Re-creating indexes");
        stmt.execute("CREATE UNIQUE INDEX index_ordens_on_ordem ON ordens (ordem)");
        System.out.println("Index ordens OK");
        stmt.execute("CREATE UNIQUE INDEX index_linhas_on_linha ON linhas (linha)");
        System.out.println("Index linhas OK");
        stmt.execute("CREATE UNIQUE INDEX index_coletas_on_data_hora_inicio_and_filename ON coletas (data_hora_inicio, filename)");
        System.out.println("Index coletas OK");
        stmt.execute("CREATE UNIQUE INDEX index_dados_rj_on_data_hora_and_ordem_id ON dados_rj (data_hora, ordem_id)");
        System.out.println("Index dados OK");
        System.out.println("Re-Creating FKs");
        stmt.execute("ALTER TABLE dados_rj ADD FOREIGN KEY (coleta_id) REFERENCES coletas(id)");
        System.out.println("FK dados_rj coleta_id OK");
        stmt.execute("ALTER TABLE dados_rj ADD FOREIGN KEY (linha_id) REFERENCES linhas(id)");
        System.out.println("FK dados_rj linha_id OK");
        stmt.execute("ALTER TABLE dados_rj ADD FOREIGN KEY (ordem_id) REFERENCES ordens(id)");
        System.out.println("FK dados_rj ordem_id OK");
        System.out.println("Re-Creating FKs Indexes");
        stmt.execute("CREATE INDEX fk__dados_rj_ordem_id ON dados_rj (ordem_id)");
        System.out.println("FK index dados_rj ordem_id OK");
        stmt.execute("CREATE INDEX fk__dados_rj_linha_id ON dados_rj (linha_id)");
        System.out.println("FK index dados_rj linha_id OK");
        stmt.execute("CREATE INDEX fk__dados_rj_coleta_id ON dados_rj (coleta_id)");
        System.out.println("FK index dados_rj coleta_id OK");
    }
}
