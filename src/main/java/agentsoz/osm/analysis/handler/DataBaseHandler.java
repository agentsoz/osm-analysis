package agentsoz.osm.analysis.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import agentsoz.osm.analysis.app.Main;
import agentsoz.osm.analysis.interfaces.InsertData;

public class DataBaseHandler extends BasicProblemHandler implements InsertData {
	
	@Override
	public void insert(Statement stm, Connection con) 
	{
		// set path of the new generated database
		File filename = new File(Main.OSM_FILE);
		
		InputStreamReader reader;
		try {
			reader = new InputStreamReader(new FileInputStream(filename));
			BufferedReader br = new BufferedReader(reader);
			String line = "";
			String id = "";
			String current = "";
			while((line = br.readLine()) != null) {

				if(line.contains("<node")) {
					current = "node";
					String[] nodes = line.split("\"");
					id = nodes[1];

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
					
				}else if(line.contains("<way")) {
					current = "way";
					String[] ways = line.split("\"");
					id = ways[1];
					if(ways[9].contains("'")) {
						ways[9] = ways[9].replace("'", "''");
					}
					
					String way = "INSERT INTO ways VALUES('"+ways[1]+"',"
							+ "'"+ways[3]+"',"
							+ "'"+ways[5]+"',"
							+ "'"+ways[7]+"',"
							+ "'"+ways[9]+"',"
							+ "'"+ways[11]+"')";
					stm.executeUpdate(way);
					
				}else if(line.contains("<relation")) {
					current = "relation";
					String[] relations = line.split("\"");
					id = relations[1];
					if(relations[9].contains("'")) {
						relations[9] = relations[9].replace("'", "''");
					}
					
					String relation = "INSERT INTO relations VALUES('"+relations[1]+"',"
							+ "'"+relations[3]+"',"
							+ "'"+relations[5]+"',"
							+ "'"+relations[7]+"',"
							+ "'"+relations[9]+"',"
							+ "'"+relations[11]+"')";
					stm.executeUpdate(relation);
					
				}else if(line.contains("tag")) {
					String[] tags = line.split("\"");

					if(tags[3].contains("'")) {
						tags[3] = tags[3].replace("'", "''");
					}
					
					String table = "";
					if(current.equals("node")) {
						table = "nodes_tags";
					}else if(current.equals("way")) {
						table = "ways_tags";
					}else if(current.equals("relation")) {
						table = "relations_tags";
					}
					String tag = "INSERT INTO '"+table+"' VALUES('"+id+"',"
							+ "'"+tags[1]+"',"
							+ "'"+tags[3]+"')";
					
					stm.executeUpdate(tag);	

				}else if(line.contains("member")) {
					String[] members = line.split("\"");
					
					String member = "INSERT INTO relations_members VALUES('"+id+"',"
							+ "'"+members[1]+"',"
							+ "'"+members[3]+"',"
							+ "'"+members[5]+"')";
					stm.executeUpdate(member);
				}else if(line.contains("nd")) {
					String[] ways = line.split("\"");
					String way = "INSERT INTO ways_nodes VALUES('"+id+"',"
							+ "'"+ways[1]+"')";
					stm.executeUpdate(way);
				}
			}
			
			con.commit();
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void handleProblem() 
	{
		Connection con = null;	
		Statement stm = null;
		
		//set database URL with input from user
		String url = "jdbc:sqlite:"+ Main.file_path;
		
		super.running_timer();

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
		} finally {super.stop_timer();}
	}

	private static void createBasicTable(Statement stm, Connection con) throws SQLException
	{	
		String ways = "CREATE TABLE IF NOT EXISTS ways(ID INT PRIMARY KEY NOT NULL,"
				+ "version TEXT,"
				+ "timestamp TEXT,"
				+ "uid TEXT,"
				+ "user TEXT,"
				+ "changeset TEXT)";
		
		String ways_tags = "CREATE TABLE IF NOT EXISTS ways_tags(way_id TEXT, tag_key TEXT, tag_value TEXT)";
		
		String ways_nodes = "CREATE TABLE IF NOT EXISTS ways_nodes(way_id TEXT,node_id TEXT)";
		
		String relations = "CREATE TABLE IF NOT EXISTS relations(ID INT PRIMARY KEY NOT NULL,"
				+ "version INT,"
				+ "timestamp TEXT,"
				+ "uid INT,"
				+ "user TEXT,"
				+ "changeset INT)"; 
		
		String relations_tags = "CREATE TABLE IF NOT EXISTS relations_tags(relation_id INT, tag_key TEXT, tag_value TEXT)";
		
		String relations_members = "CREATE TABLE IF NOT EXISTS relations_members(relation_id INT,"
				+ "member_type TEXT,"
				+ "member_ref TEXT,"
				+ "member_role TEXT)";
		
		String nodes = "CREATE TABLE IF NOT EXISTS nodes(id TEXT PRIMARY KEY NOT NULL,"
				+ "version TEXT,"
				+ "timestamp TEXT,"
				+ "uid TEXT,"
				+ "user TEXT,"
				+ "changeset TEXT,"
				+ "lat TEXT,"
				+ "lon TEXT)";
		
		String nodes_tags = "CREATE TABLE IF NOT EXISTS nodes_tags(nodes_id INT, tag_key TEXT, tag_value TEXT)";

			stm.executeUpdate(ways);
			stm.executeUpdate(ways_nodes);
			stm.executeUpdate(ways_tags);
			stm.executeUpdate(relations);
			stm.executeUpdate(relations_tags);
			stm.executeUpdate(relations_members);
			stm.executeUpdate(nodes_tags);
			stm.executeUpdate(nodes);
			con.commit();
	}
}
