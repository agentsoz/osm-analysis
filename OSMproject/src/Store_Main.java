import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class Store_Main {
	
	static final Logger LOG = Logger.getLogger(Store_Main.class.getName());
	
	public static void main(String args[]) {

		Connection con = null;
		Statement stm = null;
		
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:osm.db");
			stm = con.createStatement();
			con.setAutoCommit(false);

			con.setAutoCommit(true);
			stm.close();
			con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			LOG.severe("ClassNotFoundException");
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOG.severe("SQLException");
			e.printStackTrace();
		} 
	
	}
	
	private static void createProblemTable(Statement stm, Connection con) throws SQLException {
		// TODO Auto-generated method stub
		String maxspeed = "CREATE TABLE maxspeed(ID_FIRST INT NOT NULL, ID_SECOND INT NOT NULL)";
		String maxspeedRW = "CREATE TABLE maxspeed(relation_id TEXT NOT NULL, way_id TEXT NOT NULL)";
		String routeRW = "CREATE TABLE routeRW(relation_id TEXT NOT NULL, way_id TEXT NOT NULL)";
		
		stm.executeUpdate(routeRW);
		stm.executeUpdate(maxspeed);
		stm.executeUpdate(maxspeedRW);
		
	}

	private static void createBasicTable(Statement stm, Connection con) throws SQLException {

		
		
		String ways = "CREATE TABLE ways(ID INT PRIMARY KEY NOT NULL,"
				+ "version TEXT,"
				+ "timestamp TEXT,"
				+ "uid TEXT,"
				+ "user TEXT,"
				+ "changeset TEXT)";
		String ways_tags = "CREATE TABLE ways_tags(way_id TEXT, tag_key TEXT, tag_value TEXT)";
		String ways_nodes = "CREATE TABLE ways_nodes(way_id TEXT,node_id TEXT)";
		String relations = "CREATE TABLE relations(ID INT PRIMARY KEY NOT NULL,"
				+ "version INT,"
				+ "timestamp TEXT,"
				+ "uid INT,"
				+ "user TEXT,"
				+ "changeset INT)"; 
		String relations_tags = "CREATE TABLE relations_tags(relation_id INT, tag_key TEXT, tag_value TEXT)";
		String relations_members = "CREATE TABLE relations_members(relation_id INT,"
				+ "member_type TEXT,"
				+ "member_ref TEXT,"
				+ "member_role TEXT)";
		String nodes = "CREATE TABLE nodes(id TEXT PRIMARY KEY NOT NULL,"
				+ "version TEXT,"
				+ "timestamp TEXT,"
				+ "uid TEXT,"
				+ "user TEXT,"
				+ "changeset TEXT,"
				+ "lat TEXT,"
				+ "lon TEXT)";
		String nodes_tags = "CREATE TABLE nodes_tags(nodes_id INT, tag_key TEXT, tag_value TEXT)";

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
