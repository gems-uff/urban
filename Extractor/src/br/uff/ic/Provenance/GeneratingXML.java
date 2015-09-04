package br.uff.ic.Provenance;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


import br.uff.bus_data.dao.BusPositionDAO;
import br.uff.bus_data.dao.LineDAO;
import br.uff.bus_data.dao.BusDAO;
import br.uff.bus_data.dbConnection.DBConnectionInterface;
import br.uff.bus_data.dbConnection.PostgresDBConnection;
import br.uff.bus_data.models.BusPosition;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;

 
/**
 *
 * @author thiago
 */

public class GeneratingXML {

    public static ProvenanceMain provenanceM = new ProvenanceMain();
 
public static void rodarVertex(int i, BusPosition busPosition){
    
    //JOptionPane.showMessageDialog(null, "" + busPosition.getBusId() + "\n" + busPosition.getId());
    
    String linhaBus = "Linha";
    String idBus = "" + busPosition.getBusId();
    
    //--------------------------------------------------------------------------
    //Verificando e inserindo caso a linha e o id aparecem pela primeira vez no vetor linhaIdBus:
    int posicaoLin = 0;
    posicaoLin = ProvenanceMain.insereLinhaEId(linhaBus, idBus);
    
    //--------------------------------------------------------------------------
    
    Element id_vertex = new Element("ID");
    id_vertex.setText("vertex_" + i);
    vertex.addContent(id_vertex);
    
    //======================================
    
    Element type_vertex = new Element("type");
    if(ProvenanceMain.linhaIdBus[posicaoLin][4].equals("0"))
    {        
        type_vertex.setText("Agent");
    }else{
        type_vertex.setText("Activity");
    }
    vertex.addContent(type_vertex);
    
    //======================================
    
    Element label_vertex = new Element("label");
    label_vertex.setText("Onibus" + idBus);
    vertex.addContent(label_vertex);
    
    //======================================
    
    Element date_vertex = new Element("date");
    String data_antiga = "" + busPosition.getTime();
    
    String nova_data = "";
    //JOptionPane.showMessageDialog(null, data_antiga.substring(11, 19));
    nova_data += data_antiga.substring(11, 19);
    nova_data += " ";
    
    nova_data += data_antiga.substring(8, 10);//Dia
    //JOptionPane.showMessageDialog(null, data_antiga.substring(8, 10));
    nova_data += "/";
    String mes = data_antiga.substring(4,7);
    //JOptionPane.showMessageDialog(null, data_antiga.substring(4, 7));
    if(mes.equals("Jan")){nova_data += "01";}
    if(mes.equals("Feb")){nova_data += "02";}
    if(mes.equals("Mar")){nova_data += "03";}
    if(mes.equals("Apr")){nova_data += "04";}
    if(mes.equals("May")){nova_data += "05";}
    if(mes.equals("Jun")){nova_data += "06";}//June
    if(mes.equals("Jul")){nova_data += "07";}//July
    if(mes.equals("Aug")){nova_data += "08";}
    if(mes.equals("Sep")){nova_data += "09";}//Sept
    if(mes.equals("Oct")){nova_data += "10";}
    if(mes.equals("Nov")){nova_data += "11";}
    if(mes.equals("Dec")){nova_data += "12";}
    nova_data += "/";
    nova_data += data_antiga.substring(24, 28);//Ano
    
    date_vertex.setText(nova_data);
    vertex.addContent(date_vertex);
    
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    Element attributes_vertex = new Element("attributes");
    
    //##################################################
    
    Element attribute_vertex;
    attribute_vertex = new Element("attribute");
    
    //======================================
    
    Element name1_vertex = new Element("name");
    name1_vertex.setText(linhaBus);
    attribute_vertex.addContent(name1_vertex);
    
    //======================================
    
    Element value1_vertex = new Element("value");
    value1_vertex.setText(idBus);
    attribute_vertex.addContent(value1_vertex);
    
    //======================================
    
    attributes_vertex.addContent(attribute_vertex);
    
    //##################################################
    //##################################################
        
    if(ProvenanceMain.linhaIdBus[posicaoLin][4].equals("0"))
    {
        ProvenanceMain.linhaIdBus[posicaoLin][4] = "1";
        
        //======================================
        //======================================
        
        attribute_vertex = new Element("attribute");

        //======================================
        Element name2_vertex = new Element("name");
        name2_vertex.setText("ObjectTag");
        attribute_vertex.addContent(name2_vertex);

        //======================================
        Element value2_vertex = new Element("value");
        value2_vertex.setText("Untagged");
        attribute_vertex.addContent(value2_vertex);

        //======================================
        attributes_vertex.addContent(attribute_vertex);

        //##################################################
        //##################################################
        attribute_vertex = new Element("attribute");

        //======================================
        Element name3_vertex = new Element("name");
        name3_vertex.setText("ObjectID");
        attribute_vertex.addContent(name3_vertex);

        //======================================
        Element value3_vertex = new Element("value");
        value3_vertex.setText("" + busPosition.getBusId());
        attribute_vertex.addContent(value3_vertex);

        //======================================
        attributes_vertex.addContent(attribute_vertex);

    }
    //##################################################
    //##################################################
        
    attribute_vertex = new Element("attribute");
    
    //======================================
    
    Element name4_vertex = new Element("name");
    name4_vertex.setText("ObjectPosition_X");
    attribute_vertex.addContent(name4_vertex);
    
    //======================================
    
    Element value4_vertex = new Element("value");
    value4_vertex.setText("" + busPosition.getLongitude());
    attribute_vertex.addContent(value4_vertex);
    
    //======================================
    
    attributes_vertex.addContent(attribute_vertex);
    
    //##################################################
    //##################################################
        
    attribute_vertex = new Element("attribute");
    
    //======================================
    
    Element name5_vertex = new Element("name");
    name5_vertex.setText("ObjectPosition_Y");
    attribute_vertex.addContent(name5_vertex);
    
    //======================================
    
    Element value5_vertex = new Element("value");
    value5_vertex.setText("" + busPosition.getLatitude());
    attribute_vertex.addContent(value5_vertex);
    
    //======================================
    
    attributes_vertex.addContent(attribute_vertex);
    
    //##################################################
    //##################################################
        
    attribute_vertex = new Element("attribute");
    
    //======================================
    
    Element name6_vertex = new Element("name");
    name6_vertex.setText("Speed");
    attribute_vertex.addContent(name6_vertex);
    
    //======================================
    
    Element value6_vertex = new Element("value");
    value6_vertex.setText("" + busPosition.getSpeed());
    attribute_vertex.addContent(value6_vertex);
    
    //======================================
    
    attributes_vertex.addContent(attribute_vertex);
    
    //##################################################
    
    vertex.addContent(attributes_vertex);
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
}

//______________________________________________________________________________    

public static void rodarEdge(int k,  BusPosition busPosition)
{
    
    
    edge = new Element("edge");                         
    //################################################################################################33    
    
    Element id_edge = new Element("ID");
    id_edge.setText("edge_" + ProvenanceMain.contador_de_edges);
    edge.addContent(id_edge);
    
    //==============================================
    
    Element label_edge = new Element("label");
    label_edge.setText("Neutral");
    edge.addContent(label_edge);
    
    //==============================================
    
    Element type_edge = new Element("type");
    type_edge.setText("WasInfluencedBy");
    edge.addContent(type_edge);
    
    //==============================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //Procurando qual a edege anterior e somando +1 à ela
    
    double VelocidadeAntiga = 0.0;    
    double VelocidadeAtual = busPosition.getSpeed();
    //
    int idEdgeAnterior = 0;
    int idEdgeAtual = 0;
    int pos = 0;
       
    while(!ProvenanceMain.linhaIdBus[pos][0].equals("") || !ProvenanceMain.linhaIdBus[pos][1].equals(""))
    {
        //JOptionPane.showMessageDialog(null, "" + provenanceM.linhaIdBus[pos][0]);
        if(ProvenanceMain.linhaIdBus[pos][0].equals("Linha") 
        && ProvenanceMain.linhaIdBus[pos][1].equals("" + busPosition.getBusId()))
        {            
            VelocidadeAntiga = Double.parseDouble(ProvenanceMain.linhaIdBus[pos][2]);
            ProvenanceMain.linhaIdBus[pos][2] = "" + VelocidadeAtual;
            //
            idEdgeAnterior = Integer.parseInt(ProvenanceMain.linhaIdBus[pos][3]);
            idEdgeAtual = k;
            ProvenanceMain.linhaIdBus[pos][3] = "" + idEdgeAtual;
            break;
        }
        
        pos++;
    }
    
    
    
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================
    
    Element value_edge = new Element("value");
    
    
    
    double DeltaVAtual = VelocidadeAtual - VelocidadeAntiga;
    //JOptionPane.showMessageDialog(null, "VelocidadeAntiga: " + VelocidadeAntiga);
    //JOptionPane.showMessageDialog(null, "VelocidadeAtual: " + VelocidadeAtual);
    //JOptionPane.showMessageDialog(null, "DeltaVAtual: " + DeltaVAtual);
    
    value_edge.setText("" + DeltaVAtual);
    edge.addContent(value_edge);
    
    //==============================================
    
    Element sourceID_edge = new Element("sourceID");
    sourceID_edge.setText("vertex_" + idEdgeAtual);//edge atual
    edge.addContent(sourceID_edge);
    
    //==============================================
    
    Element targetID_edge = new Element("targetID");
    targetID_edge.setText("vertex_" + idEdgeAnterior);//edge anterior
    edge.addContent(targetID_edge);
    
    //==============================================
    //################################################################################################33
    edges.addContent(edge);
    
    edge = new Element("edge");                         
    //################################################################################################33    
    
    id_edge = new Element("ID");
    id_edge.setText("edgeDeltaV_" + ProvenanceMain.contador_de_edges);
    edge.addContent(id_edge);
    
    //==============================================
    
    label_edge = new Element("label");
    label_edge.setText("DeltaV");
    edge.addContent(label_edge);
    
    //==============================================
    
    type_edge = new Element("type");
    type_edge.setText("WasInfluencedBy");
    edge.addContent(type_edge);
    
    //==============================================
    
    
    value_edge = new Element("value");
    
    
    value_edge.setText("" + DeltaVAtual);
    edge.addContent(value_edge);
    
    //==============================================
    
     sourceID_edge = new Element("sourceID");
    sourceID_edge.setText("vertex_" + idEdgeAtual);//edge atual
    edge.addContent(sourceID_edge);
    
    //==============================================
    
    targetID_edge = new Element("targetID");
    targetID_edge.setText("vertex_" + idEdgeAnterior);//edge anterior
    edge.addContent(targetID_edge);
    
    //==============================================
    //################################################################################################33
    edges.addContent(edge);
    
    ProvenanceMain.contador_de_edges = ProvenanceMain.contador_de_edges + 1;
    
}
 

//______________________________________________________________________________    
    
