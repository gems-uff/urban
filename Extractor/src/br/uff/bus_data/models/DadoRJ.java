/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.models;

import br.uff.bus_data.helper.Constants;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author schettino
 */
public class DadoRJ implements Comparable<DadoRJ>, Mappable<String, String> {

    long id;
    long coletaId;
    long linhaId;
    long ordemId;
    Date dataHora;
    float latitude;
    float longitude;
    float velocidade;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getColetaId() {
        return coletaId;
    }

    public void setColetaId(long coletaId) {
        this.coletaId = coletaId;
    }

    public long getLinhaId() {
        return linhaId;
    }

    public void setLinhaId(long linhaId) {
        this.linhaId = linhaId;
    }

    public long getOrdemId() {
        return ordemId;
    }

    public void setOrdemId(long ordemId) {
        this.ordemId = ordemId;
    }

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(float velocidade) {
        this.velocidade = velocidade;
    }

    public Position getPosition() {
        Position p = new Position(this.latitude, this.longitude);
        return p;
    }

    @Override
    public int compareTo(DadoRJ o) {
        if ((this.dataHora.compareTo(o.dataHora) == 0) && (this.ordemId == o.ordemId)) {
            return 0;
        }
        return -1;
    }

    @Override
    public Map<String, String> getMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        SimpleDateFormat dt = new SimpleDateFormat(Constants.DB_DATE_FORMAT);
        map.put("id", String.valueOf(this.id));
        map.put("coleta_id", String.valueOf(this.coletaId));
        map.put("linha_id", String.valueOf(this.linhaId));
        map.put("ordem_id", String.valueOf(this.ordemId));
        map.put("latitude", String.valueOf(this.latitude));
        map.put("longitude", String.valueOf(this.longitude));
        map.put("velocidade", String.valueOf(this.velocidade));
        map.put("data_hora", "'" + dt.format(this.dataHora) + "'");
        return map;
    }
}
