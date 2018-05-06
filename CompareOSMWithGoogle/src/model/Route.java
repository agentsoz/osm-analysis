
package model;

import java.util.*;

public class Route {

	public Node orig;
	public Node dest;
	public ArrayList<Node> nodes;
	public int oTime;
	public double oDis;
	public int gTime;
	public double gDis;
	public double timeDif;
	public double disDif;
	
	public Route(Node orig, Node dest,int time, double osmDis){
		nodes = new ArrayList<Node>();
		this.oTime = time;
		this.oDis = osmDis;
		this.orig = orig;
		this.dest = dest;
	}
	
	
	
}