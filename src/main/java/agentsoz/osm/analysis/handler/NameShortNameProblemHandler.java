package agentsoz.osm.analysis.handler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * some relations have attribute called short_name
 * and the precondition of short_name is name
 * thus, if a relation does not have name, but has short_name
 * it could be incorrect(OSM does not has input checking for this)
 * */
public class NameShortNameProblemHandler extends PrerequisitesProblemHandler {
	
	Statement stm;
	Statement stm1;
	Statement stm2;
	Connection con;
	private String url;
	String content;

	public NameShortNameProblemHandler(String databaseUrl)
	{
		url = "jdbc:sqlite:" + databaseUrl;
		content = "";
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
			
			getIdFromRelations();
			
			stm.close();
			stm1.close();
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally {
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

	private void getIdFromRelations() throws SQLException
	{	
		String sql = "SELECT ID FROM relations";
		ResultSet res = stm.executeQuery(sql);

		while(res.next()) 
		{
			// check each relation
			checkNameInTags(res.getInt("ID"));
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

	
	private void checkNameInTags(int relationID) throws SQLException 
	{
		String sql = "SELECT tag_key,tag_value FROM relations_tags WHERE relation_id='"+relationID+"'";
		ResultSet res = stm1.executeQuery(sql);
		
		String name = "";
		String short_name = "";
		
		while(res.next()) 
		{	
			if(res.getString("tag_key").equals("name"))
			{
				name = res.getString("tag_value");
			}
			if(res.getString("tag_key").equals("short_name")) 
			{
				short_name = res.getString("tag_value");
			}
		}
		// if a relation has attribute name but does not have attribute short name
		// mark it as potential problem
		if(!(short_name.equals(""))) 
		{		
			if(name.equals(""))
			{
				String output = "name_shortname_missing: relation_id" + relationID + ", shortname: " + short_name;
				content = content + output + "\r\n";
			}
		}
	}
}
