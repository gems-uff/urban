/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.Provenance;



/**
 *
 * @author thiago
 */
public class ProvenanceMain {
    
    //////////////////THOP/////////////////
    //Máximo de pontos no mapa
    public static int QUANTIDADE_DE_PONTOS = 10000;
    //////////////////THOP/////////////////
    
    //////////////////THOP222222/////////////////
    public static String String_SQL = "line_id = 94 and time >= '2015-05-02 06:00:00' and time < '2015-05-02 07:00:00'";
    //public static String String_SQL = "bus_id = 5 and time > '2015-05-02 16:53:27' and time <= '2015-05-02 17:00:59'";    
    //////////////////THOP222222/////////////////
    
    
    //////////////////THOP3333333333/////////////////
    public static final int LIN_linhaIdBus = 500;//MAXIMO DE ONIBUS DIFERENTES
    public static final int COL_linhaIdBus = 6;    
    //////////////////THOP3333333333/////////////////
    
    public static String linhaIdBus[][] = new String[LIN_linhaIdBus][COL_linhaIdBus];
    //coluna 0: linha 
    //coluna 1: ID    
    //coluna 2: VelocidadeANTIGA (Para o cálculo do DeltaVAtual)
              
    //coluna 3: id da última aresta(edge) (idEdgeAnterior)
    //armazena o número do vértice que será impresso na edge em targetID. Ex: <targetID>vertex_16</targetID>
    
    //coluna 4: 0 ou 1, para saber se o Agent já foi criado
    //coluna 5: 0 ou 1, para saber se a primeira edge desse Agente já foi criada
    
    
    public static GeneratingConfigXML gConfigXML = new GeneratingConfigXML();
    public static GeneratingXML gXML = new GeneratingXML();
    
    //Máximo de cores pré-definidas
    public static final int MAX_colors = 16;
    
    //0: red  //1: green  //blue: 2
    public static String colors[][] = new String[MAX_colors][3];
    
    public static int contador_de_edges = 0;//diz em qual edges está, nada de complexo
    
    //Inserindo linha e id, caso apareçam pela  primeira vez no vetor linhaIdBus:
    public static int insereLinhaEId(String linha, String id)
    {        
        int posicaoLin = -1;
        
        for(int i = 0; i < LIN_linhaIdBus; i++)
        {
            if (linhaIdBus[i][0].equals("") && linhaIdBus[i][1].equals("")) 
            {
                linhaIdBus[i][0] = linha;
                linhaIdBus[i][1] = id;
                posicaoLin = i;
                break;
            }

            if (linha.toUpperCase().equals(linhaIdBus[i][0].toUpperCase()) 
             && id.toUpperCase().equals(linhaIdBus[i][1].toUpperCase())) 
            {
                posicaoLin = i;
                break;
            }
        }
        
        return posicaoLin;
    }
    
    
    public static void main(String[] args) {
        
        int i = 0;
        int j = 0;
        
        //Inicializando matriz com as linhas e IDs dos ônibus
        for(i = 0; i < LIN_linhaIdBus; i++)
        {
            for(j = 0; j < COL_linhaIdBus; j++)
            {
                if(j == 2 || j == 3 || j == 4 || j == 5)
                {
                    linhaIdBus[i][j] = "0";
                }else{
                    linhaIdBus[i][j] = "";
                }
            }
        }
        
        
        //Cores pré-definidas para as "linhas de Bus"
        //      Red             Green               Blue
        colors[0][0] = "0";  colors[0][1] = "0";  colors[0][2] = "130";
        colors[1][0] = "255";  colors[1][1] = "255";  colors[1][2] = "0";
        colors[2][0] = "255";  colors[2][1] = "164";  colors[2][2] = "36";
        colors[3][0] = "0";  colors[3][1] = "0";  colors[3][2] = "0";
        colors[4][0] = "255";  colors[4][1] = "107";  colors[4][2] = "184";
        colors[5][0] = "90";  colors[5][1] = "143";  colors[5][2] = "6";
        colors[6][0] = "0";  colors[6][1] = "209";  colors[6][2] = "202";
        colors[7][0] = "219";  colors[7][1] = "179";  colors[7][2] = "138";
        colors[8][0] = "255";  colors[8][1] = "0";  colors[8][2] = "0";        
        colors[9][0] = "170";  colors[9][1] = "0";  colors[9][2] = "179";
        colors[10][0] = "0";  colors[10][1] = "0";  colors[10][2] = "255";
        colors[11][0] = "181";  colors[11][1] = "79";  colors[11][2] = "38";
        colors[12][0] = "117";  colors[12][1] = "93";  colors[12][2] = "65";
        colors[13][0] = "152";  colors[13][1] = "199";  colors[13][2] = "0";
        colors[14][0] = "118";  colors[14][1] = "153";  colors[14][2] = "107";
        colors[15][0] = "0";  colors[15][1] = "100";  colors[15][2] = "107";
        
        
        GeneratingXML.mainGeneratingXML(linhaIdBus);
        GeneratingConfigXML.mainGeneratingConfigXML(colors, linhaIdBus);
    }
    
    
}
