package straightIssue;

import java.io.File;
import java.util.*;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import model.*;

public class oneWayDeadEnd {
	
	public static void main(String args[]){
		deadEnd();
//		count();
	}
	
	//64
	public static void deadEnd(){
		SAXReader reader = new SAXReader();
		try{
			Document document = reader.read(new File("src/res/way.xml"));
			Element root = document.getRootElement();
			List<Element> rList = root.elements();
			Way way = new Way();
			List<Way> ways = new ArrayList<Way>();
			for(Element w : rList){
				List<Element> wList = w.elements();
				Set<String> s  = new HashSet<String>();
				int count  = 0;
				for(Element c : wList){
					//total nodes number
					if(c.getName().equals("tag") && c.attributeValue("k").equals("oneway")){
						for(Element c2: wList){
							if(c2.getName().equals("nd")){
								s.add(c2.attributeValue("ref"));
								count++;
							}
						}
						if(count != s.size()){
							way.id = w.attributeValue("ref");
							ways.add(way);
						}
					}
					//distinct nodes number
				}
			}
			System.out.println(ways.size() + " oneways are deadend.");
			
		}catch(DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//8081
	public static void count(){
		SAXReader reader = new SAXReader();
		try{
			Document document = reader.read(new File("src/res/way.xml"));
			Element root = document.getRootElement();
			List<Element> rList = root.elements();
			int count = 0;
			for(Element w : rList){
				List<Element> wList = w.elements();
				for(Element c : wList){
					if(c.getName().equals("tag") && c.attributeValue("k").equals("oneway")){
						count++;
					}
				}
			}
			System.out.println("There are "+count+" oneways.");
		}catch(DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//4150
	public static void a (){
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
						int connected = 0;
						System.out.println(connected);
						for (String s : nodes){
							if(node1.equals(s)){
								connected ++;
							}
							if(node2.equals(s)){
								connected ++;
							}
						}
						if(connected != 2){
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
