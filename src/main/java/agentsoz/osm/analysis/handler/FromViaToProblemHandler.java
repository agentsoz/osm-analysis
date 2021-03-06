package agentsoz.osm.analysis.handler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/*
 * for example: in relation,
 *  <member type="way" ref="431199097" role="from"/> (way1)
 *  <member type="node" ref="1871199633" role="via"/> (node1)
 *  <member type="way" ref="431199095" role="to"/>	(way2)
 *  thus, the last node of way1 should equal to the node1
 *  	  the first node of way2 should equal to the node1
 * */ 
public class FromViaToProblemHandler extends BasicProblemHandler {
	
	int countIssue = 0;
	
	Statement stm;
	Statement stm1;
	Statement stm2;
	Statement stm3;
	Statement stm4;
	Statement stm5;
	Connection con;
	
	List<String> wayIDs;
	private String url;
	
	public FromViaToProblemHandler(String databaseUrl) 
	{
		url = "jdbc:sqlite:" + databaseUrl;
	}
	
	void create() throws SQLException
	{
		stm = con.createStatement();
		stm1 = con.createStatement();
		stm2 = con.createStatement();
		stm3 = con.createStatement();
		stm4 = con.createStatement();
		stm5 = con.createStatement();
	}
	
	void close() throws SQLException 
	{
		stm.close();
		stm1.close();
		stm2.close();
		stm3.close();
		stm4.close();
		con.close();
	}
	
	@Override
	public void handleProblem() 
	{	
		wayIDs = new ArrayList<>();
		
		try 
		{
			Class.forName("org.sqlite.JDBC");
			con = super.connect(url);
	
			getRelations();	
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

	/**
	 * wayID : list contains all way id
	 * check from, via, to by calling getMembers(relation_id)
	 * */
	private void getRelations() throws SQLException 
	{
		String sql_wayID = "SELECT ID FROM ways";
		ResultSet res_wayID = stm5.executeQuery(sql_wayID);
		
		while(res_wayID.next()) 
		{
			wayIDs.add(res_wayID.getString("ID"));
		}
		stm5.close();
		
		String sql = "SELECT ID FROM relations";
		ResultSet res = stm1.executeQuery(sql);

		while(res.next()) 
		{	
			getMembers(res.getInt("ID"));
		}
	}

	private void getMembers(int relationID) throws SQLException 
	{
		String sql = "SELECT member_type, member_ref,member_role FROM relations_members WHERE relation_id='"+relationID+"'";
		ResultSet res = stm2.executeQuery(sql);
		
		String[] nodes = new String[3];
		
		int test = 0;
		int check = 0;
		
		boolean ifVIAway = false;
		
		// check from via to with sequence
		while(res.next())
		{
			test++;
			if(res.getString("member_role").equals("from"))
			{
				nodes[0] = res.getString("member_ref");
				check = test;
			}
			else if(res.getString("member_role").equals("via") && (check == test + 1))
			{
				if(res.getString("member_type").equals("way")) 
				{
					ifVIAway = true;
				}
				nodes[1] = res.getString("member_ref");
			}
			else if(res.getString("member_role").equals("to") && (check == test + 2))
			{
				nodes[2] = res.getString("member_ref");
			}
		}
		
		if(wayIDs.contains(nodes[0]) && wayIDs.contains(nodes[2]))
		{
			check(nodes, ifVIAway);
		}
	}

	private void check(String[] nodes, boolean ifVIAway) throws SQLException
	{
		// if all 3 nodes not null
		if(!(nodes[0].equals("") || nodes[1].equals("") || nodes[2].equals(""))) 
		{
			String sql_from = "SELECT node_id FROM ways_nodes WHERE way_id='"+nodes[0]+"'";
			ResultSet res_from = stm3.executeQuery(sql_from);
			ArrayList<String> nodes_first = new ArrayList<>();
			
			while(res_from.next()) 
			{
				nodes_first.add(res_from.getString("node_id"));
			}
			String sql_to = "SELECT node_id FROM ways_nodes WHERE way_id='"+nodes[2]+"'";
			ResultSet res_to = stm4.executeQuery(sql_to);
			ArrayList<String> nodes_last = new ArrayList<>();
			
			while(res_to.next())
			{
				nodes_last.add(res_to.getString("node_id"));
			}
			
			ArrayList<String> nodes_via = new ArrayList<>();
			
			if(ifVIAway) 
			{
				String sql_via = "SELECT node_id FROM ways_nodes WHERE way_id = '"+nodes[1]+"'";
				ResultSet res_via = stm.executeQuery(sql_via);
				
				while(res_via.next()) 
				{
					nodes_via.add(res_via.getString("node_id"));
				}
			}
			
			boolean ifcount = true;
			
			for(int i=0;i<nodes_first.size();i++) 
			{
				for(int j=0;j<nodes_last.size();j++) 
				{
					if(nodes_first.get(i).equals(nodes_last.get(j))) 
					{
						if(ifVIAway) 
						{
							for(int k=0;k<nodes_via.size();k++) 
							{
								if(nodes_via.get(k).equals(nodes_first.get(i))) 
								{
									ifcount = false;
									break;
								}
							}
						}
						else 
						{
							if(nodes_first.get(i).equals(nodes[1])) 
							{
								ifcount = false;
								break;
							}
						}
					}
				}
			}
			
			if(!ifcount)
			{
				countIssue++;
			}
		}
	}
}
