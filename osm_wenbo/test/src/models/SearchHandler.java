package models;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import client.testClient;
import model.interfaces.LoggingCallback;

/**
 * searchWayByNode()
 *              input: "nd", output: "wayId"
 * searchSpeedByWay()
 *              input: "way id", output: "maxspeed"
 * searchWayByName()
 *              input: "name", output: "way id"
 * searchWayByNameWithSpeed()
 *              input: "name", output: "way id" and "maxspeed"
 * 
 * @author Wenbo Peng
 * */

public class SearchHandler {

	LoggingCallback loggingCallback;
	
	public void addLoggingCallback(LoggingCallback loggingCallback) 
	{
		// call LoggingCallback constructor
		this.loggingCallback = loggingCallback;
	}
	
	public Way searchWayByNode(Way way, String nodeId)
	{
		ArrayList<String> nodes = way.getNodes();
		for(String node : nodes) 
		{
			if(node.equalsIgnoreCase(nodeId)) 
			{
				loggingCallback.showWayByNode(way.getWayId(), nodeId);
				XMLHandler.noRecord = false;
				return way;
			}		
		}
		return null;
	}
	
	public void searchSpeedByWay(Way way, String wayId) 
	{
		if(way.getWayId().equals(wayId)) 
		{
			Hashtable<String, String> tags = way.getTags();
			Set<String> keys = tags.keySet();
			for(String key : keys) 
			{
				if(key.equalsIgnoreCase("maxspeed")) 
				{
					loggingCallback.showSpeedByWay(way.getWayId(), tags.get(key));
					XMLHandler.noRecord = false;
				}
			}			
		}
	}
	
	public void searchWayByName(Way way, String wayName) 
	{
		Hashtable<String, String> tags = way.getTags();
		Set<String> keys = tags.keySet();
		for(String key : keys) 
		{
			if(key.equalsIgnoreCase("name")) 
			{
				if(tags.get(key).equalsIgnoreCase(wayName)) 
				{
					loggingCallback.showWayByName(way.getWayId(), wayName);
					XMLHandler.noRecord = false;
				}
			}
		}
	}
	
	public void searchWayByNameWithSpeed(Way way, String wayName)
	{
		Hashtable<String, String> tags = way.getTags();
		Set<String> keys = tags.keySet();
		for(String key : keys) 
		{
			if(key.equalsIgnoreCase("name")) 
			{
				String maxspeed = "no record";
				if(tags.get(key).equalsIgnoreCase(wayName))
				{
					XMLHandler.noRecord = false;
					for(String key2 : keys)
					if(key2.equalsIgnoreCase("maxspeed"))
						maxspeed = tags.get(key2);
					loggingCallback.showWayByNameWithSpeed(way.getWayId(), wayName, maxspeed);
				}
			}
		}
	}
}
  