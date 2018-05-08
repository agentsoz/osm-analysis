package agentsoz.osm.analysis.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class BasicProblemHandler {

	/**
	 * Connect to the osm.db database
	 * @return the connection object
	 * */
	public Connection connect(String url) 
	{
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(url);
		}catch(SQLException e){
			System.out.println(e.getMessage());
		}	
		return conn;
	}
	
	public abstract void handleProblem();

}
