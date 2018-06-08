package osm.analysis.google;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandler {

	public static String osmPath;
	public static String dbPath;
	
	public static void generate() 
	{
		Connection con = null;	
		Statement stm = null;
		
		String url = "jdbc:sqlite:"+dbPath;

		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection(url);
			stm = con.createStatement();
			con.setAutoCommit(false);
			
			createBasicTable(stm, con);
		    insert(stm,con);

			con.setAutoCommit(true);
			stm.close();
			con.close();
		} catch (ClassNotFoundException e) {
		
			e.printStackTrace();
		} catch (SQLException e) {
			
			e.printStackTrace();
		} 
	}

	public static void insert(Statement stm, Connection con) 
	{
		File osm = new File(osmPath);
		
		InputStreamReader reader;
		try {
			reader = new InputStreamReader(new FileInputStream(osm));
			BufferedReader br = new BufferedReader(reader);
			String line = "";
			while((line = br.readLine()) != null) {

				if(line.contains("<node")) {
					String[] nodes = line.split("\"");

					if(nodes[9].contains("'")) {
						nodes[9] = nodes[9].replace("'", "''");
					}
					
					String node = "INSERT INTO nodes VALUES('"+nodes[1]+"',"
							+ "'"+nodes[3]+"',"
							+ "'"+nodes[5]+"',"
							+ "'"+nodes[7]+"',"
							+ "'"+nodes[9]+"',"
							+ "'"+nodes[11]+"',"
							+ "'"+nodes[13]+"',"
							+ "'"+nodes[15]+"')";
					stm.executeUpdate(node);
					
				}
			}
			
			con.commit();
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
	private static void createBasicTable(Statement stm, Connection con) throws SQLException
	{	
		
		String nodes = "CREATE TABLE IF NOT EXISTS nodes(id TEXT PRIMARY KEY NOT NULL,"
				+ "version TEXT,"
				+ "timestamp TEXT,"
				+ "uid TEXT,"
				+ "user TEXT,"
				+ "changeset TEXT,"
				+ "lat TEXT,"
				+ "lon TEXT)";
		
			stm.executeUpdate(nodes);
			con.commit();
	}
	
}
