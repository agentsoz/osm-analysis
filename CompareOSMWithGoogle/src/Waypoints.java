
package project;

import java.util.*;

public class Waypoints {

	public ArrayList<Node> nodes;
	int assumedTime;
	double assumedDistance;
	int actualTime;
	double actualDistance;
	Node origin;
	Node destination;
	double timeDiff;
	
	public Waypoints(int time, double distance,Node origin, Node destination){
		nodes = new ArrayList<Node>();
		this.assumedTime = time;
		this.assumedDistance = distance;
		this.origin = origin;
		this.destination = destination;
	}
	
	
	
}