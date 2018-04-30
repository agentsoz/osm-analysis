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
	
	int i=0;
	int j=0;
	
	public void prepare() {
		
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:osm.db");
			stm = con.createStatement();
			stm1 = con.createStatement();
			stm2 = con.createStatement();
			con.setAutoCommit(false);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			LOG.severe("ClassNotFoundException");
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOG.severe("SQLException");
			e.printStackTrace();
		}
	//	getWays();
		getRelations();
	}
	
	public void finish() throws SQLException {
		con.close();
		stm.close();
	}
	
	public List<Relation> getRelations() {
		
		String sql = "SELECT ID FROM relations";
		ResultSet res_relation;		

		Relation relation;

		String sql_tag;
		ResultSet res_tag;
		
		String sql_member;
		ResultSet res_member;
		try {
			res_relation = stm.executeQuery(sql);
			while(res_relation.next()) {
				relation = new Relation();
				relation.id = res_relation.getInt("ID");
				/*
				sql_tag = "SELECT * FROM relations_tags WHERE relation_id='"+res_relation.getInt("ID")+"'";
				res_tag = stm1.executeQuery(sql_tag);
				
				while(res_tag.next()) {
					//TODO add tag from database to list when need it
					if(res_tag.getString("tag_key").equals("maxspeed")) {
						relation.maxspeed = Integer.parseInt(res_tag.getString("tag_value"));
					}
					if(res_tag.getString("tag_key").equals("route")) {
						relation.route = res_tag.getString("tag_value");
					}
				}
				*/
				sql_member = "SELECT * FROM relations_members WHERE relation_id='"+relation.id+"'";
				res_member = stm2.executeQuery(sql_member);
				
				while(res_member.next()) {
					//read member from database
					if(res_member.getString("member_type").equals("way")) {
						Member way = getOneWay(res_member.getInt("member_ref"));
						if(way != null) {
							way.role = res_member.getString("member_role");
							relation.members.add(way);
						}
					}
				}
				relations.add(relation);
 			}
			System.out.println(relations.size());
			res_relation.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(i+"======="+j);
		return relations;
	}
	
	public List<Way> getWays() {
		
		int count_maxspeed = 0;

		try {
			String sql = "SELECT ID FROM ways";
			ResultSet res = stm.executeQuery(sql);
			ResultSet res_t;
			String tags;
			String nodes;
			Way way;
			String wayID;
			while(res.next()) {
				way = new Way();
				wayID = res.getString("ID");
				way.id = Integer.parseInt(wayID);
				tags = "SELECT * FROM ways_tags WHERE way_id='"+wayID+"'";
				res_t = stm1.executeQuery(tags);
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
				
				nodes = "SELECT * FROM ways_nodes WHERE way_id='"+wayID+"'";
				res_t = stm2.executeQuery(nodes);
				while(res_t.next()) {
					way.nodes_ref.add(res_t.getString("node_id"));
				}
				ways.add(way);
				
			}
			con.commit();
			res.close();
		//	stm.close();
		//	con.close();
		} catch (SQLException e) {
			LOG.log(Level.SEVERE, "sql problem");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			LOG.log(Level.INFO, "how many ways in this area: " + ways.size());
			LOG.log(Level.INFO, "how many ways have max speed: " + count_maxspeed);
		}
		System.out.println(ways.size());
		return ways;
	}
	
	public List<Node> getNodes() {
		
		Node node;
		List<Node> nodes = new ArrayList<>();
		
		try {
			
			String search = "SELECT * FROM nodes";
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
