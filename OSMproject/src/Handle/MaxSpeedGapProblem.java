package Handle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Model.Node;
import Model.Way;

public class MaxSpeedGapProblem extends Problem{
	
	static final Logger LOG = Logger.getLogger(MaxSpeedGapProblem.class.getName());
	
	private int count_speed = 0;
	
	int gap;
	
	Connection con = null;
	Statement stm = null;

	/*
	 * check if there are any incorrect max speed problem between two connected ways which have same street name
	 * firstly, compare two ways by two "for" loop to get every two connected way, way 'i' and way 'j', for example
	 * in addition, if two ways are connected, there are four situation
	 * 1. i.beginNode == j.beginNode
	 * 2. i.beginNode == j.endNode
	 * 3. i.endNode == j.beginNode
	 * 4. i.endNode == j.endNode
	 * secondly, judge speed_gap = i.maxspeed - j.maxspeed
	 * */
	public void handle(List<Way> ways, int set_gap) {
		// TODO Auto-generated method stub
		
		gap = set_gap;
		
		int w1_node_size;
		int w2_node_size;
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
				//if two ways have same name, they are the same street
				if(!(ways.get(i).name.equals(ways.get(j).name))) {
					continue;
				}
				
				if(w1_begin.equals(w2_begin)) {
					handle(ways, i, j, w1_begin, w2_begin);
				}
				
				if(w1_end.equals(w2_begin)) {
					handle(ways,i,j,w1_end,w2_begin);
				}
				
				if(w1_begin.equals(w2_end)) {
					handle(ways,i,j,w1_begin,w2_end);
				}
				
				if(w1_end.equals(w2_end)) {
					handle(ways,i,j,w1_begin,w2_end);
				}
			}
		}
		LOG.log(Level.INFO, " speed gap between two ways which is more than 60: " + count_speed);
		
	}

	private boolean judge(Node w1_n1, Node w1_n2, Node w2_n1, Node w2_n2, int speed_gap) {
		// TODO Auto-generated method stub
		
		if(Math.abs(speed_gap) >= gap) {
			return true;
		}else {
			return false;
		}
	}
	
	private void handle(List<Way> ways, int i, int j, String w1_node, String w2_node) {
		int speed_gap = ways.get(i).maxspeed - ways.get(j).maxspeed;
		
		Node w1_n1;
		Node w1_n2;
		Node w2_n1;
		Node w2_n2;
		
		int w2_node_size = ways.get(j).nodes_ref.size();
		int w1_node_size = ways.get(i).nodes_ref.size();
		
		if(w1_node.equals(ways.get(i).nodes_ref.get(0))) {
			w1_n1 = getNode(w1_node);
			w1_n2 = getNode(ways.get(i).nodes_ref.get(1));
			w2_n1 = getNode(w2_node);
			if(w2_node.equals(ways.get(j).nodes_ref.get(0))) { 
				 w2_n2 = getNode(ways.get(j).nodes_ref.get(1));
			}else {
				if(w2_node_size == 2) {
					w2_n2 = getNode(ways.get(j).nodes_ref.get(0));
				}else {
					w2_n2 = getNode(ways.get(j).nodes_ref.get(w2_node_size-2));
				}
			}
		}else {
			w1_n1 = getNode(w1_node);
			if(w1_node_size == 2) {
				w1_n2 = getNode(ways.get(i).nodes_ref.get(0));
			}else {
				w1_n2 = getNode(ways.get(i).nodes_ref.get(w1_node_size-2));
			}
			w2_n1 = getNode(w2_node);
			if(w2_node.equals(ways.get(j).nodes_ref.get(0))) {
				w2_n2 = getNode(ways.get(j).nodes_ref.get(1));
			}else {
				if(w2_node_size == 2) {
					w2_n2 = getNode(ways.get(j).nodes_ref.get(0));
				}else {
					w2_n2 = getNode(ways.get(j).nodes_ref.get(w2_node_size-2));
				}
			}
		}
		if(judge(w1_n1,w1_n2,w2_n1,w2_n2,speed_gap)) {
			int id_i = ways.get(i).id;
			int id_j = ways.get(j).id;
			
			storeInSQLITE(id_i, id_j);
		}
	}
	
	private void storeInSQLITE(int id_i, int id_j) {

		try {
			if(con == null && stm == null) {
				Class.forName("org.sqlite.JDBC");
				con = DriverManager.getConnection("jdbc:sqlite:osm.db");
				stm = con.createStatement();
				con.setAutoCommit(false);	
			}
			
			String add = "INSERT INTO maxspeed VALUES('"+id_i+"','"+id_j+"')";
			stm.executeUpdate(add);
			con.commit();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOG.log(Level.INFO, "way(" + id_i + ") and way(" + id_j + ") have max speed problem");
		count_speed++;
	}
	
	private Node getNode(String id) {
		return DataHandle.getInstance().getOneNode(id);
	}
}
