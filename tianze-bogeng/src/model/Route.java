
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
	public double timeDifMs;
	public double disDifM;
	
	public Route(){
		
	}
	
	public Route(Node orig, Node dest,int oTime, double oDis){
		nodes = new ArrayList<Node>();
		this.oTime = oTime;
		this.oDis = oDis;
		this.orig = orig;
		this.dest = dest;
	}
	
	public void format(){
		double big;
		
		// Store time diff
		if(this.oTime > this.gTime)
			big = this.oTime;
		else
			big = this.gTime;
		DecimalFormat df = new DecimalFormat("0.00000");
		this.timeDif = Double.parseDouble(df.format(Math.abs((this.gTime - this.oTime)/big)));
		this.timeDifMs = Math.abs(this.gTime - this.oTime);
		
		// Store distance diff
		double big2 ;
		if(this.oDis > this.gDis)
			big2 =  this.oDis;
		
		else
			big2 = this.gDis;
		
		this.disDif = Double.parseDouble(df.format(Math.abs((this.gDis - this.oDis)/big2)));
		this.disDifM = Math.abs(this.gDis - this.oDis);
	}
}