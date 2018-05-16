package agentsoz.osm.analysis.handler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import agentsoz.osm.analysis.models.Relation;
import agentsoz.osm.analysis.models.Way;


public class RelationSpeedProblemHandler extends BasicProblemHandler{

	public String url;
	Connection conn;
	Timer timer;
		
	public RelationSpeedProblemHandler(String databaseUrl)
	{
		url = "jdbc:sqlite:" + databaseUrl;
	}

	@Override
	public void handleProblem()
	{
		conn = super.connect(url);
		List<Relation> ls_relation = getRelation();
	
		String content = "";
		
		for(Relation relation : ls_relation) 
		{
			List<String> wayIds = getWayIdsInRelation(relation.getId());
			
			for(String wayId : wayIds) 
			{
				Way way = getWayHasSpeedById(wayId);
			
				if(way != null) 
				{
					if(relation.getSpeed() < way.getSpeed()) 	
					{
						String output = "way_relation_speed_diff: way_" + way.getId() + "=" + way.getSpeed() 
						            + " relation_" +  relation.getId() + "=" + relation.getSpeed();
					
						content = content + output + "\r\n";
					}
				}
			}
		}
		try 
		{
			conn.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		writeToFile(content);
	}
	

	/**
	 * @return a list of Relation which has tag "maxspeed" 
	 * */
	public List<Relation> getRelation() 
	{
		List<Relation> ls_relations = new ArrayList<>();
		String query = "SELECT DISTINCT* FROM relations_tags WHERE tag_key LIKE 'maxspeed%'";
		// "try-with-resources" statement, ensure resource is closed
		// at the end of the statement
		try(Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);)
		{
			while(rs.next() ) 
			{
				Relation relation = new Relation();
				relation.setId(rs.getString("relation_id"));
				int speed = getDigit(rs.getString("tag_value"));
				relation.setSpeed(speed);
				ls_relations.add(relation); 
			}
		}
		catch(SQLException e) 
		{
			System.out.println(e.getMessage());
		}
		return ls_relations;
	}
	
	/**
	 * checked in main
	 * run query in database first and compare result with the one produced by this method 
	 * find all the ways in one relation object
	 * */
	public List<String> getWayIdsInRelation(String relation_id) 
	{
		List<String> ls_wayIds = new ArrayList<String>();
		
		String query = "SELECT  DISTINCT* FROM relations_members WHERE  member_type=\"way\" AND relation_id=\""
	                    + relation_id + "\"";
		
		try(Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);)
		{
			 while(rs.next()) 
			 {
				 ls_wayIds.add(rs.getString("member_ref"));
			 }
		}
		catch(SQLException e) 
		{
			System.out.println(e.getMessage());
		}
		return ls_wayIds;
	}
	
	/**
	 * checked in main
	 * extract int from String, ready for setting 
	 * */
	public int getDigit(String input)
	{
		String result = "";
		for(int i = 0; i < input.length(); i++) 
		{
			Character chars = input.charAt(i);
			if(Character.isDigit(chars))
			{
				result += chars;
				if(Integer.parseInt(result) > 130)
					break;
			}
		}
		return Integer.parseInt(result);
	}
	
	/**
	 * checked in main
	 * return way object which match input id and has tag "maxspeed"
	 * */
	public Way getWayHasSpeedById(String wayId) 
	{
		String query = "SELECT DISTINCT* FROM ways_tags WHERE tag_key LIKE 'maxspeed%' AND way_id=\""
		               + wayId + "\"";
		 Way way = null;
		
		try(Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);)
		{
			while(rs.next()) 
			{
				int speed = getDigit(rs.getString("tag_value"));
				
				if(rs.getString("way_id").equals(wayId) && speed > 20)
				{
					way = new Way();
					way.setId(rs.getString("way_id"));
					way.setSpeed(speed);
				    break;
				}
			}
		}
		catch(SQLException e) 
		{
				System.out.println(e.getMessage());
		}
		return way;
	}
} 
