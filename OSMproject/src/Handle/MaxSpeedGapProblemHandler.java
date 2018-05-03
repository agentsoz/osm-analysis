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

import Model.Way;

public class MaxSpeedGapProblemHandler extends BasicProblemHandler{
	
	static final Logger LOG = Logger.getLogger(MaxSpeedGapProblemHandler.class.getName());
	
	private int count_speed = 0;
	
	List<Way> ways;
	
	int gap;
	
	Connection con = null;
	Statement stm = null;
	Statement stm1 = null;
	Statement stm_j = null;

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
	@Override
	public void handleProblem() {
		// TODO Auto-generated method stub
		gap = 40;
		
		long begin = 0;
		long end = 0;
		
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:osm.db");
			stm = con.createStatement();
			stm1 = con.createStatement();
			
			begin = System.currentTimeMillis();
			getWays();
			end = System.currentTimeMillis();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			LOG.severe("ClassNotFoundException");
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOG.severe("SQLException");
			e.printStackTrace();
		}finally {
			LOG.log(Level.INFO, "the running time is " + (end - begin) + "ms");
			LOG.log(Level.INFO, " speed gap between two ways which is more than '"+gap+"': " + count_speed);
		}
	}
	
	private void getWays() throws SQLException {
		Way way = new Way();
		List<Way> ways = new ArrayList<>();
		String sql = "SELECT way_id,tag_value FROM ways_tags WHERE tag_key='maxspeed'";
		ResultSet res = stm.executeQuery(sql);
		while(res.next()) {
			way = new Way();
			way.id = Integer.parseInt(res.getString("way_id"));
			way.maxspeed = Integer.parseInt(res.getString("tag_value"));
			getNodes(way, res.getString("way_id"));
			ways.add(way);
		}
		LOG.log(Level.INFO, "how many ways have 'maxspeed' attribute: " + ways.size());
		
		stm.close();
		compare(ways);
		con.close();
	}
	
	private void getNodes(Way way, String id) throws SQLException {
		String sql = "SELECT node_id FROM ways_nodes where way_id='"+id+"'";
		ResultSet res = stm1.executeQuery(sql);
		while(res.next()) {
			way.nodes_ref.add(res.getString("node_id"));
		}
		stm1.close();
	}
	
	private void compare(List<Way> ways) throws SQLException {
		
		String way_i_f;
		String way_i_l;
		String way_j_f;
		String way_j_l;
		int count = 0;
		int percent = 0;
		for(int i=0;i<ways.size();i++) {
			count++;
			if(count >= ways.size()/10) {
				percent = percent + 10;
				LOG.log(Level.INFO, percent + "% data has been finished");
				count = 0;
			}
			int i_size = ways.get(i).nodes_ref.size();
			way_i_f = ways.get(i).nodes_ref.get(0);
			way_i_l = ways.get(i).nodes_ref.get(i_size-1);
			for(int j=i+1;j<ways.size()-1;j++) {
				int j_size = ways.get(j).nodes_ref.size();
				way_j_f = ways.get(j).nodes_ref.get(0);
				way_j_l = ways.get(j).nodes_ref.get(j_size-1);
				
				if(way_i_f.equals(way_j_f) ||
						way_i_f.equals(way_j_l) || 
							way_i_l.equals(way_j_l) ||
								way_i_l.equals(way_j_f)) {
					int actual_gap = Math.abs(ways.get(i).maxspeed - ways.get(j).maxspeed);
					if(actual_gap > gap) {
						count_speed++;
						LOG.log(Level.INFO, "way(" + ways.get(i).id + ") and way(" + ways.get(j).id + ") have max speed problem");
					}
				}
			}
		}
	}
	
	
	
	/*
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
	}
	*/

}
