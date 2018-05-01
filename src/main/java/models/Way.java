package models;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Way extends Member{
	
	private List<String> nodes_ref;
	private String id;
	private int maxspeed;
	private String name;
	//tag how many marked traffic lanes there are on a highway
	public String lanes;
	public String oneWay;
	public String bicycle;
	private Hashtable<String, String> tags;
	
	public Way() 
	{
		nodes_ref = new ArrayList<>();
		name = new String();
		lanes = new String();
		oneWay = new String();
		bicycle = new String();
		tags = new Hashtable<String, String>();
	}
	
	public void setId(String id) {this.id = id;}

	public String getId() {return id;}
	
	public void setSpeed(int speed){this.maxspeed = speed;}
	
	public int getSpeed() {return maxspeed;}
	
	public void setName(String name) {this.name = name;}
	
	public String getName() {return name;}
	
	public void addTags(String k, String v) {tags.put(k,v);}

	public Hashtable<String, String> getTags(){return tags;}
	
	public void addNodesRef(String arg) {nodes_ref.add(arg);}

	public List<String> getNodesRef() {return nodes_ref;}

}
