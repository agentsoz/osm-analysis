package agentsoz.osm.analysis.handler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import agentsoz.osm.analysis.models.Way;

public class getWaysHandler extends BasicProblemHandler{
	
	public String url;
	Statement stmt;
    public List<Way> ways;
	int count_maxspeed;
	Connection conn;
	
	public getWaysHandler(String databaseUrl)
	{
		url = "jdbc:sqlite:" + databaseUrl;
	}
	
	
	public static void main(String args[]) 
	{
		 getWaysHandler gwh = new  getWaysHandler("osm.db");
		 gwh.getWaysFromTableWays();
//		 gwh.getWayHasSpeed();
//		 for(Way way:gwh.ways) {
//			 System.out.println(way.getId() + ": " + way.getSpeed());
//		 }
//		 System.out.println(gwh.ways.get(0).getNodesRef().get(0));
//		MaxSpeedGapProblemHandler msp = new MaxSpeedGapProblemHandler();
//		msp.inCorrectMaxSpeed(gwh.ways, 60);
	}
	
	// get all the way
	public List<Way> getWaysFromTableWays() 
	{
		ways = new ArrayList<>();
		count_maxspeed = 0;
		conn = super.connect(url);
		String query = "SELECT ID FROM ways";
		
		try(Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);)
		{
			while(rs.next() ) 
			{
				Way way = new Way(); 
			    way.setId(rs.getString("ID"));
			    
			    getNodesForWay(way); 
			    getTagsForWay(way);
			    
			    ways.add(way);
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("how many ways in this area: " + ways.size());
			System.out.println("how many ways have max speed: " + count_maxspeed);	
		}
		return ways;
	}
		
	public void getNodesForWay(Way way)
	{
		String query_nodes = "SELECT * FROM ways_nodes WHERE way_id='"+ way.getId() +"'";
		
		try(Statement stmt = conn.createStatement();
			ResultSet rs_nodes = stmt.executeQuery(query_nodes);)
		{
			while(rs_nodes.next())
			{
				way.addNodesRef(rs_nodes.getString("node_id"));    
			}
		}
		catch(SQLException e) 
		{
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Values of "tag_key" have multiple maxspeed related attribute:   
	 * */
	public void getTagsForWay(Way way)
	{
		String query_tags = "SELECT * FROM ways_tags WHERE way_id='"+ way.getId() + "'";
		
		try(Statement stmt = conn.createStatement();
			ResultSet rs_tags = stmt.executeQuery(query_tags);)
		{
			while(rs_tags.next()) 
		    {
		    	if(rs_tags.getString("tag_key").equals("maxspeed")) 
		    	{
		    		way.setSpeed(Integer.parseInt(rs_tags.getString("tag_value")));
		    		count_maxspeed++;
		    	}
		    	if(rs_tags.getString("tag_key").equals("name")) 
		    	{
		    		way.setName(rs_tags.getString("tag_value"));
		    	}
		    	if(rs_tags.getString("tag_key").equals("oneway"))
		    	{
		    		way.oneWay = rs_tags.getString("tag_value");
		    	}
		    	if(rs_tags.getString("tag_key").equals("lanes"))
		    	{
		    		way.lanes = rs_tags.getString("tag_value");
		    	}
		    }    
		}
		catch(SQLException e) 
		{
			System.out.println(e.getMessage());
		}
	}

	// checked in sql, main
	public void getWayHasSpeed() 
	{
		int count_maxspeed = 0;
		ways = new ArrayList<>();
		conn = this.connect(url);
		String query = "SELECT * FROM ways_tags WHERE tag_key LIKE 'maxspeed%'";
		try(Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);)
		{
			while(rs.next() ) 
			{
				Way way = new Way(); 
			    way.setId(rs.getString("way_id"));
			    way.setSpeed(rs.getInt("tag_value"));
			    count_maxspeed++;
			    ways.add(way);
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("how many ways in this area: " + ways.size());
		System.out.println("how many ways have max speed: " + count_maxspeed);
	}


	@Override
	public void handleProblem() {
		// TODO Auto-generated method stub
		
	}
}