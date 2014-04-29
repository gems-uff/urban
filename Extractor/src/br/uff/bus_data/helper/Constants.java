/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.helper;

/**
 *
 * @author schettino
 */
public class Constants {

    public static final int INDEX_DATA_HORA = 0;
    public static final int INDEX_ORDEM = 1;
    public static final int INDEX_LINHA = 2;
    public static final int INDEX_LATITUDE = 3;
    public static final int INDEX_LONGITUDE = 4;
    public static final int INDEX_VELOCIDADE = 5;

    public static final String KEY_DATA_HORA = "data_hora";
    public static final String KEY_ORDEM = "ordem";
    public static final String KEY_LINHA = "linha";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_VELOCIDADE = "velocidade";

    public static final String[] COLUNAS = {"DATAHORA", "ORDEM", "LINHA",
        "LATITUDE", "LONGITUDE", "VELOCIDADE"};

    public static final int STATUS_INICIADA = 1;
    public static final int STATUS_ENCERRADA_COM_SUCESSO = 2;
    public static final int STATUS_ENCERRADA_COM_ERROS = 3;

    public static final String MSG_ERRO_COLUNAS = "'Colunas do JSON em formato inválido'";

    public static final String KEY_COLUNAS = "COLUMNS";
    public static final String KEY_DADOS = "DATA";

}
