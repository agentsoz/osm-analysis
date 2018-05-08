
package model;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import google.Sender;
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
	
	public void format(){
		int big;
		// Store time diff
		if(this.oTime > this.gTime)
			big = this.oTime;
		else
			big = this.gTime;
		DecimalFormat df = new DecimalFormat("0.0000");
		this.timeDif = Double.parseDouble(df.format(Math.abs((this.gTime - this.oTime)/(double)big)));
		
		// Store distance diff
		Double big2 ;
		if(this.oDis > this.gDis)
			big2 =  this.oDis;
		
		else
			big2 = this.gDis;
		
		this.disDif = Double.parseDouble(df.format(Math.abs((this.gDis - this.oDis)/big2)));
	}
}