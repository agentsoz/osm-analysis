package handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SameRouteInRWProblemHandler extends BasicProblemHandler {
	
	private int count = 0;
	
	static final Logger LOG = Logger.getLogger(SameRouteInRWProblemHandler.class.getName());
	
	private List<Integer> issuedways;
	
	Connection con;
	Statement stm;
	Statement stm1;
	Statement stm2;
	
	@Override
	public void handleProblem() {

		issuedways = new ArrayList<>();
		
		long begin = 0;
		long end  = 0;
		
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:osm.db");
			stm = con.createStatement();
			stm1 = con.createStatement();
			stm2 = con.createStatement();
			begin = System.currentTimeMillis();
			getRouteInR();
			end = System.currentTimeMillis();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LOG.log(Level.INFO, "the running time of finding this problem: " + (end - begin) + "ms");
		LOG.log(Level.INFO, "the number that relation and its ways have different route: " + count);
	}
	
	private void getRouteInR() throws SQLException {
		String sql = "SELECT relation_id,tag_value FROM relations_tags WHERE tag_key='route'";
		ResultSet res = stm.executeQuery(sql);
		while(res.next()) {
			if(res.getString("tag_value").equals("bicycle")) {
				getMemberInR(res.getInt("relation_id"));
			}
		}
		res.close();
		stm.close();
	}
	
	private void getMemberInR(int relationID) throws SQLException {
		String sql = "SELECT member_ref,member_type FROM relations_members WHERE relation_id='"+relationID+"'";
		ResultSet res = stm1.executeQuery(sql);
		while(res.next()) {
			if(res.getString("member_type").equals("way")) {
				getTagInWay(Integer.parseInt(res.getString("member_ref")));
			}
		}
		res.close();
		stm1.close();
	}
	
	private void getTagInWay(int wayID) throws SQLException {
		String sql = "SELECT tag_value,tag_key FROM ways_tags WHERE way_id='"+wayID+"'";
		ResultSet res = stm2.executeQuery(sql);
		while(res.next()) {
			if(res.getString("tag_key").equals("bicycle")) {
				if(res.getString("tag_value").equals("no")) {
					count++;
					issuedways.add(wayID);
				}
			}
		}
		res.close();
		stm2.close();
	}
}