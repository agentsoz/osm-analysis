package agentsoz.osm.analysis.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SearchMissingAttribute {
	Statement stm;
	Statement stm1;
	Connection con;
	
	String choose;
	//choose is 'ways' 'relations' or 'nodes'
	public SearchMissingAttribute(String choose) {
		this.choose = choose;
	}
	
	
	public void search(String attName) {
		
		String _name = "";
		
		try {
			
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:osm.db");
			stm = con.createStatement();
			stm1 = con.createStatement();
			String sql = null;
			if(choose.equals("nodes")) {
				_name = "node_id";
				sql = "SELECT '"+_name+"' FROM nodes_tags WHERE tag_key='"+attName+"'";
			}else if(choose.equals("ways")) {
				_name = "way_id";
				sql = "SELECT '"+_name+"' FROM ways_tags WHERE tag_key='"+attName+"'";
			}else if(choose.equals("relations")) {
				_name = "relation_id";
				sql = "SELECT '"+_name+"' FROM relations_tags WHERE tag_key='"+attName+"'";
			}else {
				System.out.println("input is incorrect, please input 'node' or 'way' or 'relation'.");
				System.exit(0);
			}
			
			List<String> IDs = compare();
			ResultSet res = stm.executeQuery(sql);
			String temp;
			System.out.println(sql);
			while(res.next()) {
				temp = res.getString("relation_id");
				IDs.remove(temp);
			}
			
			System.out.println("the '"+choose+"' which do not have '"+attName+"' attribute:");
			
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

	private List<String> compare() throws SQLException {
		// TODO Auto-generated method stub
		String sql = "SELECT ID FROM '"+choose+"'";
		ResultSet res = stm1.executeQuery(sql);
		List<String> IDs = new ArrayList<String>();
		
		while(res.next()) {
			IDs.add(res.getString("ID"));
		}
		stm1.close();
		
		return IDs;
	}
}
