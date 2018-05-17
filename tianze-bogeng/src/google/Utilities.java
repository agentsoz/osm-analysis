package google;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import org.json.JSONObject;

import model.*;

public class Utilities {



	public static void analyseTime(ArrayList<Route> routes, double threshold){
	
		String type;
		if(threshold < 1)
			type = "per";
		else
			type = "hr";
		
		//Store the problem ones
		int id = 1;
		Iterator<Route> ite = routes.iterator();
		ArrayList<Route> record = new ArrayList<Route>();
		while(ite.hasNext()){
			Route curr = ite.next();
			if(type == "per"){
				if(curr.timeDif > threshold)
					record.add(curr);
			}
			else if(type == "hr")
				if(curr.timeDifMs > threshold*60*1000)
					record.add(curr);
			
		}
		
		//Both write them into file and print them to user's view .
		if(record.size() == 0)
		
			System.out.println("No result. Please consider changing some of your arguments.");
		
		else{
			
			System.out.println("\n-----------------------------Results that exceed threshold------------------------------\n");
			if(type == "per")
				System.out.printf("%-5s%-24s%-24s%-13s%-17s%-15s","No.","Origin","Destination","OSM(ms)","Google(ms)","Time Diff Ratio (%)");
			else if(type == "hr")
				System.out.printf("%-5s%-24s%-24s%-13s%-17s%-15s","No.","Origin","Destination","OSM(ms)","Google(ms)","Time Diff (ms)");

			System.out.println();
			for(Route route : record){
				if(type == "per")
					System.out.printf("%-5s%-24s%-24s%-13s%-17s%-15s",id++,route.orig.lat+","+route.orig.lon,route.dest.lat+","+route.dest.lon,route.oTime,route.gTime,route.timeDif*100);
				else if(type == "hr")
					System.out.printf("%-5s%-24s%-24s%-13s%-17s%-15s",id++,route.orig.lat+","+route.orig.lon,route.dest.lat+","+route.dest.lon,route.oTime,route.gTime,route.timeDifMs);

					System.out.println();
			}
			
			Scanner scan  = new Scanner(System.in);
	
			System.out.println("\nPlease enter the path where you want to store the OSM routes data (enter q to quit): ");
			String input  = scan.nextLine();
			if(input.equals("q"))
				System.exit(0);
			System.out.println("Stroing data...");
			storeData(record,input,'o');
			System.out.println("Please enter the path where you want to store the Google routes data: (enter q to quit)");
			input  = scan.nextLine();
			if(input.equals("q"))
				System.exit(1);
			System.out.println("Storing data...");
			storeData(record,input,'g');
			scan.close();
		}
	}
	
	
	public static void analyseDist(ArrayList<Route> routes, double threshold){
	
		// Tell if user wants to view difference percent or only km difference.
		String type;
		if(threshold < 1)
			type = "per";
		else
			type = "km";
		
		// Store the problem ones into ArrayList.
		int id = 1;
		Iterator<Route> ite = routes.iterator();
		ArrayList<Route> record = new ArrayList<Route>();
		while(ite.hasNext()){
			Route curr = ite.next();
			if(type == "per"){
				if(curr.disDif > threshold)
					record.add(curr);
			}
			else if(type == "km")
				if(curr.disDifM > threshold * 1000)
					record.add(curr);
		}
		
		// Print problem ones.
		if(record.size()==0)
			System.out.println("No result. Please consider changing some of your arguments.");
		else{
			System.out.println("\n-----------------------------Results that exceed threshold------------------------------\n");
			
			if(type == "per")
				System.out.printf("%-5s%-24s%-24s%-13s%-17s%-15s","No.","Origin","Destination","OSM(m)","Google(m)","Dist Diff Ratio (%)");
			else if(type == "km")
				System.out.printf("%-5s%-24s%-24s%-13s%-17s%-15s","No.","Origin","Destination","OSM(m)","Google(m)","Dist Diff (m)");

				System.out.println();
				for(Route route : record){
					if(type == "per")
						System.out.printf("%-5s%-24s%-24s%-13s%-17s%-15s",id++,route.orig.lat+","+route.orig.lon,route.dest.lat+","+route.dest.lon,route.oDis,route.gDis,route.disDif*100);
					else if(type == "km")
						System.out.printf("%-5s%-24s%-24s%-13s%-17s%-15s",id++,route.orig.lat+","+route.orig.lon,route.dest.lat+","+route.dest.lon,route.oDis,route.gDis,route.disDifM);
	
					System.out.println();
				}
				
				Scanner scan  = new Scanner(System.in);
		
				System.out.println("\nPlease enter the path where you want to store the OSM data (enter q to quit): ");
				String input  = scan.nextLine();
				if(input.equals("q"))
					System.exit(0);
				System.out.println("Stroing data...");
				storeData(record,input,'o');
				System.out.println("Please enter the path where you want to store the Google data (enter q to quit): ");
				input = scan.nextLine();
				if(input.equals("q"))
					System.exit(0);
				System.out.println("Storing data...");
				storeData(record,input,'g');
				scan.close();
			}
	}
	
	// Input format JSON: {routes:[{ori:{lat:,lon:},dest:{lat:,lon:}}]}

	public static ArrayList<Route> parseJsonCoorList(String input){
		
		Node ori = new Node();
		Node dest = new Node();
		ArrayList<Route> routes = new ArrayList<Route>();
		JSONObject list = new JSONObject(input);
		for(int i = 0; i < list.getJSONArray("routes").length(); i++){
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
				Route route = Sender.goo(Sender.osm(ori, dest));
				routes.add(route);
				System.out.println("Generated: "+route.orig.lat+","+route.orig.lon+"/"+route.dest.lat+","+route.dest.lon);;
			} catch (IOException e) {
				System.out.println("Failed to generate one route.");
			}
		}
		return routes;
	}
	
	public static void storeData(ArrayList<Route> record,String path,char website){
		
		try {
			
			FileWriter writer= new FileWriter(path);
			
			if(website == 'o')
				writer.write("google.csv\n-----\n");
			else if(website == 'g')
				writer.write("osm.csv\n-----\n");

			Iterator<Route> iterator = record.iterator();
			int id = 0;
			while(iterator.hasNext()){
				id ++;
				Route curRoute = iterator.next();
				if(website == 'g')
					writer.write("route"+id+","+curRoute.orig.lat+","+curRoute.orig.lon+","+curRoute.dest.lat+","+curRoute.dest.lon+","+curRoute.gTime+"ms,"+curRoute.gDis+"m\n");
				else if(website == 'o')
					writer.write("route"+id+","+curRoute.orig.lat+","+curRoute.orig.lon+","+curRoute.dest.lat+","+curRoute.dest.lon+","+curRoute.oTime+"ms,"+curRoute.oDis+"m\n");

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
					if(website == 'g')
						writer.write("route"+id+"."+subId+","+sec.orig.lat+","+sec.orig.lon+","+sec.dest.lat+","+sec.dest.lon+","+sec.gTime+"ms,"+sec.gDis+"m\n");
					else if(website == 'o')	
						writer.write("route"+id+"."+subId+","+sec.orig.lat+","+sec.orig.lon+","+sec.dest.lat+","+sec.dest.lon+","+sec.oTime+"ms,"+sec.oDis+"m\n");
				}
				writer.write("\n");
			}
			
			// Close the writers
			writer.close();
			System.out.println("Storing succeeds.\n");
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
