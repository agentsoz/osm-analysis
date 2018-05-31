
package osm.analysis.model;

import java.text.DecimalFormat;
import java.util.*;
public class Route {

	public Node orig;
	public Node dest;
	public ArrayList<Node> nodes;
	public int oTime;
	public int gTime;
	public double tDifPer;
	public double tDifMs;
	public double oDis;
	public double gDis;
	public double dDifPer;
	public double dDifM;
	
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
		DecimalFormat df = new DecimalFormat("0.000000");
		this.tDifPer = Double.parseDouble(df.format(Math.abs((this.gTime - this.oTime)/big)));
		this.tDifMs = Math.abs(this.gTime - this.oTime);
		
		// Store distance diff
		double big2 ;
		if(this.oDis > this.gDis)
			big2 =  this.oDis;
		
		else
			big2 = this.gDis;
		
		this.dDifPer = Double.parseDouble(df.format(Math.abs((this.gDis - this.oDis)/big2)));
		this.dDifM = Math.abs(this.gDis - this.oDis);
	}
}