        public static Element provenancedata;
        public static Element vertices;
        public static Element vertex;
        public static Element edge;
        public static Element edges;
    
//______________________________________________________________________________
    
    
      public static void mainGeneratingXML(String linhaIdBus[][]) {

          //agenda.addContent("thiago");
            
        
             provenancedata = new Element("provenancedata");

            Document documento = new Document(provenancedata);
            
            //######################################################################################
            
            vertices = new Element("vertices");
            
            edges = new Element("edges");
            
            /*==========================================================================================
            ============================================================================================
            ==========================================================================================*/
            /*COMUNICANDO COM O BD, EXTRAINDO INFORMÇÕES E JOGANDO NO ARQUIVO XML*/
            
            int nVertex = 0;
            
            
            try {
            DBConnectionInterface dbCon = new PostgresDBConnection();
            Connection con = dbCon.dbConection();
            Statement stmt = null;
            stmt = con.createStatement();
            
            LineDAO lineDao = new LineDAO();
            lineDao.setStatement(stmt);
            BusDAO busDao = new BusDAO();
            busDao.setStatement(stmt);
            
            //--------------------------
            /*BusPositionDAO busPositionDao = new BusPositionDAO();
            busPositionDao.setStatement(stmt);
            HashMap<String, String> params = new HashMap<String, String>();
           params.put("bus_id", "5");
            
            List<BusPosition> busPositions = busPositionDao.select(params);*/
            //--------------------------
            
            BusPositionDAO busPositionDao = new BusPositionDAO();
            busPositionDao.setStatement(stmt);
            String params;
            params = ProvenanceMain.String_SQL;
            //params = "line_id = 94 and time >= '2015-05-02 06:00:00' and time < '2015-05-02 07:00:00'";
            
            List<BusPosition> busPositions = busPositionDao.newSelect(params);//newSelect fui eu quem criou THOP
            
            for (BusPosition busPosition : busPositions) {
                
                if(nVertex < ProvenanceMain.QUANTIDADE_DE_PONTOS)
                {
            //    JOptionPane.showMessageDialog(null, busPosition.getLongitude());                
                    vertex = new Element("vertex");
                    rodarVertex(nVertex, busPosition);
                    
                //if(nVertex % 50 == 0)
                    //{
                    // System.out.println(nVertex);
                    //}
                    vertices.addContent(vertex);
                    
                    
                    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
                    //Impressão das edges
                    
                    if (nVertex % 100 == 0) {
                        System.out.println("Total: " + nVertex);
                    }
                    
                    //thop00////Verificando se esse ID nunca apareceu na tabela ''ProvenanceMain.linhaIdBus''
                    //thop00//while
                    

                    int pos = 0;
                    double VelocidadeAtual = busPosition.getSpeed();
                    int aconteceu = 0;
                    while (!ProvenanceMain.linhaIdBus[pos][0].equals("") || !ProvenanceMain.linhaIdBus[pos][1].equals("")) 
                    {                                                                                                 
                        //JOptionPane.showMessageDialog(null, "" + provenanceM.linhaIdBus[pos][0]);
                        if (ProvenanceMain.linhaIdBus[pos][0].equals("Linha")
                         && ProvenanceMain.linhaIdBus[pos][1].equals("" + busPosition.getBusId())
                         && ProvenanceMain.linhaIdBus[pos][5].equals("0")   ) 
                        {
                            ProvenanceMain.linhaIdBus[pos][2] = "" + VelocidadeAtual;
                            ProvenanceMain.linhaIdBus[pos][3] = "" + nVertex;
                            
                            ProvenanceMain.linhaIdBus[pos][5] = "1";//avisa que é a primeira vez que essa linha aparece pedindo edge, 
                            //ela só é atendida na segunda vez, para fazer a ligação entre ATUAL e ANTERIOR
                            
                            aconteceu = 1;
                            break;
                        }
                        pos++;
                    }
                    
                    if(aconteceu == 0)
                    {                                           
                        rodarEdge(nVertex, busPosition);                                           
                    }
                    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
                    nVertex++;
                }
            }
            con.close();
            stmt.close();
            } catch (Exception e) {
            }
            
            
            
            /*==========================================================================================
            ============================================================================================
            ==========================================================================================*/
            //System.out.println("proxima etapa: " + nVertex);
            //Fechando os "vertices" == </vertices>
            provenancedata.addContent(vertices);
            
            provenancedata.addContent(edges);            
            
            //******************************************************************************
            
            //Abrindo as "edges" == <edges>
            //////////////THOP//////////////Element edges = new Element("edges");
            
            //******************************************************************************
           
            /*System.out.println("0");
            int nEdges = 0;
            for(int j = 0; j < nVertex; j++){
                nEdges = nEdges + j;
            }*/
            //JOptionPane.showMessageDialog(null, "nVertex: " + nVertex + "\nnEdges: " + nEdges);
            //System.out.println("1");
            /*//////////////THOP//////////////
            int a = 0;
            int b = 1;
            for(int k = 0; k < nVertex-1; k++){
                
                if(k < QUANTIDADE_DE_PONTOS)
                {
                    if (k % 100 == 0) {
                        System.out.println("k: " + k);
                    }

                    edge = new Element("edge");
                    rodarEdge(k, a, b);
                    edges.addContent(edge);

                    a++;
                    b++;
                //(b == nVertex){
                    //  b = b - a + 1;
                    //a = 0;
                    //}
                }
            }*/
           //System.out.println("2");
            //*******************************************************************************
            
            //Fechando as "edges" == </edges>
            //////////////THOP//////////////provenancedata.addContent(edges);            
            
           //*******************************************************************************
            

            XMLOutputter xout = new XMLOutputter();

           

                  try {

                        //Criando o arquivo de saida

                        FileWriter arquivo = new FileWriter(

                                                 new File("bus.xml"));

                        //Imprimindo o XML no arquivo

                        //arquivo.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");  
                        xout.output(documento, arquivo);

                  } catch (IOException e) {

                        e.printStackTrace();

                  }    

           

      }

 

}