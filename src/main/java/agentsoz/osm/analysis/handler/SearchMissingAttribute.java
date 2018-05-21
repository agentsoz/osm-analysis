package agentsoz.osm.analysis.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SearchMissingAttribute extends BasicProblemHandler{
	
	Statement stm;
	Statement stm1;
	Connection con;
	private String url;
	String input;
	String sql;
	
	//choices are 'ways' 'relations' and 'nodes'
	public static String choice;

	
	public SearchMissingAttribute(String databaseUrl)
	{
		url = "jdbc:sqlite:" + databaseUrl;
		input = "";
		sql = null;
	}

	public void search(String attName) 
	{	
		try 
		{	
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection(url);
			stm = con.createStatement();
			stm1 = con.createStatement();

	
			setSqlQuery(attName);
		
			List<String> IDs = compare();
			ResultSet res = stm.executeQuery(sql);
			String temp;

			while(res.next())
			{
				temp = res.getString(input);
				IDs.remove(temp);
			}
			
			String content = "";
			
			for(String id : IDs) 
			{
				String output = "missing_" + choice + "_" + attName + ": " + id;
				content = content + output + "\r\n";
				isFound = true;
			}
			
			writeResult(content);
			
			stm.close();
			con.close();
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		if(isFound == false) 
		{
			System.out.println("no match found");
		}
	}

	public void setSqlQuery(String attName) 
	{
		if(choice.equals("node")) 
		{
			input = "node_id";
			sql = "SELECT node_id FROM nodes_tags WHERE tag_key='"+attName+"'";
		}
		else if(choice.equals("way")) 
		{
			input = "way_id";
			sql = "SELECT way_id FROM ways_tags WHERE tag_key='"+attName+"'";
		}
		else if(choice.equals("relation"))
		{
			input = "relation_id";
			sql = "SELECT relation_id FROM relations_tags WHERE tag_key='"+attName+"'";
		}
		else 
		{
			System.out.println("incorrect input, please input 'node' or 'way' or 'relation'.");
			System.exit(0);
		}
	}
	
	private List<String> compare() throws SQLException 
	{
		String sql = "SELECT ID FROM '"+choice+"s'";
		ResultSet res = stm1.executeQuery(sql);
		List<String> IDs = new ArrayList<String>();
		
		while(res.next())
		{
			IDs.add(res.getString("ID"));
		}
		stm1.close();
		
		return IDs;
	}

	@Override
	public void handleProblem() {}
}
