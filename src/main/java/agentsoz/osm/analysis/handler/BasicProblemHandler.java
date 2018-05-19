package agentsoz.osm.analysis.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Timer;

import agentsoz.osm.analysis.app.Main;

public abstract class BasicProblemHandler {

	boolean writeFile;
	boolean isFound;
	String path;
	Timer timer;
	
	public BasicProblemHandler()
	{
		writeFile = false;
		isFound = false;
	}
	
	// program running indicator
	public void running_timer() 
	{
		timer = new Timer();
		timer.schedule(new RunningPrompt(), 0, 1*1000);
	}
	
	public void stop_timer() 
	{
		timer.cancel();
		timer.purge();
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
	
	public void writeToFile(String content) 
	{
		if(Main.file_path == null) 
		{
			System.out.print(content);
		}
		if(Main.file_path != null)
		{
			String[] lines = content.split("\\r?\\n");
			for(String line : lines) 
			{
				LOGReadWrite.write(Main.file_path, line);
			}
		}
	}
	
	public abstract void handleProblem();

}
