package Handle;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import model.Way;

public class Main_Handler {
	
	static final Logger LOG = Logger.getLogger(Main_Handler.class.getName());
	
	static List<Way> ways = new ArrayList<>();
	
	public static void main(String args[]) {
		
//		ways = getWays();
		MaxSpeedGapProblem.inCorrectMaxSpeed(ways);
		
		
	}
	
	public static Connection connect() 
	{
		Connection conn = null;
		String url = "jdbc:sqlite:osm.db";
		
		try {
			conn = DriverManager.getConnection(url);
		}catch(SQLException e){
			System.out.println(e.getMessage());
		}
		
		return conn;
	}
	
	public List<Way> getWays() 
	{
		int count_maxspeed = 0;
		ways = new ArrayList<>();
		
		Connection con;
		Statement stm;
		
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:osm.db");
			stm = con.createStatement();
			con.setAutoCommit(false);
			
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
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			LOG.log(Level.SEVERE, "class not found");
			e1.printStackTrace();
		} catch (SQLException e1) {
			LOG.log(Level.SEVERE, "sql problem");
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally {
			LOG.log(Level.INFO, "how many ways in this area: " + ways.size());
			LOG.log(Level.INFO, "how many ways have max speed: " + count_maxspeed);
		}
	}
	
	
}
