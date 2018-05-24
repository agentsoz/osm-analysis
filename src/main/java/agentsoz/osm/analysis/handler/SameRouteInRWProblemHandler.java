package agentsoz.osm.analysis.handler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * in the relation, there is a attribute called tag_key = route, tag_value = bicycle
 * in the way, there is a attribute called bicycle = yes/no
 * thus, assume there is a way in the relation, 
 * if the relation has the attribute above, while in the way bicycle = no
 * this could be incorrect
 * */
public class SameRouteInRWProblemHandler extends BasicProblemHandler {
	
	private int count = 0;
	
	static final Logger LOG = Logger.getLogger(SameRouteInRWProblemHandler.class.getName());
	
	private List<String> issuedways;
	
	private String url;
	Connection con;
	Statement stm;
	Statement stm1;
	Statement stm2;
	
	public SameRouteInRWProblemHandler(String databaseUrl)
	{
		url = "jdbc:sqlite:" + databaseUrl;
	}
	
	@Override
	public void handleProblem() {

		issuedways = new ArrayList<>();
		
		long begin = 0;
		long end  = 0;
		
		try 
		{
			Class.forName("org.sqlite.JDBC");
			con = super.connect(url);
			stm = con.createStatement();
			stm1 = con.createStatement();
			stm2 = con.createStatement();
			begin = System.currentTimeMillis();
			getRouteInR();
			end = System.currentTimeMillis();
		}
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		LOG.log(Level.INFO, "the running time of finding this problem: " + (end - begin) + "ms");
		LOG.log(Level.INFO, "the number that relation and its ways have different route: " + count);
	}
	
	private void getRouteInR() throws SQLException 
	{
		String sql = "SELECT relation_id,tag_value FROM relations_tags WHERE tag_key='route'";
		ResultSet res = stm.executeQuery(sql);
		
		while(res.next()) 
		{
			if(res.getString("tag_value").equals("bicycle")) 
			{
				getMemberInR(res.getString("relation_id"));
			}
		}
		res.close();
		stm.close();
	}
	
	private void getMemberInR(String relationID) throws SQLException 
	{
		String sql = "SELECT member_ref,member_type FROM relations_members WHERE relation_id='"+relationID+"'";
		ResultSet res = stm1.executeQuery(sql);
		
		while(res.next()) 
		{
			if(res.getString("member_type").equals("way")) 
			{
				getTagInWay(res.getString("member_ref"));
			}
		}
		res.close();
		stm1.close();
	}
	
	private void getTagInWay(String wayID) throws SQLException 
	{
		String sql = "SELECT tag_value,tag_key FROM ways_tags WHERE way_id='"+wayID+"'";
		ResultSet res = stm2.executeQuery(sql);
		
		while(res.next()) 
		{
			if(res.getString("tag_key").equals("bicycle"))
			{
				if(res.getString("tag_value").equals("no"))
				{
					count++;
					issuedways.add(wayID);
				}
			}
		}
		res.close();
		stm2.close();
	}
}