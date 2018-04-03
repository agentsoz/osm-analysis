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

public class HandleWay {
	
	static final Logger LOG = Logger.getLogger(HandleWay.class.getName());
	
	List<Way> ways;
	
	static HandleWay instance;
	
	public static HandleWay getInstance() {
		if(instance == null) {
			instance = new HandleWay();
		}
		return instance;
	}
	
	public void getWays() {
		
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
							way.maxspeed = Integer.parseInt(c.attributeValue("v"));
						}else if(c.attributeValue("k").equals("name")) {
							way.name = c.attributeValue("v");
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
		}

	}
	
	public void inCorrectMaxSpeed() {
		// TODO Auto-generated method stub
		
		HandleNode ins = HandleNode.getInstance();
		
		int w1_node_size;
		int w2_node_size;
		int count_speed = 0;
		//第一条路
		for(int i=0;i<ways.size() - 1;i++) {
			//第一条路的node的数量
			w1_node_size = ways.get(i).nodes_ref.size();
			//第一条路的开始node
			String w1_begin = ways.get(i).nodes_ref.get(0);
			//第一条路的结束node
			String w1_end = ways.get(i).nodes_ref.get(w1_node_size-1);
			//第二条路
			for(int j=i+1;j<ways.size();j++) {
				//第二条路的node的数量
				w2_node_size = ways.get(j).nodes_ref.size();
				//第二条路的开始node
				String w2_begin = ways.get(j).nodes_ref.get(0);
				//第二条路的结束node
				String w2_end = ways.get(j).nodes_ref.get(w2_node_size-1);
				//速度差
				int speed_diff;
				//如果两条way有一个名字 说明是一条路
				if(ways.get(i).name.equals(ways.get(j).name)) {
					continue;
				}
				//如果第一条路的开始node和第二条路的开始node一样
				if(w1_begin.equals(w2_begin)) {
					speed_diff = ways.get(i).maxspeed - ways.get(j).maxspeed;
					
					Node w1_n1 = ins.getNode(w1_begin);
					Node w1_n2 = ins.getNode(ways.get(i).nodes_ref.get(1));
					
					Node w2_n1 = ins.getNode(w2_begin);
					Node w2_n2 = ins.getNode(ways.get(j).nodes_ref.get(1));
					
					boolean ifturn = ins.ifTurn(ins.getSlope(w1_n1, w1_n2), ins.getSlope(w2_n1, w2_n2));
					
					if(ifturn && Math.abs(speed_diff) > 60) {
						count_speed++;
					}
					
				}
				//如果第一条路的结束node和第二条路的开始node一样
				if(w1_end.equals(w2_begin)) {
					speed_diff = ways.get(i).maxspeed - ways.get(j).maxspeed;
					
					Node w1_n1 = ins.getNode(w1_end);
					Node w1_n2;
					if(w1_node_size == 2) {
						w1_n2 = ins.getNode(ways.get(i).nodes_ref.get(0));
					}else {
						w1_n2 = ins.getNode(ways.get(i).nodes_ref.get(w1_node_size-2));
					}
					
					Node w2_n1 = ins.getNode(w2_begin);
					Node w2_n2 = ins.getNode(ways.get(j).nodes_ref.get(1));
			
					boolean ifturn = ins.ifTurn(ins.getSlope(w1_n1, w1_n2), ins.getSlope(w2_n1, w2_n2));
					
					if(ifturn && Math.abs(speed_diff) > 60) {
						count_speed++;
					}
					
				}
				//如果第一条路的开始node和第二条路的结束node一样
				if(w1_begin.equals(w2_end)) {
					speed_diff = ways.get(i).maxspeed - ways.get(j).maxspeed;
					
					Node w1_n1 = ins.getNode(w1_begin);
					Node w1_n2 = ins.getNode(ways.get(i).nodes_ref.get(1));
					
					Node w2_n1 = ins.getNode(w2_end);
					Node w2_n2;
					if(w2_node_size == 2) {
						w2_n2 = ins.getNode(ways.get(j).nodes_ref.get(0));
					}else {
						w2_n2 = ins.getNode(ways.get(j).nodes_ref.get(w2_node_size-2));
					}
					 
			
					boolean ifturn = ins.ifTurn(ins.getSlope(w1_n1, w1_n2), ins.getSlope(w2_n1, w2_n2));
					
					if(ifturn && Math.abs(speed_diff) > 60) {
						count_speed++;
					}
					
				}
				//如果第一条路的结束node和第二条路的结束node一样
				if(w1_end.equals(w2_end)) {
					speed_diff = ways.get(i).maxspeed - ways.get(j).maxspeed;
					
					Node w1_n1 = ins.getNode(w1_end);
					Node w1_n2;
					if(w1_node_size == 2) {
						w1_n2 = ins.getNode(ways.get(i).nodes_ref.get(0));
					}else {
						w1_n2 = ins.getNode(ways.get(i).nodes_ref.get(w1_node_size-2));
					}

					Node w2_n1 = ins.getNode(w2_end);
					Node w2_n2;
					if(w2_node_size == 2) {
						w2_n2 = ins.getNode(ways.get(j).nodes_ref.get(0));
					}else {
						w2_n2 = ins.getNode(ways.get(j).nodes_ref.get(w2_node_size-2));
					}
			
					boolean ifturn = ins.ifTurn(ins.getSlope(w1_n1, w1_n2), ins.getSlope(w2_n1, w2_n2));
					
					if(ifturn && Math.abs(speed_diff) > 60) {
						count_speed++;
					}
					
				}
			}
		}
		LOG.log(Level.INFO, " speed difference more than 60: " + count_speed);
	}
	//not finish yet
	public void repeatedStreetName() {
		String w1_name;
		String w2_name;
		int count_repeat = 0;
		for(int i=0;i<ways.size();i++) {
			w1_name = ways.get(i).name;
			for(int j=i+1;j<ways.size()-1;j++) {
				w2_name = ways.get(j).name;
				String w1_begin_node = ways.get(j).nodes_ref.get(0);
			//	String w1_end_node = ways.get(j)
				if(w1_name.equals(w2_name)) {
					count_repeat++;
				}
			}
		}
		LOG.log(Level.INFO, "the number of two streets having same name:" + count_repeat);
	}

}
