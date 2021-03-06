package agentsoz.osm.analysis.handler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import agentsoz.osm.analysis.models.Way;

/*
 * check if there are any incorrect max speed problem between two connected ways which have same street name
 * firstly, compare two ways by two "for" loop to get every two connected way, way 'i' and way 'j', for example
 * in addition, if two ways are connected, there are four situation
 * 1. i.beginNode == j.beginNode
 * 2. i.beginNode == j.endNode
 * 3. i.endNode == j.beginNode
 * 4. i.endNode == j.endNode
 * secondly, check speed_gap = i.maxspeed - j.maxspeed
 * */

public class MaxSpeedGapProblemHandler extends BasicProblemHandler{
	
	int speed_gap;
	private String url;
	Connection con;
	Statement stm; 
	Statement stm1;
	
	public MaxSpeedGapProblemHandler(String databaseUrl, int speed)
	{
		this.speed_gap =speed;
		url = "jdbc:sqlite:" + databaseUrl;
	}
	
	@Override
	public void handleProblem()
	{		
		super.running_timer();
		
		try 
		{
			Class.forName("org.sqlite.JDBC");
			con = super.connect(url);
			stm = con.createStatement();
			stm1 = con.createStatement();
			
			List<Way> ways = getAllWaysHasMaxSpeed();
			compare(ways);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		} 
		finally 
		{
			if(isFound == false) 
			{
				System.out.println("no match found");
			}

			super.stop_timer();
			
			try 
			{
				con.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private List<Way> getAllWaysHasMaxSpeed() throws SQLException 
	{
		Way way = new Way();
		List<Way> ways = new ArrayList<>();
		String sql = "SELECT way_id,tag_value FROM ways_tags WHERE tag_key='maxspeed'";
		
		ResultSet res = stm.executeQuery(sql);
		while(res.next()) 
		{
			way = new Way();
			way.setId(res.getString("way_id"));
			way.setSpeed(res.getInt("tag_value"));
			getNodes(way, res.getString("way_id"));
			ways.add(way);
		}
		
		stm.close();
		
		return ways;
	}
	
	private void getNodes(Way way, String id) throws SQLException
	{
		String sql = "SELECT node_id FROM ways_nodes where way_id='"+id+"'";
		ResultSet res = stm1.executeQuery(sql);
		
		while(res.next())
		{
			way.addNodesRef(res.getString("node_id"));
		}
		
		stm1.close();
	}
	
	private void compare(List<Way> ways) throws SQLException
	{	
		String way_i_f;	//the first node of way "i"
		String way_i_l; //the last node of way "i"
		String way_j_f; //the first node of way "j"
		String way_j_l; //the last node of way "j"

		String content = ""; //output
		//compare every two ways which actual_gap > speed_gap
		for(int i=0;i<ways.size();i++) 
		{

			int i_size = ways.get(i).getNodesRef().size();
			way_i_f = ways.get(i).getNodesRef().get(0);
 			way_i_l = ways.get(i).getNodesRef().get(i_size-1);
			
			for(int j=i+1;j<ways.size()-1;j++)
			{
				int actual_gap = Math.abs(ways.get(i).getSpeed() - ways.get(j).getSpeed());
				
				//if the actual_gap > speed_gap, no need to compare the node, just continue
				if(actual_gap > speed_gap) {
					
					int j_size = ways.get(j).getNodesRef().size();
					way_j_f = ways.get(j).getNodesRef().get(0);
					way_j_l = ways.get(j).getNodesRef().get(j_size-1);
					
					//if the two ways are connected but don't intersect
					if(way_i_f.equals(way_j_f) ||
							way_i_f.equals(way_j_l) || 
								way_i_l.equals(way_j_l) ||
									way_i_l.equals(way_j_f))
					{
						isFound = true;
						// way_maxspeed: id1=speed1 id2=speed2 diff=speed3
						String output = "way_maxspeed_diff: " + "way_" + ways.get(i).getId() + "=" + ways.get(i).getSpeed()
						         + " " + "way_" + ways.get(j).getId() + "=" + ways.get(j).getSpeed() + " diff=" + actual_gap;	
						content = content + output + "\r\n";
						continue;
					}
					
					//if two ways are intersect
					String node_k; //node from way i
					String node_l; //node from way j
					
					//compare every two nodes from two ways to judge if they intersect
					for(int k=0;k<ways.get(i).getNodesRef().size();k++) {
						node_k = ways.get(i).getNodesRef().get(k);
						for(int l=k+1;l<ways.get(j).getNodesRef().size()-1;l++) {
							node_l = ways.get(j).getNodesRef().get(l);
							if(node_k.equals(node_l)) {
								isFound = true;
								// way_maxspeed: id1=speed1 id2=speed2 diff=speed3
								String output = "way_maxspeed_diff: " + "way_" + ways.get(i).getId() + "=" + ways.get(i).getSpeed()
								         + " " + "way_" + ways.get(j).getId() + "=" + ways.get(j).getSpeed() + " diff=" + actual_gap;	
								content = content + output + "\r\n";
							}
						}
					}
					
				} else {
					continue;
				}

			}
		}
		if(content == "") 
		{
			System.out.println("No match found");
		}
		else 
		{
			writeResult(content);	
		}
	}
}
