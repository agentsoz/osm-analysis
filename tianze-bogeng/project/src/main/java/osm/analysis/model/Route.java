
package osm.analysis.model;

import java.text.DecimalFormat;
import java.util.*;
public class Route {

	public Node orig;
	public Node dest;
	public ArrayList<Node> nodes;
	public double oTime; 
	public double gTime;
	public double tDifPer;
	public double tDifHr;
	public double oDist; 
	public double gDist; 
	public double dDifPer;
	public double dDifKm;
	
	public Route(){
		
	}
	
	public Route(Node orig, Node dest,int oTime, double oDis){
		nodes = new ArrayList<Node>();
		this.oTime = oTime;
		this.oDist = oDis;
		this.orig = orig;
		this.dest = dest;
	}
	
	
	public void format(){
		
		DecimalFormat df = new DecimalFormat("0.000");
		
		// ms to hr
		oTime /= 3600000;
		gTime /= 3600000;
		// m to km
		oDist /= 1000;
		gDist /= 1000;
		
		oTime = Double.parseDouble(df.format(oTime));

		gTime = Double.parseDouble(df.format(gTime));

		oDist = Double.parseDouble(df.format(oDist));

		gDist = Double.parseDouble(df.format(gDist));
		
		double big;
		if(oTime > gTime)
			big = oTime;
		else
			big = gTime;
		
		

		tDifPer = Double.parseDouble(df.format(Math.abs((gTime - oTime)/big)*100));
		tDifHr = Double.parseDouble(df.format(Math.abs(gTime - oTime)));
		
		if(oDist > gDist)
			big =  oDist;
		
		else
			big = gDist;
		
		dDifPer = Double.parseDouble(df.format(Math.abs((gDist - oDist)/big)*100));
		dDifKm = Double.parseDouble(df.format(Math.abs(gDist - oDist)));
	}
}