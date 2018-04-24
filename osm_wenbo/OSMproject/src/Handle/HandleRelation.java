package Handle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HandleRelation {

	public static void main(String args[]) 
	{
		HandleRelation hr = new HandleRelation();
		hr.getWaysInRelation(1148);
		hr.getDigit("s3620b");
		hr.getWayById(4148483);
	}
	
	/**
	 * Connect to the osm.db database
	 * @return the connection object
	 * */
	private Connection connect() 
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
	
	/**
	 * @return a list of Relation which has tag "maxspeed" 
	 * */
	public List<Relation> getRelation() 
	{
		List<Relation> ls_relations = new ArrayList<>();
		String query = "SELECT DISTINCT* FROM relations_tags WHERE tag LIKE 'maxspeed%'";
		// "try-with-resources" statement, ensure resource is closed
		// at the end of the statement
		try(Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);)
		{
			while(rs.next() ) 
			{
				Relation relation = new Relation();
				relation.setId(rs.getInt("relation_id"));
				int speed = getDigit(rs.getString("tag"));
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
	public List<String> getWaysInRelation(int relation_id) 
	{
		List<String> ls_wayIds = new ArrayList<String>();
		
		String query = "SELECT  DISTINCT* FROM relations_members WHERE  member_type=\"way\" AND relation_id=\""
	                    + String.valueOf(relation_id) + "\"";
		
		try(Connection conn = this.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);)
		{
			 while(rs.next()) 
			 {
				 ls_wayIds.add(String.valueOf(rs.getInt("member_ref")));
			 }
		}catch(SQLException e) {
				System.out.println(e.getMessage());
		}
		for(String id : ls_wayIds) 
		{System.out.println(id);}
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
			}
		}
		System.out.println(Integer.parseInt(result));
		return Integer.parseInt(result);
	}
	
	/**
	 * checked in main
	 * return way object which match input id and has tag "maxspeed"
	 * */
	public Way getWayById(int wayId) 
	{
		String query = "SELECT DISTINCT* FROM ways_tags WHERE tag LIKE 'maxspeed%' AND way_id=\""
		               + String.valueOf(wayId) + "\"";
		 Way way = null;
		
		try(Connection conn = this.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);)
		{
			way = new Way();
			way.setId(rs.getInt("way_id"));
			int speed = getDigit(rs.getString("tag"));
			way.setSpeed(speed);
		}catch(SQLException e) {
				System.out.println(e.getMessage());
		}
		System.out.println(way.id + ", " + way.maxspeed);
		return way;
	}
} 
