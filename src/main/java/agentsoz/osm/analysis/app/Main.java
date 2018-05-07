package agentsoz.osm.analysis.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.logging.Logger;
import agentsoz.osm.analysis.handler.*;

public class Main {
	
	static final Logger LOG = Logger.getLogger(Main.class.getName());
//	static String OSM_FILE = "C:\\Users\\Castiel\\Desktop\\osm_wenbo\\OSMproject\\mount_alexander_shire_network.osm";
	public static String OSM_FILE;
	static BasicProblemHandler handler;
	
	/**
	 * Parse args[] here and set variables from commandline options
	 * For instance, --way-file FILE could be used to set the var WAY_FILE to some
	 * user specified value.
	 * */
	public static int parse(String[] args) 
	{
		for(int i = 0; i < args.length-1; i++) 
		{
			if(args[i].equals("-file")) 
			{
				OSM_FILE = args[i+1];
				return 1;
			}
			else if(args[i].equals("-maxspeedgapproblem")) 
			{
				int speed = Integer.valueOf(args[i+1]);
				String dbUrl = args[i+2];
				
				handler = new MaxSpeedGapProblemHandler(dbUrl, speed);
			}
			else if(args[i].equals("-relationspeedproblem"))
			{
				String dbUrl = args[i+1];
				handler = new RelationSpeedProblemHandler(dbUrl);
			}
//			if(args[i].equals("-prepare"))
//			{
//				DataHandler.getInstance().prepare();
//			}
			else if(args[i].equals("-sameRouteInRWProblem"))
			{
				String dbUrl = args[i+1];
				handler = new SameRouteInRWProblemHandler(dbUrl);
			}
			
		}
		return 2;
		
	} 
	
	public static void main(String args[]) 
	{
		int ref_problem = parse(args);
		
		switch(ref_problem)
		{
			case 1: createTables();
			        break;
			
			case 2: handler.handleProblem();
			        break;
		}
	
		
	}
	
	public static void createTables() 
	{
		Connection con = null;	
		Statement stm = null;

		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:osm.db");
			stm = con.createStatement();
			con.setAutoCommit(false);
			
			createBasicTable(stm, con);
			DataBaseHandler.getInstance().insert(stm,con);
			createProblemTable(stm, con);

			con.setAutoCommit(true);
			stm.close();
			con.close();
		} catch (ClassNotFoundException e) {
			
			LOG.severe("ClassNotFoundException");
			e.printStackTrace();
		} catch (SQLException e) {
			
			LOG.severe("SQLException");
			e.printStackTrace();
		} 
	}
		
	private static void createProblemTable(Statement stm, Connection con) throws SQLException 
	{
		String maxspeed = "CREATE TABLE IF NOT EXISTS maxspeed(ID_FIRST INT NOT NULL, ID_SECOND INT NOT NULL)";
		String maxspeedRW = "CREATE TABLE IF NOT EXISTS maxspeed(relation_id TEXT NOT NULL, way_id TEXT NOT NULL)";
		String routeRW = "CREATE TABLE IF NOT EXISTS routeRW(relation_id TEXT NOT NULL, way_id TEXT NOT NULL)";
		
		stm.executeUpdate(routeRW);
		stm.executeUpdate(maxspeed);
		stm.executeUpdate(maxspeedRW);
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
