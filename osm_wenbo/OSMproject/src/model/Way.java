package model;

import java.util.ArrayList;
import java.util.List;

public class Way extends Member{
	
	List<String> nodes_ref;
	private String id;
	private int maxspeed;
	private String name;
	//tag how many marked traffic lanes there are on a highway
	public String lanes;
	public String oneWay;
	
	public Way() {
		nodes_ref = new ArrayList<>();
		name = new String();
		lanes = new String();
		oneWay = new String();
	}
	
	public void setId(String id) {this.id = id;}

	public String getId() {return id;}
	
	public void setSpeed(int speed){this.maxspeed = speed;}
	
	public int getSpeed() {return maxspeed;}
}
