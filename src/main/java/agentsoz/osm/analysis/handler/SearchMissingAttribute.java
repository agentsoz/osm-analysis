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
	
	//choices are 'ways' 'relations' and 'nodes'
	public static String choice;

	
	public SearchMissingAttribute(String databaseUrl)
	{
		url = "jdbc:sqlite:" + databaseUrl;
	}
	
	public void search(String attName) 
	{	
		String _name = "";
		
		try 
		{	
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection(url);
			stm = con.createStatement();
			stm1 = con.createStatement();
			String sql = null;
			
			if(choice.equals("node")) {
				_name = "node_id";
				sql = "SELECT '"+_name+"' FROM nodes_tags WHERE tag_key='"+attName+"'";
			}else if(choice.equals("way")) {
				_name = "way_id";
				sql = "SELECT '"+_name+"' FROM ways_tags WHERE tag_key='"+attName+"'";
			}else if(choice.equals("relation")) {
				_name = "relation_id";
				sql = "SELECT '"+_name+"' FROM relations_tags WHERE tag_key='"+attName+"'";
			}else {
				System.out.println("incorrect input, please input 'node' or 'way' or 'relation'.");
				System.exit(0);
			}
			
			List<String> IDs = compare();
			ResultSet res = stm.executeQuery(sql);
			String temp;
			System.out.println(sql);
			while(res.next()) {
				temp = String.valueOf(res.getInt(_name));
				IDs.remove(temp);
			}
			
			System.out.println("the '"+choice+"' which do not have '"+attName+"' attribute:");
			
			for(String id : IDs) {
				System.out.println(id);
			}
			
			stm.close();
			con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private List<String> compare() throws SQLException 
	{
		// TODO Auto-generated method stub
		String sql = "SELECT ID FROM '"+choice+"s'";
		ResultSet res = stm1.executeQuery(sql);
		List<String> IDs = new ArrayList<String>();
		
		while(res.next()) {
			IDs.add(res.getString("ID"));
		}
		stm1.close();
		
		return IDs;
	}

	@Override
	public void handleProblem() {
		// TODO Auto-generated method stub
		
	}
}
