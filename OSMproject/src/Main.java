import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
	
	
	public static void main(String args[]) {
		
		InsertData insert;	
		Connection con = null;
		Statement stm = null;
		
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:osm.db");
			stm = con.createStatement();
			con.setAutoCommit(false);
			
			createTable(stm, con);
			
			insert = HandleWay.getInstance();
			insert.insert(stm, con);
			insert = HandleRelation.getInstance();
			insert.insert(stm, con);
			insert = HandleNode.getInstance();
			insert.insert(stm, con);
			
			con.setAutoCommit(true);
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
	
	private static void createTable(Statement stm, Connection con) {

		String ways = "CREATE TABLE ways(ID INT PRIMARY KEY NOT NULL,"
				+ "version TEXT,"
				+ "timestamp TEXT,"
				+ "uid TEXT,"
				+ "user TEXT,"
				+ "changeset TEXT)";
		String ways_tags = "CREATE TABLE ways_tags(way_id TEXT,tag TEXT)";
		String ways_nodes = "CREATE TABLE ways_nodes(way_id TEXT,node_id TEXT)";
		String relations = "CREATE TABLE relations(ID INT PRIMARY KEY NOT NULL,"
				+ "version INT,"
				+ "timestamp TEXT,"
				+ "uid INT,"
				+ "user TEXT,"
				+ "changeset INT)"; 
		String relations_tags = "CREATE TABLE relations_tags(relation_id INT, tag TEXT)";
		String relations_members = "CREATE TABLE relations_members(relation_id INT,"
				+ "member_type TEXT,"
				+ "member_ref TEXT,"
				+ "member_role TEXT)";
		String nodes = "CREATE TABLE nodes(id INT PRIMARY KEY NOT NULL,"
				+ "version TEXT,"
				+ "timestamp TEXT,"
				+ "uid TEXT,"
				+ "user TEXT,"
				+ "changeset TEXT,"
				+ "lat TEXT,"
				+ "lon TEXT)";
		String nodes_tags = "CREATE TABLE nodes_tags(nodes_id INT, tag_key TEXT, tag_value TEXT)";
		try {
			stm.executeUpdate(ways);
			stm.executeUpdate(ways_nodes);
			stm.executeUpdate(ways_tags);
			stm.executeUpdate(relations);
			stm.executeUpdate(relations_tags);
			stm.executeUpdate(relations_members);
			stm.executeUpdate(nodes_tags);
			stm.executeUpdate(nodes);
			con.commit();
		} catch (SQLException x) {
			// TODO Auto-generated catch block
			x.printStackTrace();
		}
	}
	
}
