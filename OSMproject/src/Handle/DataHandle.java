package Handle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Model.Member;
import Model.Node;
import Model.Relation;
import Model.Way;

public class DataHandle {
	
	static final Logger LOG = Logger.getLogger(DataHandle.class.getName());
	
	private static DataHandle instance = new DataHandle();
	
	public static DataHandle getInstance() {
		return instance;
	}
	
	private DataHandle() {}
	
	List<Way> ways = new ArrayList<>();
	List<Node> nodes = new ArrayList<>();
	List<Relation> relations = new ArrayList<>();
	
	Connection con = null;
	Statement stm = null;
	Statement stm1 = null;
	Statement stm2 = null;
	
	int count_maxspeed = 0;
	int count_relation = 0;
	int i=0;
	int j=0;
	
	public void prepare() {
		
		long getWay_Begin = 0;
		long getWay_end = 0;
		
		long getR_begin = 0;
		long getR_end = 0;
		
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:osm.db");
			stm = con.createStatement();
			stm1 = con.createStatement();
			stm2 = con.createStatement();
			
			getWay_Begin = System.currentTimeMillis();
			getWays();
			getWay_end = System.currentTimeMillis();
			
			stm = con.createStatement();
			stm1 = con.createStatement();
			stm2 = con.createStatement();
			
			getR_begin = System.currentTimeMillis();
			getRelations();
			getR_end = System.currentTimeMillis();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			LOG.severe("ClassNotFoundException");
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOG.severe("SQLException");
			e.printStackTrace();
		} finally {
			LOG.log(Level.INFO, "running time for get ways from database:" + (getWay_end - getWay_Begin) + "ms");
			LOG.log(Level.INFO, "how many ways in this area: " + ways.size());
			LOG.log(Level.INFO, "how many ways have max speed: " + count_maxspeed);
			
			LOG.log(Level.INFO, "running time for get relations from database:" + (getR_end - getR_begin) + "ms");
			LOG.log(Level.INFO, "how many relations in this areas:" + count_relation);
			LOG.log(Level.INFO, "how many ways which in the relation dont exist in the table ways:" + i);
			LOG.log(Level.INFO, "how many ways which in the relation exist in the table ways:" + j);
		}
	}

	//get every way from database
	public List<Way> getWays() throws SQLException {
		
		String sql = "SELECT ID FROM ways";
		ResultSet res = stm.executeQuery(sql);
		Way way;
		String wayID;

		while(res.next()) {
			way = new Way();
			wayID = res.getString("ID");
			way.id = Integer.parseInt(wayID);
			
			getTagInWay(way, wayID);
			
			getNodesInWay(way, wayID);

			ways.add(way);
		}	
		stm.close();
		return ways;
	}
	
	//get every tag in one way
	private void getTagInWay(Way way, String wayID) throws SQLException {
		String tags = "SELECT tag_key,tag_value FROM ways_tags WHERE way_id='"+wayID+"'";
		ResultSet res_t = stm1.executeQuery(tags);

		while(res_t.next()) {
			//TODO add tag from database to list when need it
			if(res_t.getString("tag_key").equals("maxspeed")) {
				way.maxspeed = Integer.parseInt(res_t.getString("tag_value"));
				count_maxspeed++;
			}
			if(res_t.getString("tag_key").equals("name")) {
				way.name = res_t.getString("tag_value");
			}
			if(res_t.getString("tag_key").equals("oneway")) {
				way.oneWay = res_t.getString("tag_value");
			}
			if(res_t.getString("tag_key").equals("lanes")) {
				way.lanes = res_t.getString("tag_value");
			}
			if(res_t.getString("tag_key").equals("bicycle")) {
				way.bicycle = res_t.getString("tag_value");
			}
		}
		stm1.close();
		res_t.close();
	}
	
	//get every node in one way
	private void getNodesInWay(Way way, String wayID) throws SQLException {
		String nodes = "SELECT node_id FROM ways_nodes WHERE way_id='"+wayID+"'";

		ResultSet res_n = stm2.executeQuery(nodes);
		while(res_n.next()) {
			way.nodes_ref.add(res_n.getString("node_id"));
		}
		stm2.close();
	}
	
	//get every relation
	public void getRelations() throws SQLException  {
			
		String a = "SELECT ID FROM relations";
			
		ResultSet res = stm.executeQuery(a);
			
		while(res.next()) {
			count_relation++;
			int relationID = res.getInt("ID");
			getMembers(relationID);
		}
	}
		
	//get every member in one relation
	private void getMembers(int relationID) throws SQLException {
		String b = "SELECT member_type,member_ref,member_role FROM relations_members WHERE relation_id='"+relationID+"'";
		ResultSet res_way = stm1.executeQuery(b);

		while(res_way.next()) {
			if(res_way.getString("member_type").equals("way")) {
				String wayID = res_way.getString("member_ref");
				int outcomeID = check(wayID);
				if(outcomeID == 0) {
				//	LOG.log(Level.WARNING, "no way matchs the input id, return null");
					i++;
				}else {
					j++;
				}
					
			}
		}
		res_way.close();
		stm1.close();
	}
		
	//check if the way which in relation also stored in the table way
	private int check(String wayID) throws SQLException {
			
		int temp = 0;
		String c = "SELECT ID FROM ways WHERE ID = '"+Integer.parseInt(wayID)+"'";
		ResultSet res_way_id = stm2.executeQuery(c);
		while(res_way_id.next()) {
			temp = res_way_id.getInt("ID");
		}
		
		res_way_id.close();
		stm2.close();
			
		return temp;
	}
	
	//get every node from database
	public List<Node> getNodes() {
		
		Node node;
		List<Node> nodes = new ArrayList<>();
		
		try {
			
			String search = "SELECT ID FROM nodes";
			ResultSet res = stm.executeQuery(search);
			
			while(res.next()) {
				node = new Node();
				node.id = res.getString("id");
				node.lat = Double.parseDouble(res.getString("lat"));
				node.lon = Double.parseDouble(res.getString("lon"));
				nodes.add(node);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return nodes;
	}
	
	public Way getOneWay(int id) {
		
		Way way = new Way();
		/*
		for(int i=0;i<ways.size();i++) {
			System.out.println(id + "=======" + String.valueOf(ways.get(i).id));
			if(String.valueOf(ways.get(i).id).equals(id)) {
				way = ways.get(i);
				
				return way;
			}
		}
		*/
		
		
		Connection con = null;
		Statement stm = null;
		
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:osm.db");
			stm = con.createStatement();

			String search = "SELECT * FROM ways WHERE ID = '"+id+"'";
			ResultSet res = stm.executeQuery(search);
			while(res.next()) {
				way.id = res.getInt("ID");
			}
			
			stm.close();
			con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			LOG.severe("ClassNotFoundException");
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOG.severe("SQLException");
			e.printStackTrace();
		} 
		System.out.println(way.id);
		if(way.id == 0) {
			LOG.log(Level.WARNING, "no way matchs the input id, return null");
			i++;
		}else {
			j++;
		}
	//	LOG.log(Level.WARNING, "no way matchs the input id, return null");
		return way;
	}
	
	public Node getOneNode(String id) {
		
		Node node;
		
		for(int i=0;i<nodes.size();i++) {
			if(String.valueOf(nodes.get(i).id).equals(id)) {
				node = nodes.get(i);
				
				return node;
			}
		}
		
		LOG.log(Level.WARNING, "no node matchs the input id, return null");
		return null;
	}
}
