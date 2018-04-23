package Handle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Handle_Main {
	
	static final Logger LOG = Logger.getLogger(Handle_Main.class.getName());
	
	static List<Way> ways;
	
	public static void main(String args[]) {
		/*
		if(args == null || args.length == 0) {
		//	LOG.log(Level.SEVERE, "you should input what kind of problem you want");
			System.err.println("Missing argument");
		}else {
			getWays();
			if(args[0].equals("max speed problem")) {
				int speed_gap = Integer.parseInt(args[1]);
				MaxSpeedGapProblem.getInstance().inCorrectMaxSpeed(ways, speed_gap);
			}
		}
		*/
		getWays();
		MaxSpeedGapProblem.getInstance().inCorrectMaxSpeed(ways, 60);
		
	}
	
	public static void getWays() {
		
		int count_maxspeed = 0;
		int count_streetNameNull = 0;
		ways = new ArrayList<>();
		Way way;
		
		SAXReader reader = new SAXReader();
		try {	
			Document document = reader.read(new File("src/res/way.xml"));
			Element root = document.getRootElement();
			List<Element> list = root.elements();
			
			for(Element e:list) {

				way = new Way();
				way.id = Integer.parseInt(e.attributeValue("id"));
				List<Element> cList = e.elements();
				for(Element c:cList) {
					if(c.getName().equals("nd")) {
						way.nodes_ref.add(c.attributeValue("ref"));
						
					}else if(c.getName().equals("tag")) {
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
