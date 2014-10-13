/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.dao;

import br.uff.bus_data.helper.Constants;
import br.uff.bus_data.models.Descarte;
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
public class DescarteDAO extends AbstractDAO<Descarte> {

    @Override
    public Descarte getFromResultSet(ResultSet rs) throws SQLException {

        SimpleDateFormat dt = new SimpleDateFormat(Constants.DB_DATE_FORMAT);
        Descarte descarte = new Descarte();
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
        String motivo = rs.getString("motivo");

        descarte.setDataHora(dataHora);
        descarte.setId(i);
        descarte.setColetaId(coletaId);
        descarte.setLinhaId(linhaId);
        descarte.setOrdemId(ordemId);
        descarte.setLatitude(latitude);
        descarte.setLongitude(longitude);
        descarte.setVelocidade(velocidade);
        descarte.setMotivo(motivo);

        return descarte;
    }

    @Override
    public String getTableName() {
        return "descartes";
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
        attrs.add("motivo");
        return attrs;
    }

}


