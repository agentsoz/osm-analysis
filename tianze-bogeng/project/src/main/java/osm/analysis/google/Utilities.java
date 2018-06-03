package osm.analysis.google;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.json.JSONObject;

import osm.analysis.model.Node;
import osm.analysis.model.Route;

public class Utilities {

	public static ArrayList<Route> tFilter(ArrayList<Route> routes, double threshold,String type){

		Iterator<Route> i = routes.iterator();
		ArrayList<Route> result = new ArrayList<Route>();
		Route curr;
		while(i.hasNext()){
			curr = i.next();
			if(type == "per"){
				if(curr.tDifPer >= threshold)
					result.add(curr);
			}
			else if(type == "hr")
				if(curr.tDifHr >= threshold)
					result.add(curr);
		}
		return result;
	}
	
	public static ArrayList<Route> dFilter(ArrayList<Route> routes, double threshold, String type){
		
		Iterator<Route> i = routes.iterator();
		ArrayList<Route> result = new ArrayList<Route>();
		Route curr;
		while(i.hasNext()){
			curr = i.next();
			if(type == "per"){
				if(curr.dDifPer >= threshold)
					result.add(curr);
			}
			else if(type == "km")
				if(curr.dDifKm >= threshold * 1000)
					result.add(curr);
		}
		return result;
	}
	
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
	
	public static void printSummary(ArrayList<Route> record){
		
		int id = 1;
		if(record.size() == 0)
		
			System.out.println("No result. Please consider changing some of your arguments.");
		
		else{
			System.out.println("\n--------------------------------------------------- Summary ----------------------------------------------------\n");
			System.out.printf("%-5s%-24s%-24s%-8s%-11s%-14s%-13s%-9s%-11s%-14s%-13s",
					"No","Origin","Destination","OSM(hr)","Google(hr)","Time Diff(hr)","Time Diff(%)",
					"OSM(km)","Google(km)","Dist Diff(km)","Dist Diff(%)");
			System.out.println();
			for(Route route : record){
					System.out.printf("%-5s%-24s%-24s%-8s%-11s%-14s%-13s%-9s%-11s%-14s%-13s",
							id++,route.orig.lat+","+route.orig.lon,route.dest.lat+","+route.dest.lon,
							route.oTime,route.gTime,route.tDifHr,route.tDifPer,
							route.oDist,route.gDist,route.dDifPer,route.dDifKm);
					System.out.println();
			}
		}
		writeTemp(record);
	}
	
	public static void writeTemp(ArrayList<Route> record){
		
		FileWriter writer;
		try {
			writer = new FileWriter("temp.csv");
			writer.write("ID,ORIGIN_LAT,ORIGIN_LON,DEST_LAT,DEST_LON\n");
			Iterator<Route> iterator = record.iterator();
			int id = 1;
			while(iterator.hasNext()){
				Route curr = iterator.next();
				writer.write(id++ +","+ curr.orig.lat+","+curr.orig.lon+","+curr.dest.lat+","+curr.dest.lon+"\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void storeSummary(ArrayList<Route> record, String path){
		
		FileWriter writer;
		try {
			writer = new FileWriter(path);
			writer.write("ID,ORIGIN_LAT,ORIGIN_LON,DEST_LAT,DEST_LON,OSM_HR,OSM_KM,GOOGLE_HR,GOOGLE_KM,DIFF_TIME_PERCNET,DIFF_DIST_PERCENT\n");
			Iterator<Route> iterator = record.iterator();
			int id = 1;
			while(iterator.hasNext()){
				Route curr = iterator.next();
				writer.write(id++ +","+ curr.orig.lat+","+curr.orig.lon+","+curr.dest.lat+","+curr.dest.lon+","+
				+curr.oTime+","+curr.oDist+","+curr.gTime+","+curr.gDist+","+
				curr.tDifPer+","+curr.dDifPer+"\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	

	
	public static ArrayList<Integer> parseIdList(String input){
		StringTokenizer st = new StringTokenizer(input,",");
		ArrayList<Integer> id = new ArrayList<Integer>();
		
		while(st.hasMoreTokens()){
			String cur = st.nextToken();
			StringTokenizer cst = new StringTokenizer(cur,"-");
			
			if (cst.countTokens() == 1)
				id.add(Integer.parseInt(cst.nextToken()));
			else if(cst.countTokens() == 2){
				int fir = Integer.parseInt(cst.nextToken());
				int sec = Integer.parseInt(cst.nextToken()); 
				for(int c = fir; c <= sec; c++)
					id.add(c);
			}
		}
		
		return id;
	}
	
	public static void storeDetail(ArrayList<Integer> idList, String path){
		
		
		Scanner scanner = null;
		Writer writer = null;
		try {
			scanner = new Scanner(new FileInputStream("temp.csv"));
			scanner.nextLine();
			String line = null;
			StringTokenizer st = null;
			Node fir = new Node(); Node sec = new Node();
			Route curr = null;
			writer = new FileWriter(path);
			writer.write("ID,ORIGIN_LAT,ORIGIN_LON,DEST_LAT,DEST_LON,OSM_HR,OSM_KM,GOOGLE_HR,GOOGLE_KM");
			while(scanner.hasNextLine()){
				line = scanner.nextLine();
				st = new StringTokenizer(line,",");
				int no = Integer.parseInt(st.nextToken());
				if(idList.contains(no)){
					fir.lat = st.nextToken();
					fir.lon = st.nextToken();
					sec.lat = st.nextToken();
					sec.lon = st.nextToken();
					curr = Sender.goo(Sender.osm(fir, sec));
					writer.write(no +","+ curr.orig.lat+","+curr.orig.lon+","+curr.dest.lat+","+curr.dest.lon+","+
							+curr.oTime+","+curr.oDist+","+curr.gTime+","+curr.gDist+","+
							curr.tDifPer+","+curr.dDifPer+"\n");
					Iterator<Node> i1 = curr.nodes.iterator();
					Iterator<Node> i2 = curr.nodes.iterator();
					Node first,second;
					second = i2.next();
					int subId = 1;
					while(i2.hasNext()){
						first = i1.next();
						second = i2.next();
						Route div = Sender.gooSimple(Sender.osmSimple(first, second));
						
						writer.write(no+"."+subId++ +",origin="+div.orig.lat+","+div.orig.lon+",dest="+div.dest.lat+","+div.dest.lon
									+",time_osm="+div.oTime+"ms,dist_osm="+div.oDist+"m,time_google="+div.gTime+"ms,dist_google="+div.gDist+"m\n");
						
					}
					writer.write("\n");
					scanner.close();
					writer.close();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
