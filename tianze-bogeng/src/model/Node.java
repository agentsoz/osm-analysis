package model;

import java.text.DecimalFormat;

public class Node {

	public String lat;
	public String lon;
	public String id;
	
	public Node(){
		lat = new String();
		lon = new String();
		id = new String();
	}
	
	public Node(String lat, String lon, String id){
		this.lat = lat;
		this.lon = lon;
		this.id = id;
	}
	
	public Node(String lat, String lon){
		this.lat = lat;
		this.lon = lon;
	}
	
	public Node(double lat, double lon){
		this.lat = Double.toString(lat);
		this.lon = Double.toString(lon);
	}
	
	public Node(double lat, double lon,String id){
		this.lat = Double.toString(lat);
		this.lon = Double.toString(lon);
		this.id = id;
	}
	
	public double set3Decimal(double s){
		DecimalFormat df = new DecimalFormat("0.000");
		df.format(s);
		return s;
	}
	
	public String set3Decimal(String s){
		double d = Double.parseDouble(s);
		DecimalFormat df = new DecimalFormat("0.000");
		df.format(d);
		return Double.toString(d);
	}
	
}