
package model;

import java.util.*;

public class Waypoints {

	public ArrayList<Node> nodes;
	public int assumedTime;
	public double assumedDistance;
	public int actualTime;
	public double actualDistance;
	public Node origin;
	public Node destination;
	public double timeDiff;
	
	public Waypoints(int time, double distance,Node origin, Node destination){
		nodes = new ArrayList<Node>();
		this.assumedTime = time;
		this.assumedDistance = distance;
		this.origin = origin;
		this.destination = destination;
	}
	
	
	
}