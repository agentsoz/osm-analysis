package Handle;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MaxSpeedGapProblem {
	
	static final Logger LOG = Logger.getLogger(MaxSpeedGapProblem.class.getName());
	
	static HandleNode ins;
	
	/*
	 * check if there are any incorrect max speed problem between two connected ways
	 * firstly, compare two ways by two "for" loop to get every two connected way, way 'i' and way 'j', for example
	 * in addition, if two ways are connected, there are four situation
	 * 1. i.beginNode == j.beginNode
	 * 2. i.beginNode == j.endNode
	 * 3. i.endNode == j.beginNode
	 * 4. i.endNode == j.endNode
	 * secondly, judge speed_gap = i.maxspeed - j.maxspeed
	 * thirdly, judge if the included angle of two nodes <= 90
	 * */
	public static void inCorrectMaxSpeed(List<Way> ways) {
		// TODO Auto-generated method stub
		
		ins = HandleNode.getInstance();
		
		int w1_node_size;
		int w2_node_size;
		int count_speed = 0;
		//the first way
		for(int i=0;i<ways.size() - 1;i++) {
			//the number of nodes of the first way
			w1_node_size = ways.get(i).nodes_ref.size();
			//the begin node of the first way
			String w1_begin = ways.get(i).nodes_ref.get(0);
			//the end node of the first way
			String w1_end = ways.get(i).nodes_ref.get(w1_node_size-1);
			//the second way
			for(int j=i+1;j<ways.size();j++) {
				//the number of nodes of the second way
				w2_node_size = ways.get(j).nodes_ref.size();
				//the begin node of the second way
				String w2_begin = ways.get(j).nodes_ref.get(0);
				//the end node of the second way
				String w2_end = ways.get(j).nodes_ref.get(w2_node_size-1);
				//the speed gap
				int speed_gap;
				//if two ways have same name, they are the same street
				if(ways.get(i).name.equals(ways.get(j).name)) {
					continue;
				}
				
				if(w1_begin.equals(w2_begin)) {
					speed_gap = ways.get(i).maxspeed - ways.get(j).maxspeed;
					
					Node w1_n1 = ins.getNode(w1_begin);
					Node w1_n2 = ins.getNode(ways.get(i).nodes_ref.get(1));
					
					Node w2_n1 = ins.getNode(w2_begin);
					Node w2_n2 = ins.getNode(ways.get(j).nodes_ref.get(1));
					
					count_speed = count_speed + judge(w1_n1,w1_n2,w2_n1,w2_n2,speed_gap);
				}
				
				if(w1_end.equals(w2_begin)) {
					speed_gap = ways.get(i).maxspeed - ways.get(j).maxspeed;
					
					Node w1_n1 = ins.getNode(w1_end);
					Node w1_n2;
					if(w1_node_size == 2) {
						w1_n2 = ins.getNode(ways.get(i).nodes_ref.get(0));
					}else {
						w1_n2 = ins.getNode(ways.get(i).nodes_ref.get(w1_node_size-2));
					}
					
					Node w2_n1 = ins.getNode(w2_begin);
					Node w2_n2 = ins.getNode(ways.get(j).nodes_ref.get(1));
					
					count_speed = count_speed + judge(w1_n1,w1_n2,w2_n1,w2_n2,speed_gap);
				}
				
				if(w1_begin.equals(w2_end)) {
					speed_gap = ways.get(i).maxspeed - ways.get(j).maxspeed;
					
					Node w1_n1 = ins.getNode(w1_begin);
					Node w1_n2 = ins.getNode(ways.get(i).nodes_ref.get(1));
					
					Node w2_n1 = ins.getNode(w2_end);
					Node w2_n2;
					if(w2_node_size == 2) {
						w2_n2 = ins.getNode(ways.get(j).nodes_ref.get(0));
					}else {
						w2_n2 = ins.getNode(ways.get(j).nodes_ref.get(w2_node_size-2));
					}
					
					count_speed = count_speed + judge(w1_n1,w1_n2,w2_n1,w2_n2,speed_gap);
				}
				
				if(w1_end.equals(w2_end)) {
					speed_gap = ways.get(i).maxspeed - ways.get(j).maxspeed;
					
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
			
					count_speed = count_speed + judge(w1_n1,w1_n2,w2_n1,w2_n2,speed_gap);
				}
			}
		}
		LOG.log(Level.INFO, " speed gap between two ways which is more than 60: " + count_speed);
	}

	private static int judge(Node w1_n1, Node w1_n2, Node w2_n1, Node w2_n2, int speed_gap) {
		// TODO Auto-generated method stub
		boolean ifturn = ins.ifTurn(ins.getSlope(w1_n1, w1_n2), ins.getSlope(w2_n1, w2_n2));
		
		if(ifturn && Math.abs(speed_gap) > 60) {
			return 1;
		}else {
			return 0;
		}
	}
}
