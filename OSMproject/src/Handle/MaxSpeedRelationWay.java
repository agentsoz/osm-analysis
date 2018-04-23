package Handle;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class MaxSpeedRelationWay {

	static final Logger LOG = Logger.getLogger(MaxSpeedRelationWay.class.getName());
	
	Way way;
	
	public void getRelation() {
		
		SAXReader reader = new SAXReader();
		try {	
			Document document = reader.read(new File("src/res/relation.xml"));
			Element root = document.getRootElement();
			List<Element> list = root.elements();
			
			Relation relation;
			
			for(Element e:list) {
				
				relation = new Relation();
				way = new Way();
				way.id = Integer.parseInt(e.attributeValue("id"));
				relation.id = Integer.parseInt(e.attributeValue("id"));
				List<Element> cList = e.elements();
				for(Element c:cList) {
					if(c.getName().equals("maxspeed")) {
						way.nodes_ref.add(c.attributeValue("ref"));
						
					}else if(c.getName().equals("way")) {
						if(c.attributeValue("k").equals("maxspeed")) {
							count_maxspeed++;
							way.maxspeed = Integer.parseInt(c.attributeValue("v"));
						}else if(c.attributeValue("k").equals("name")) {
							
							way.name = c.attributeValue("v");
						}else if(c.attributeValue("k").equals("oneway")) {
							way.oneWay = c.attributeValue("v");
						}else if(c.attributeValue("k").equals("lanes")) {
							way.lanes = c.attributeValue("v");
						}
					}
				}
				ways.add(way);
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			LOG.log(Level.INFO, "how many ways in this area: " + ways.size());
			LOG.log(Level.INFO, "how many streets don't have name: " + count_streetNameNull);
			LOG.log(Level.INFO, "how many ways have max speed: " + count_maxspeed);
		}
	}
	
}
