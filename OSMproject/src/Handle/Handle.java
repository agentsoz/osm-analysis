package Handle;

import java.sql.Connection;
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

public class Handle {
	
	static final Logger LOG = Logger.getLogger(Handle.class.getName());
	
	private static Handle instance;
	
	public static Handle getInstance() {
		
		if(instance == null) {
			instance = new Handle();
		}
		
		return instance;
	}
	//not finish yet
	public List<Relation> getRelations(Connection con, Statement stm) {
		
		List<Relation> relations = new ArrayList<>();
		
		String sql = "SELECT ID FROM relations";
		ResultSet res;
		Relation relation;
		ResultSet res_t;
		String sql_tag;
		String sql_member;
		try {
			res = stm.executeQuery(sql);
			while(res.next()) {
				relation = new Relation();
				relation.id = Integer.parseInt(res.getString("ID"));
				sql_tag = "SELECT * FROM relations_tags WHERE relation_id='"+res.getString("ID")+"'";
				res_t = stm.executeQuery(sql_tag);
				while(res_t.next()) {
					//read tag from database
				}
				sql_member = "SELECT * FROM relations_members WHERE relation_id='"+res.getString("ID")+"'";
				res_t = stm.executeQuery(sql_member);
				while(res_t.next()) {
					//read member from database
					if(res_t.getString("member_type").equals("way")) {
						Member way = getOneWay(res_t.getString("member_ref"));
						way.role = res_t.getString("member_role");
					}
					
					if(res_t.getString("member_type").equals("ndoe")) {
						Member node = getOneNode(res_t.getString("member_ref"));
						node.role = res_t.getString("member_role");
					}
				
				}
				
 			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return relations;
	}
	
	public List<Way> getWays(Connection con, Statement stm) {
		
		int count_maxspeed = 0;
		List<Way> ways = new ArrayList<>();

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
				res_t = stm.executeQuery(tags);
				while(res_t.next()) {
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
				}
				
				nodes = "SELECT * FROM ways_nodes WHERE way_id='"+wayID+"'";
				res_t = stm.executeQuery(nodes);
				while(res_t.next()) {
					way.nodes_ref.add(res_t.getString("node_id"));
				}
				ways.add(way);
				
			}
			res.close();
			stm.close();
			con.close();
		} catch (SQLException e) {
			LOG.log(Level.SEVERE, "sql problem");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			LOG.log(Level.INFO, "how many ways in this area: " + ways.size());
			LOG.log(Level.INFO, "how many ways have max speed: " + count_maxspeed);
		}
		
		return ways;
	}
	
	public Way getOneWay(String id) {
		
	}
	
	public Node getOneNode(String id) {}
}
