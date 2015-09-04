package br.uff.ic.ProvenanceCombined;

//import br.uff.ic.Provenance.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

 
/**
 *
 * @author thiago
 */

public class GeneratingConfigXMLCombined {

 
      public static ProvenanceMainCombined provenanceM = new ProvenanceMainCombined();

      public static void mainGeneratingConfigXML(String colors[][], String linhaIdBus[][]){

          //agenda.addContent("thiago");
            Element config = new Element("config");
            //config.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            //config.setAttribute("xsi:noNamespaceSchemaLocation", "config.xsd");
            //config.setContent("xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'    xsi:noNamespaceSchemaLocation='config.xsd'");
            
            //config.setNamespace("xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'    xsi:noNamespaceSchemaLocation='config.xsd'");
            //config.("xsi:noNamespaceSchemaLocation='config.xsd'");
            
            //Define Agenda como root

            Document documento = new Document(config);
            
            //==================================================================
            
            Element layoutbackbone = new Element("layoutbackbone");
            layoutbackbone.setText("Player");
            config.addContent(layoutbackbone);
            
            //==================================================================
            
            Element layoutAxis_X = new Element("layoutAxis_X");
            layoutAxis_X.setText("ObjectPosition_X");
            config.addContent(layoutAxis_X);
            
            //==================================================================
            
            Element layoutAxis_Y = new Element("layoutAxis_Y");
            layoutAxis_Y.setText("ObjectPosition_Y");
            config.addContent(layoutAxis_Y);
            
            //==================================================================
            
            Element imageLocation = new Element("imageLocation");
            imageLocation.setText("/images/BusMap.png");
            config.addContent(imageLocation);
            
            //==================================================================
            
            Element imageOffset_X = new Element("imageOffset_X");
            imageOffset_X.setText("-43.2206256");
            config.addContent(imageOffset_X);
            
            //==================================================================
            
            Element imageOffset_Y = new Element("imageOffset_Y");
            imageOffset_Y.setText("-22.9072658");
            config.addContent(imageOffset_Y);
            
            //==================================================================
            
            Element spatialLayoutPosition = new Element("spatialLayoutPosition");
            spatialLayoutPosition.setText("-0.0586224");
            config.addContent(spatialLayoutPosition);
            
            //==================================================================
            
            Element temporalLayoutscale = new Element("temporalLayoutscale");
            temporalLayoutscale.setText("1");
            config.addContent(temporalLayoutscale);
            
            //==================================================================
            
            Element showentitydate = new Element("showentitydate");
            showentitydate.setText("false");
            config.addContent(showentitydate);
            
            //==================================================================
            
            Element showentitylabel = new Element("showentitylabel");
            showentitylabel.setText("false");
            config.addContent(showentitylabel);
            
            //*******************************************************************************
            
            Element edgetype = new Element("edgetype");
            
            //__________________________________________
            
            Element edge = new Element("edge");
            edge.setText("Neutral");
            edgetype.addContent(edge);
            
            //__________________________________________
            
            Element edgestroke = new Element("edgestroke");
            edgestroke.setText("MAX");
            edgetype.addContent(edgestroke);
            
            //__________________________________________
            
            Element collapsefunction = new Element("collapsefunction");
            collapsefunction.setText("SUM");
            edgetype.addContent(collapsefunction);
            
            //__________________________________________
            
            config.addContent(edgetype);
            
            //*******************************************************************************
            //*******************************************************************************
            
            edgetype = new Element("edgetype");
            
            //__________________________________________
            
            edge = new Element("edge");
            edge.setText("DeltaV");
            edgetype.addContent(edge);
            
            //__________________________________________
            
            edgestroke = new Element("edgestroke");
            edgestroke.setText("MAX");
            edgetype.addContent(edgestroke);
            
            //__________________________________________
            
            collapsefunction = new Element("collapsefunction");
            collapsefunction.setText("SUM");
            edgetype.addContent(collapsefunction);
            
            //__________________________________________
            
            config.addContent(edgetype);
            
            //*******************************************************************************
            //*******************************************************************************
                        
            
            Element colorscheme = new Element("colorscheme");
            
            //__________________________________________
            
            Element attribute1 = new Element("attribute");
            attribute1.setText("Speed");
            colorscheme.addContent(attribute1);
            
            //__________________________________________
            
            Element classs = new Element("class");
            classs.setText("ActivityScheme");
            colorscheme.addContent(classs);
            
            //__________________________________________
            
            Element values = new Element("values");
            values.setText("");
            colorscheme.addContent(values);
            
            //__________________________________________
            
            Element goodvalue = new Element("goodvalue");
            goodvalue.setText("");
            colorscheme.addContent(goodvalue);
            
            //__________________________________________
            
            Element badvalue = new Element("badvalue");
            badvalue.setText("");
            colorscheme.addContent(badvalue);
            
            //__________________________________________
            
            config.addContent(colorscheme);

           //*******************************************************************************
           //*******************************************************************************
           //*******************************************************************************
          
            int linhaAtualLinhaIdBus = 0;
            
            while(!ProvenanceMainCombined.linhaIdBus[linhaAtualLinhaIdBus][0].equals("")
               && !ProvenanceMainCombined.linhaIdBus[linhaAtualLinhaIdBus][1].equals(""))
            {
                
                //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
                //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
                
                Element activitycolor = new Element("activitycolor");

                //__________________________________________
                Element attribute2 = new Element("attribute");
                attribute2.setText(ProvenanceMainCombined.linhaIdBus[linhaAtualLinhaIdBus][0]);//Linha
                activitycolor.addContent(attribute2);

                //__________________________________________
                Element value = new Element("value");
                value.setText(ProvenanceMainCombined.linhaIdBus[linhaAtualLinhaIdBus][1]);//ID
                activitycolor.addContent(value);

                //__________________________________________
                Element vercolor = new Element("vercolor");

                //@@@@@@@@@@@@@@@@@@@@@@@@@@@
                Element r = new Element("r");
                
                if(linhaAtualLinhaIdBus < ProvenanceMainCombined.MAX_colors)
                {
                    r.setText(ProvenanceMainCombined.colors[linhaAtualLinhaIdBus][0]);
                }else{
                    int rRGB = (int) (256 * Math.random());
                    r.setText("" + rRGB);
                }
                
                vercolor.addContent(r);

                //_______________________
                Element g = new Element("g");
                
                if(linhaAtualLinhaIdBus < ProvenanceMainCombined.MAX_colors)
                {
                    g.setText(ProvenanceMainCombined.colors[linhaAtualLinhaIdBus][1]);
                }else{
                    int gRGB = (int) (256 * Math.random());
                    g.setText("" + gRGB);
                }
                
                vercolor.addContent(g);

                //_______________________
                Element b = new Element("b");
                
                if(linhaAtualLinhaIdBus < ProvenanceMainCombined.MAX_colors)
                {
                    b.setText(ProvenanceMainCombined.colors[linhaAtualLinhaIdBus][2]);
                }else{
                    int bRGB = (int) (256 * Math.random());
                    b.setText("" + bRGB);
                }
                
                vercolor.addContent(b);

                //@@@@@@@@@@@@@@@@@@@@@@@@@@@
                activitycolor.addContent(vercolor);

                //__________________________________________
                config.addContent(activitycolor);
                
                
                //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
                //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
                
                linhaAtualLinhaIdBus++;
            }
            
            
            
                        
           //*******************************************************************************
           //*******************************************************************************
           //*******************************************************************************
            

            XMLOutputter xout = new XMLOutputter();

           

                  try {

                        //Criando o arquivo de saida

                        FileWriter arquivo = new FileWriter(

                                                 new File("bus_config.xml"));

                        //Imprimindo o XML no arquivo

                        //arquivo.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");  
                        xout.output(documento, arquivo);

                  } catch (IOException e) {

                        e.printStackTrace();

                  }    

           

      }

 

}