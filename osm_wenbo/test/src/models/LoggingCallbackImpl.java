package models;

import java.util.logging.Level;
import java.util.logging.Logger;

import model.interfaces.LoggingCallback;

public class LoggingCallbackImpl implements LoggingCallback{

	private Logger logger = Logger.getLogger("osm-analysis");
	
	public LoggingCallbackImpl()
	{
		logger.setLevel(Level.FINE);
	}
	
	public void showWayByNode(String wayId, String nodeId) 
	{
		logger.log(Level.INFO, "way " + wayId + " contains node " + nodeId);
	}
	
	public void showSpeedByWay(String wayId, String speed) 
	{
		logger.log(Level.INFO, "way " + wayId + " max speed is " + speed);
	}

	public void showWayByName(String wayId, String wayName) 
	{
		logger.log(Level.INFO, "way " + wayId + " name is " + wayName);
	}

	public void showWayByNameWithSpeed(String wayId, String wayName, String maxspeed) 
	{
		logger.log(Level.INFO, "way " + wayId + " belongs to " + wayName + ", maxspeed : " + maxspeed);
	}

	public void showNoRecord() 
	{
		logger.log(Level.INFO, "Sorry, no record found");
//		System.out.println("no record");
	}
}
