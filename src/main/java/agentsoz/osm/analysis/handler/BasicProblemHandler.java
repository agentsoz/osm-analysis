package agentsoz.osm.analysis.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class BasicProblemHandler {

	boolean writeFile;
	boolean isFound;
	String path;
	
	public BasicProblemHandler()
	{
		writeFile = false;
		isFound = false;
	}
	
	/**
	 * Connect to the osm.db database
	 * @return the connection object
	 * @throws SQLException 
	 * */
	public Connection connect(String url) 
	{
		Connection conn = null;
		
		try
		{
			conn = DriverManager.getConnection(url);
		}
		catch(SQLException e)
		{
			System.out.println(e.getMessage());
		}	
		return conn;
	}
	
	public void setFilePath(String path) 
	{
		writeFile = true;
		this.path = path;
	}
	
	public abstract void writeToFile(String path, String content);
	
	public abstract void handleProblem();

}
