/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.dao;

import br.uff.bus_data.helper.Constants;
import br.uff.bus_data.models.Coleta;
import br.uff.bus_data.models.DadoRJ;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author schettino
 */
public class DadoRJDAO extends AbstractDAO<DadoRJ> {

    @Override
    public DadoRJ getFromResultSet(ResultSet rs) throws SQLException {

        SimpleDateFormat dt = new SimpleDateFormat(Constants.DB_DATE_FORMAT);
        DadoRJ dado = new DadoRJ();
        Date dataHora = null;
        try {
            dataHora = dt.parse(rs.getString("data_hora"));
        } catch (ParseException ex) {
        }
        int i = rs.getInt("id");
        int coletaId = rs.getInt("coleta_id");
        int linhaId = rs.getInt("linha_id");
        int ordemId = rs.getInt("ordem_id");
        float latitude = rs.getFloat("latitude");
        float longitude = rs.getFloat("longitude");
        float velocidade = rs.getFloat("velocidade");

        dado.setDataHora(dataHora);
        dado.setId(i);
        dado.setColetaId(coletaId);
        dado.setLinhaId(linhaId);
        dado.setOrdemId(ordemId);
        dado.setLatitude(latitude);
        dado.setLongitude(longitude);
        dado.setVelocidade(velocidade);

        return dado;
    }

    @Override
    public String getTableName() {
        return "dados_rj";
    }

    @Override
    public List<String> getAttributes() {
        ArrayList<String> attrs = new ArrayList<String>();
        attrs.add("data_hora");
        attrs.add("coleta_id");
        attrs.add("linha_id");
        attrs.add("ordem_id");
        attrs.add("latitude");
        attrs.add("longitude");
        attrs.add("velocidade");
        return attrs;
    }
}
