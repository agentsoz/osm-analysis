

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class OpenStreetMap {
    //check all node  
    @SuppressWarnings("unchecked")
    public static void listNodes(Element node){  
        System.out.println("Node Name：" + node.getName());  
        //get all attributes  
        List<Attribute> list = node.attributes();  
       
        for(Attribute attribute : list){  
            System.out.println("attribute"+attribute.getName() +":" + attribute.getValue());  
        }  
        //if node is not empty 
        if(!(node.getTextTrim().equals(""))){  
             System.out.println( node.getName() + "：" + node.getText());    
        }  
        SAXReader reader = new SAXReader ();
        
        
        Iterator<Element> iterator = node.elementIterator();  
        while(iterator.hasNext()){  
            Element e = iterator.next();  
            listNodes(e);  
        }  
    }  
    
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException,
            DocumentException {
        // point
        File pointFile = new File("/Users/Jason/Desktop/MapData/point.text");
        // arc
        File arcFile = new File("/Users/Jason/Desktop/MapData/rc.text");
        FileOutputStream fosPoint = new FileOutputStream(pointFile);
        FileOutputStream fosArc = new FileOutputStream(arcFile);
        OutputStreamWriter oswPoint = new OutputStreamWriter(fosPoint);
        OutputStreamWriter oswArc = new OutputStreamWriter(fosArc);
        BufferedWriter bwPoint = new BufferedWriter(oswPoint);
        BufferedWriter bwArc = new BufferedWriter(oswArc);
        SAXReader reader = new SAXReader();
        
        // data file path
        String path = "/Users/Jason/Desktop/MapData/map (1).osm in csv/map (1).osm";
        Document document = reader.read(new File(path));
        
        // get osm element
        Element root = document.getRootElement();
        
        Iterator<Element> iterator = root.elementIterator();  
        while(iterator.hasNext()){  
            Element e = iterator.next(); 
//            if(e.getName().equals("node")||e.getName().equals("way"))
//                listNodes(e);
            //out all point
            if(e.getName().equals("node")){
                StringBuilder sb = new StringBuilder();
                //get all attributes 
                List<Attribute> list = e.attributes();  
                  
                for(Attribute attribute : list){
                    if(attribute.getName().equals("id"))
                        sb.append(attribute.getValue()+"      ");
                    if(attribute.getName().equals("lat"))
                        sb.append(attribute.getValue()+"      ");
                    if(attribute.getName().equals("lon"))
                        sb.append(attribute.getValue());
                }  
                bwPoint.write(sb.toString()+"\r\n");
                bwPoint.flush();
                System.out.println(sb.toString());
            }else if(e.getName().equals("way")){  //out all arc
                StringBuilder sb = new StringBuilder();
                String s = "";
                 
                List<Attribute> list = e.attributes();  
                 
                for(Attribute attribute : list){
                    if(attribute.getName().equals("id"))
                        s += attribute.getValue()+"      ";
                    if(attribute.getName().equals("version"))
                        s += attribute.getValue()+"      ";
                }
               
                Iterator<Element> iter = e.elementIterator();  
                while(iter.hasNext()){  
                    Element element = iter.next();  
                  
                    List<Attribute> list1 = element.attributes();  
                     
                    for(Attribute attribute : list1){
                        if(attribute.getName().equals("ref"))
                            sb.append(s + attribute.getValue()+"      "+"\r\n");
                        else if(attribute.getName().equals("k"))
                            sb.append(s + "         "+attribute.getValue()+"\r\n");
                    }
                }
                bwArc.write(sb.toString());
                bwArc.flush();
                System.out.print(sb.toString());
            }
        } 
       bwPoint.close();
       oswPoint.close();
       fosPoint.close();
       bwArc.close();
       oswArc.close();
       fosArc.close();
       System.out.println("output finished!");
   }
}