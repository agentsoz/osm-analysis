package google;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import model.*;

public class Utilities {

	public static void recordCoor(Route route){
		try {
			FileWriter fw = new FileWriter("co.txt");
			for(Node n : route.nodes){
				fw.write(n.lat+","+n.lon+"\n");
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void analyseRoutesTimePer(ArrayList<Route> routes, double threshold){

		int id = 1;
		Iterator<Route> ite = routes.iterator();
		
		//Store the problem ones
		ArrayList<Route> record = new ArrayList<Route>();
		while(ite.hasNext()){
			Route curr = ite.next();
			if(curr.timeDif >= threshold){
				record.add(curr);
			}
			
		}
		
		//Both write them into file and print them to user's view .
		if(record.size()==0)
			System.out.println("No result. Please consider changing some of your arguments.");
		else{
			System.out.println("\n-----------------------------Results that exceed threshold------------------------------\n");
			System.out.printf("%-5s%-24s%-24s%-13s%-17s%-15s","No.","Origin","Destination","OSM(ms)","Google(ms)","Time Diff Ratio");
			System.out.println();
			for(Route route : record){
				System.out.printf("%-5s%-24s%-24s%-13s%-17s%-15s",id++,route.orig.lat+","+route.orig.lon,route.dest.lat+","+route.dest.lon,route.oTime,route.gTime,route.timeDif);
				System.out.println();
			}
			System.out.println("\nWriting data into files...");
			writeToFile(record);
			System.out.println("Data is stored in google.csv and osm.csv.");
		}
	}
	
	public static void analyseRoutesTimeMin(ArrayList<Route> routes, double threshold){
		int id = 1;
		Iterator<Route> ite = routes.iterator();
		
		//Store the problem ones
		ArrayList<Route> record = new ArrayList<Route>();
		while(ite.hasNext()){
			Route curr = ite.next();
			if(curr.timeDifMs >= threshold*60000)
				record.add(curr);
		}
		
		//Both write them into file and print them to user's view .
		if(record.size()==0)
			System.out.println("No result. Please consider changing some of your arguments.");
		else{
			System.out.println("\n-----------------------------Results that exceed threshold------------------------------\n");
			System.out.printf("%-5s%-24s%-24s%-13s%-17s%-15s","No.","Origin","Destination","OSM(ms)","Google(ms)","Time Diff (ms)");
			System.out.println();
			for(Route route : record){
				System.out.printf("%-5s%-24s%-24s%-13s%-17s%-15s",id++,route.orig.lat+","+route.orig.lon,route.dest.lat+","+route.dest.lon,route.oTime,route.gTime,route.timeDifMs);
				System.out.println();
			}
			System.out.println("\nWriting data into files...");
			writeToFile(record);
			System.out.println("Data is stored in google.csv and osm.csv.");		}
	}
	
	public static void analyseRoutesDistPer(ArrayList<Route> routes, double threshold){
	
			int id = 1;
			Iterator<Route> ite = routes.iterator();
			
			//Store the problem ones
			ArrayList<Route> record = new ArrayList<Route>();
			while(ite.hasNext()){
				Route curr = ite.next();
				if(curr.disDif >= threshold)
					record.add(curr);
			}
			
			//Both write them into file and print them to user's view .
			if(record.size()==0)
				System.out.println("No result. Please consider changing some of your arguments.");
			else{
				System.out.println("\n-----------------------------Results that exceed threshold------------------------------\n");
				System.out.printf("%-5s%-24s%-24s%-13s%-17s%-15s","No.","Origin","Destination","OSM(m)","Google(m)","Dist Diff Ratio");
				System.out.println();
				for(Route route : record){
					
					System.out.printf("%-5s%-24s%-24s%-13s%-17s%-15s",id++,route.orig.lat+","+route.orig.lon,route.dest.lat+","+route.dest.lon,route.oDis,route.gDis,route.disDif);
					System.out.println();
				}
				System.out.println("\nWriting data into files...");
				writeToFile(record);
				System.out.println("Data is stored in google.csv and osm.csv.");			}
	}
	
	public static void analyseRoutesDistKm(ArrayList<Route> routes, double threshold){
		
		int id = 1;
		Iterator<Route> ite = routes.iterator();
		
		//Store the problem ones
		ArrayList<Route> record = new ArrayList<Route>();
		while(ite.hasNext()){
			Route curr = ite.next();
			if(curr.disDifM >= threshold*1000)
				record.add(curr);
		}
		
		//Both write them into file and print them to user's view .
		if(record.size()==0)
			System.out.println("No result. Please consider changing some of your arguments.");
		else{
			System.out.println("\n-----------------------------Results that exceed threshold------------------------------\n");
			System.out.printf("%-5s%-24s%-24s%-13s%-17s%-15s","No.","Origin","Destination","OSM(m)","Google(m)","Dist Diff (m)");
			System.out.println();
			for(Route route : record){
				System.out.printf("%-5s%-24s%-24s%-13s%-17s%-15s",id++,route.orig.lat+","+route.orig.lon,route.dest.lat+","+route.dest.lon,route.oDis,route.gDis,route.disDifM);
				System.out.println();
			}
			System.out.println("\nWriting data into files...");
			writeToFile(record);
			System.out.println("Data is stored in google.csv and osm.csv.");		}
	}
	
	
	// Input is like : 35,140/23,25;56,150/24,28;35,140/23,25.... (lat/lon,lat/lon;lat/lon,lat/lon;...)

	public static ArrayList<Route> parseListOfCoor(String input){

		ArrayList<Route> routes = new ArrayList<Route>();
		
		StringTokenizer stoken1 = new StringTokenizer(input,";");
		
		//Handle each "ori/dest" pair
		while(stoken1.hasMoreTokens()){
			// coor = 35,140/23,25 
			String coor = stoken1.nextToken();
			Node ori = new Node();
			Node dest = new Node();
			StringTokenizer stoken2 = new StringTokenizer(coor,"/");
	
			// n1&n2 = 35,140 
			String n1 = stoken2.nextToken();
			String n2 = stoken2.nextToken();
			StringTokenizer stoken3 = new StringTokenizer(n1,",");
			ori.lat = stoken3.nextToken();
			ori.lon = stoken3.nextToken();
			StringTokenizer stoken4 = new StringTokenizer(n2,",");
			dest.lat = stoken4.nextToken();
			dest.lon = stoken4.nextToken();
			
			try {
				Route route = Sender.goo(Sender.osm(ori, dest));
				routes.add(route);
				System.out.println("Generated: "+route.orig.lat+","+route.orig.lon+"/"+route.dest.lat+","+route.dest.lon);;
			} catch (IOException e) {
				System.out.println("Failed to generate route: "+coor);
			}
		}
		
		return routes;
	}
	
	
	
	public static void writeToFile(ArrayList<Route> record){
		
		try {
			FileWriter gWriter = new FileWriter("google.csv");
			FileWriter oWriter = new FileWriter("osm.csv");

			gWriter.write("google.csv\n-----\n");
			oWriter.write("osm.csv\n-----\n");

			Iterator<Route> iterator = record.iterator();
			int id = 0;
			while(iterator.hasNext()){
				id ++;
				Route curRoute = iterator.next();
				gWriter.write("route"+id+","+curRoute.orig.lat+","+curRoute.orig.lon+","+curRoute.dest.lat+","+curRoute.dest.lon+","+curRoute.gTime+"s,"+curRoute.gDis+"m\n");
				oWriter.write("route"+id+","+curRoute.orig.lat+","+curRoute.orig.lon+","+curRoute.dest.lat+","+curRoute.dest.lon+","+curRoute.oTime+"s,"+curRoute.oDis+"m\n");

				// i1 - first node, i2 - second
				Iterator<Node> i1 = curRoute.nodes.iterator();
				Iterator<Node> i2 = curRoute.nodes.iterator();
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
					Route sec = Sender.gooSimple(Sender.osmSimple(first, second));
					gWriter.write("route"+id+"."+subId+","+sec.orig.lat+","+sec.orig.lon+","+sec.dest.lat+","+sec.dest.lon+","+sec.gTime+"s,"+sec.gDis+"m\n");
					oWriter.write("route"+id+"."+subId+","+sec.orig.lat+","+sec.orig.lon+","+sec.dest.lat+","+sec.dest.lon+","+sec.oTime+"s,"+sec.oDis+"m\n");
				}
				gWriter.write("\n");
				oWriter.write("\n");
			}
			
			// Close the writers
			gWriter.close();
			oWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static double distance(Node node1, Node node2){
		double lat1 = Double.parseDouble(node1.lat);
		double lon1 = Double.parseDouble(node1.lon);
		double lat2 = Double.parseDouble(node2.lat);
		double lon2 = Double.parseDouble(node2.lon);
		
		double R = 6371e3; // meters
		double ¦Õ1 = Math.toRadians(lat1);
		double ¦Õ2 = Math.toRadians(lat2);
		double ¦¤¦Õ = Math.toRadians(lat2-lat1);
		double ¦¤¦Ë = Math.toRadians(lon2-lon1);

		double a = Math.sin(¦¤¦Õ/2) * Math.sin(¦¤¦Õ/2) +
		        Math.cos(¦Õ1) * Math.cos(¦Õ2) *
		        Math.sin(¦¤¦Ë/2) * Math.sin(¦¤¦Ë/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

		double d = R * c;
		return d;
	}
	
}
