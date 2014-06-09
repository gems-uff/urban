/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.models;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author schettino
 */
public class Linha implements Comparable<Linha>, Mappable<String,String>{

    String numero;
    long id;

    public Linha(String numero, int id) {
        this.numero = numero;
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    

    @Override
    public int compareTo(Linha o) {
        if (this.numero.equals(o.numero)) {
            return 0;
        }
        return -1;
    }

    @Override
    public Map<String, String> getMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", String.valueOf(this.id));
        map.put("linha", "'"+ this.numero + "'");
        return map;
    }

}


