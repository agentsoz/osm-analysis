package straightIssue;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import model.*;

public class oneWayDeadEnd {
	public static void main (String args[]){
		SAXReader reader = new SAXReader();
		List<Way> ways = new ArrayList<Way>();
		
		
		try {	
			Way way = new Way();
			Document document = reader.read(new File("src/res/way.xml"));
			Element root = document.getRootElement();
			List<Element> rList = root.elements();
			String thisID = new String();
			
			
 			
			// Get first node and last node of "oneway".
			String node1 = new String();
			String node2 = new String();
			
			for(Element w : rList) {
				List<Element> wList = w.elements();
				for(Element c1 : wList) {
					
					if(c1.getName().equals("tag") && c1.attributeValue("k").equals("oneway") && c1.attributeValue("v").equals("yes")){
						// Store otherways' node.
						thisID = w.attributeValue("id");
						String[] nodes = new String[10000000];
						int i = 0;
						for(Element w2 : rList){
							
							if(w2.attributeValue("id")!=thisID){
								List<Element> wList2 = w2.elements();
								for(Element n : wList2)
									if(n.getName() == "nd"){
										nodes[i++] = n.attributeValue("ref");
										
									}
							}
						}
						//Get first / last node.
						Iterator<Element> i1 = wList.iterator();
						Iterator<Element> i2 = wList.iterator();
						node1 = i1.next().attributeValue("ref");
						i2.next();
						while(i1.next().getName() == "nd")
							node2 = i2.next().attributeValue("ref");
						//Check if both first & last are in nodes[]
						int count = 0;
						for (String s : nodes){
							if(s == node1){
								count ++;
								System.out.println("node1 matched");
							}
							else if(s == node2){
								count ++;
								System.out.println("node2 matched");
							}
						}
						if(count != 2){
							System.out.println("find 1");
							way.id = w.attributeValue("id");
							ways.add(way);
						}
					}
				}
			}
			System.out.println("There are " +ways.size()+" one-ways are dead-end.");
			
			//Check if node 1 or node 2 is not in nodes[];
			
			
					
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
