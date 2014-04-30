/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.bus_data.models;

import java.util.Map;

/**
 *
 * @author schettino
 * @param <T1>
 * @param <T2>
 */
public interface Mappable<T1, T2> {
    public Map<T1,T2> getMap();
}
