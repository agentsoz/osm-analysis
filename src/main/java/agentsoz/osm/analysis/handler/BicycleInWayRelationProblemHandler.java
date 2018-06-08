package agentsoz.osm.analysis.handler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


/*
 * in the relation, there is a attribute 'tag_key = route, tag_value = bicycle'
 * all the ways in that relation should have a attribute 'bicycle = yes/no'
 * thus, assume there is a way in the relation, 
 * if the relation has the attribute above, while its way has attribute 'bicycle = no' or don't have attribute 'bicycle' 
 * that could be incorrect
 * */
public class BicycleInWayRelationProblemHandler extends BasicProblemHandler {
	
	private String url;
	Connection con;
	Statement stm;
	Statement stm1;
	Statement stm2;
	String content;
	String content_missing;
	
	public BicycleInWayRelationProblemHandler(String databaseUrl)
	{
		url = "jdbc:sqlite:" + databaseUrl;
		content = "";
		content_missing = "";
	}
	
	@Override
	public void handleProblem() 
	{
		try 
		{
			Class.forName("org.sqlite.JDBC");
			con = super.connect(url);
			stm = con.createStatement();
			stm1 = con.createStatement();
			stm2 = con.createStatement();

			getRouteInRelation();
			
			String content_all = content + content_missing;
			if(content_all == "") 
			{
				System.out.println("No match found");
			}
			else 
			{
				writeResult(content_all);	
			}
		}
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	private void getRouteInRelation() throws SQLException 
	{
		String sql = "SELECT relation_id,tag_value FROM relations_tags WHERE tag_key='route'";
		ResultSet res = stm.executeQuery(sql);
		
		while(res.next()) 
		{
			if(res.getString("tag_value").equals("bicycle")) 
			{
				getMemberInRelation(res.getString("relation_id"));
			}
		}
		res.close();
		stm.close();
	}
	
	// get all member way given relation id
	private void getMemberInRelation(String relationID) throws SQLException 
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
	
	//get way tags given way id
	private void getTagInWay(String wayID) throws SQLException 
	{
		String sql = "SELECT tag_value,tag_key FROM ways_tags WHERE way_id='"+wayID+"'";
		ResultSet res = stm2.executeQuery(sql);
		boolean hasBicycle = false;
		
		while(res.next()) 
		{
			if(res.getString("tag_key").equals("bicycle"))
			{
				hasBicycle = true;
				if(res.getString("tag_value").equals("no"))
				{
					String output =  "mismatch_bicycle_way_relation: " + "way_" + wayID + 
							", tag_key: bicycle, tag_value: no";
					content = content + output + "\r\n";
				}
			}
		}
		
		if(hasBicycle)     
		{
			String output_missing = "mismatch_bicycle_way_relation: " + "way_" + wayID + 
					       ", missing tag_key bicycle";
			content_missing = content_missing + output_missing + "\r\n";
		}
		res.close();
		stm2.close();
	}
}