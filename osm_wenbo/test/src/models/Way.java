package models;

import java.util.ArrayList;
import java.util.Hashtable;

public class Way {

	private String wayId;
	private ArrayList<String> nodes;
	private Hashtable<String, String> tags;
	
	public Way() 
	{
		nodes = new ArrayList<String>();
		tags = new Hashtable<String, String>();
	}
	public void setWayId(String wayId) 
	{
		this.wayId = wayId;
	}
	
	public String getWayId() 
	{
		return wayId;
	}

	public void addNodes(String node) 
	{
		nodes.add(node);
	}
	
	public ArrayList<String> getNodes()
	{
		return nodes;
	}
	
	public void addTags(String k, String v) 
	{
		tags.put(k,v);
	}

	public Hashtable<String, String> getTags()
	{
		return tags;
	}
}
