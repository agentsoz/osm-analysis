package Handle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class Temp {
	public static void main(String args[]) {
	//	IfwayConnectedInOneRelation instance = new IfwayConnectedInOneRelation();
	//	instance.check();
		
		Connection con = null;
		Statement stm = null;
		
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:osm.db");
			stm = con.createStatement();
			con.setAutoCommit(false);
			
			String a = "SELECT * FROM ways_nodes";
			
			ResultSet s = stm.executeQuery(a);
			Set<String> b = new HashSet<>();
			while(s.next()) {
		//		System.out.println(s.getString("way_id") +" | "+ s.getString("node_id"));
		//		System.out.println(s.getRow());
				b.add(s.getString("way_id"));
			}
			System.out.println(b.size());
			con.setAutoCommit(true);
			stm.close();
			con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
		//	LOG.severe("ClassNotFoundException");
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		//	LOG.severe("SQLException");
			e.printStackTrace();
		} 
	}
}
