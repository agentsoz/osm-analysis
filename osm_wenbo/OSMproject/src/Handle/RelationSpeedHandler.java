package Handle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Relation;
import model.Way;

public class RelationSpeedHandler {

	public String url;
	
	public static void main(String[] args) 
	{
		RelationSpeedHandler rs = new RelationSpeedHandler("osm.db");
		rs.handleProblem();
	}
	
	public RelationSpeedHandler(String databaseUrl)
	{
		url = "jdbc:sqlite:" + databaseUrl;
	}

	public void handleProblem() 
	{
		List<Relation> ls_relation = getRelation();
	
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
						System.out.println("Mark relation between way " + way.getId() 
		                        + " and Relation " + relation.getId() + " as a potantial problem");
		                System.out.println("way maxspeed is " + way.getSpeed() + ", "
		                		+ " and relation maxspeed is " + relation.getSpeed());
					}
				}
			}
		}
	}
	
	/**
	 * Connect to the osm.db database
	 * @return the connection object
	 * */
	private Connection connect() 
	{
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(url);
		}catch(SQLException e){
			System.out.println(e.getMessage());
		}	
		return conn;
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
		try(Connection conn = this.connect();
			Statement stmt = conn.createStatement();
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
		}catch(SQLException e) {
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
		
		try(Connection conn = this.connect();
				Statement stmt2 = conn.createStatement();
				ResultSet rs2 = stmt2.executeQuery(query);)
		{
			 while(rs2.next()) 
			 {
				 ls_wayIds.add(rs2.getString("member_ref"));
			 }
		}catch(SQLException e) {
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
		
		try(Connection conn = this.connect();
				Statement stmt3 = conn.createStatement();
				ResultSet rs3 = stmt3.executeQuery(query);)
		{
			while(rs3.next()) 
			{
				int speed = getDigit(rs3.getString("tag_value"));
				
				if(rs3.getString("way_id").equals(wayId) && speed > 20)
				{
					way = new Way();
					way.setId(rs3.getString("way_id"));
					way.setSpeed(speed);
				    break;
				}
			}
		}catch(SQLException e) {
				System.out.println(e.getMessage());
		}
		return way;
	}
} 
