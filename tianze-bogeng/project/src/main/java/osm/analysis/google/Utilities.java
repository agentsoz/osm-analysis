package osm.analysis.google;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import osm.analysis.model.*;
import osm.org.json.JSONObject;

public class Utilities {


	// Threshold is either smaller than 1 or bigger than 1 (min)
	public static ArrayList<Route> tFilter(ArrayList<Route> routes, double threshold){
	
		String type;
		if(threshold < 1)
			type = "per";
		else
			type = "ms";
		
		Iterator<Route> ite = routes.iterator();
		ArrayList<Route> record = new ArrayList<Route>();
		Route curr;
		while(ite.hasNext()){
			curr = ite.next();
			if(type == "per"){
				if(curr.tDifPer >= threshold)
					record.add(curr);
			}
			else if(type == "ms")
				if(curr.tDifMs >= threshold)
					record.add(curr);
		}
		return record;
	}
	
	public static void printSummary(ArrayList<Route> record){
		
		//Both write them into file and print them to user's view .
		int id = 1;
		if(record.size() == 0)
		
			System.out.println("No result. Please consider changing some of your arguments.");
		
		else{
			System.out.println("\n------------------------------------- Summary --------------------------------------\n");
			System.out.printf("%-5s%-24s%-24s%-13s%-17s%-15s%-15s%-15s%-15s","No.","Origin","Destination","OSM(ms)","Google(ms)","Time Diff (%)","Time Diff (ms)","Dist Diff (%)","Dist Diff (m)");
			System.out.println();
			DecimalFormat df = new DecimalFormat("0.000");
			DecimalFormat df2  = new DecimalFormat("0");
			for(Route route : record){
					System.out.printf("%-5s%-24s%-24s%-13s%-17s%-15s%-15s%-15s%-15s",id++,route.orig.lat+","+route.orig.lon,route.dest.lat+","+route.dest.lon,route.oTime,route.gTime,df.format(route.tDifPer*100),df2.format(route.tDifMs),df.format(route.dDifPer*100),df2.format(route.dDifM));
					System.out.println();
			}
		}
	}
	
	
	
	public static ArrayList<Route> dFilter(ArrayList<Route> routes, double threshold){
	
	// Tell if user wants to view difference percent or only km difference.
		String type;
		if(threshold < 1)
			type = "per";
		else
			type = "km";
		
		// Store the problem ones into ArrayList.
		Iterator<Route> ite = routes.iterator();
		ArrayList<Route> record = new ArrayList<Route>();
		Route curr;
		while(ite.hasNext()){
			curr = ite.next();
			if(type == "per"){
				if(curr.dDifPer >= threshold)
					record.add(curr);
			}
			else if(type == "km")
				if(curr.dDifM >= threshold * 1000)
					record.add(curr);
		}
		
		return record;
	}
	
	// Input format JSON: {routes:[{ori:{lat:,lon:},dest:{lat:,lon:}}]}

	public static ArrayList<Route> parseCoorList(String input){
		
		
		ArrayList<Route> routes = new ArrayList<Route>();
		JSONObject list = new JSONObject(input);
		
		for(int i = 0; i < list.getJSONArray("routes").length(); i++){
			Node ori = new Node();
			Node dest = new Node();
			Route curr = new Route();
			
			double lat1,lat2,lon1,lon2;
			lat1 = list.getJSONArray("routes").getJSONObject(i).getJSONObject("ori").getDouble("lat");
			lon1 = list.getJSONArray("routes").getJSONObject(i).getJSONObject("ori").getDouble("lon");
			lat2 = list.getJSONArray("routes").getJSONObject(i).getJSONObject("dest").getDouble("lat");
			lon2 = list.getJSONArray("routes").getJSONObject(i).getJSONObject("dest").getDouble("lon");

			ori.lat = Double.toString(lat1);
			ori.lon = Double.toString(lon1);
			dest.lat = Double.toString(lat2);
			dest.lon = Double.toString(lon2);
			try {	
				curr = Sender.goo(Sender.osm(ori, dest));
				routes.add(curr);
				System.out.println("Generated: "+curr.orig.lat+","+curr.orig.lon+"/"+curr.dest.lat+","+curr.dest.lon);
			} catch (IOException e) {
				System.out.println("Failed to generate one route.");
			}
		}
		
		return routes;
	}
	
	public static void storeSummary(ArrayList<Route> record, String path){
		
		FileWriter writer;
		try {
			writer = new FileWriter(path);
			writer.write("Summary output\n-----\n");
			Iterator<Route> iterator = record.iterator();
			int id = 0;
			while(iterator.hasNext()){
				id ++;
				Route curr = iterator.next();
				writer.write("route"+id+",origin="+curr.orig.lat+","+curr.orig.lon+",dest="+curr.dest.lat+","+curr.dest.lon+
				",time_osm="+curr.oTime+"ms,dist_osm="+curr.oDis+"m,time_google="+curr.gTime+"ms,dist_google="+curr.gDis+"m,time_diff="+
				curr.tDifPer+",dist_diff="+curr.dDifPer+"\n");
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
	public static void storeDetail(ArrayList<Route> record,String path){
		
		System.out.println("Storing data...");
		try {
			
			FileWriter writer= new FileWriter(path);
			
			writer.write("Detailed output\n-----\n");
			Iterator<Route> iterator = record.iterator();
			int id = 0;
			while(iterator.hasNext()){
				id ++;
				Route curr = iterator.next();
				writer.write("route"+id+",origin="+curr.orig.lat+","+curr.orig.lon+",dest="+curr.dest.lat+","+curr.dest.lon+
						",time_osm="+curr.oTime+"ms,dist_osm="+curr.oDis+"m,time_google="+curr.gTime+"ms,dist_google="+curr.gDis+"m,time_diff="+
						curr.tDifPer+",dist_diff="+curr.dDifPer+"\n");				
				// i1 - first node, i2 - second
				Iterator<Node> i1 = curr.nodes.iterator();
				Iterator<Node> i2 = curr.nodes.iterator();
				Node first,second;
				// i2 starts from [0]
				second = i2.next();
				int subId = 0;
				while(i2.hasNext()){
					subId ++;
					// i1 starts from [0]
					first = i1.next();
					// i2 starts from [1]
					second = i2.next();
					Route div = Sender.gooSimple(Sender.osmSimple(first, second));
					
						writer.write("route"+id+"."+subId+",origin="+div.orig.lat+","+div.orig.lon+",dest="+div.dest.lat+","+div.dest.lon
								+",time_osm="+div.oTime+"ms,dist_osm="+div.oDis+"m,time_google="+div.gTime+"ms,dist_google="+div.gDis+"m\n");
					
				}
				writer.write("\n");
			}
			
			// Close the writers
			writer.close();
			System.out.println("Storing finished.\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	//meters
	public static double distance(Node node1, Node node2){
		double lat1 = Double.parseDouble(node1.lat);
		double lon1 = Double.parseDouble(node1.lon);
		double lat2 = Double.parseDouble(node2.lat);
		double lon2 = Double.parseDouble(node2.lon);
		
		double R = 6371e3; // meters
		double o1 = Math.toRadians(lat1);
		double o2 = Math.toRadians(lat2);
		double oDiff = Math.toRadians(lat2-lat1);
		double vDiff = Math.toRadians(lon2-lon1);

		double a = Math.sin(oDiff/2) * Math.sin(oDiff/2) +
		        Math.cos(o1) * Math.cos(o2) *
		        Math.sin(vDiff/2) * Math.sin(vDiff/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

		double d = R * c;
		return d;
	}
	
}
