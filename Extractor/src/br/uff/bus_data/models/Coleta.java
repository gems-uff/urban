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
public class Coleta implements Comparable<Coleta>, Mappable<String, String> {

    long id;
    int status;
    Date dataHoraFim;
    Date dataHoraInicio;
    String erros;
    String filename;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(Date dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }

    public Date getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(Date dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public String getErros() {
        return erros;
    }

    public void setErros(String erros) {
        this.erros = erros;
    }

    @Override
    public int compareTo(Coleta o) {
        if ((this.dataHoraInicio.compareTo(o.dataHoraInicio) == 0) && (this.filename.equals(o.filename))) {
            return 0;
        }
        return -1;
    }

    @Override
    public Map<String, String> getMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        SimpleDateFormat dt = new SimpleDateFormat(Constants.DB_DATE_FORMAT);
        map.put("id", String.valueOf(this.id));
        map.put("status", String.valueOf(this.status));
        map.put("data_hora_fim", "'" + dt.format(this.dataHoraFim) + "'");
        map.put("data_hora_inicio", "'" + dt.format(this.dataHoraInicio) + "'");
        map.put("erros", "'" + erros + "'");
        map.put("filename", "'" + filename + "'");
        return map;
    }
}
