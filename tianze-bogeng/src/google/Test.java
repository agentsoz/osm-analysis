package google;

import java.io.FileWriter;
import java.io.IOException;
import java.net.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.json.JSONObject;

import model.Node;
import model.Route;

public class Test {

	public static void main(String[] args) throws IOException  {

		String str = "{routes:[{ori:{lat:130.3334444,lon:2},dest:{lat:1,lon:2}}]}";
		
//		JSONObject j = new JSONObject(str);
//		System.out.println(j.getJSONArray("routes").getJSONObject(0).getJSONObject("ori").getDouble("lat"));
//		Node node1 = new Node(); node1.lat="-36.4333464"; node1.lon="148.6149967";
//		Node node2 = new Node(); node2.lat="-37.1625186"; node2.lon="145.8704504";
//		
//		Route cur = Sender.goo(Sender.osm(node1, node2));
//		Node first,second;
//		Iterator<Node> i1 = cur.nodes.iterator();
//		Iterator<Node> i2 = cur.nodes.iterator();
//		i2.next();
//		while(i2.hasNext()){
//			first = i1.next();
//			second = i2.next();
//			System.out.println(Sender.gooSimple(Sender.osmSimple(first, second)).disDif);
//		}
//		
//		a();
//		printTable();
//		printJSON();
	}		

	public static void a() throws IOException{
		Node node1 = new Node(); node1.lat="-36.4333464"; node1.lon="148.6149967";
		Node node2 = new Node(); node2.lat="-37.1625186"; node2.lon="145.8704504";

			Route cur = Sender.goo(Sender.osm(node1, node2));
			
			Node first,second;
			int i1=0,i2=1; 
			while(i2 <= 21){
				first = cur.nodes.get(i2++);
				Sender.goo(Sender.osm(first, node2));
				System.out.println(first.lat);
				if(i2 == 20)
					break;
//				second = cur.nodes.get(i2++);
//				System.out.println(Sender.genRoute(first,second).dest.lat);
			}
			
		
	}
	
public static void printTable(){	
		String url = "jdbc:sqlite:routing.db";
		
        try (Connection conn = DriverManager.getConnection(url)) {
        	try {
				Class.forName("org.sqlite.JDBC");
				Connection con1 = DriverManager.getConnection("jdbc:sqlite:routing.db");
	    		
	    		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM Comparison");
	    		System.out.printf("%-4s%-15s%-15s%-15s%-15s%-15s%-15s%-20s%-20s", "Id","Node1","Lat1","Lon1","Node2","Lat2","Lon2","OMS","Google");
	    		System.out.println();
	    		while(rs.next()){
	    			System.out.printf("%-4s%-15s%-15s%-15s%-15s%-15s%-15s%-20s%-20s",rs.getString("Id"),rs.getString("Node1"),rs.getString("Lat1"),rs.getString("Lon1"),rs.getString("Node2"),rs.getString("Lat2"),rs.getString("Lon2"),rs.getString("OSM"),rs.getString("Google"));
	    			System.out.println();
	    		}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
}

public static void printJSON() throws IOException{
	String graphkey = "7bf24aff-c48e-469f-a680-3d6fbe65388e";
	String openroutekey = "58d904a497c67e00015b45fccdf4078addb04c539c260648be859a7c";
	String graphhopperRequest = "https://graphhopper.com/api/1/route?point=-36.6735138,147.3741657&point=-34.3311298,148.7039834&key=7bf24aff-c48e-469f-a680-3d6fbe65388e";
	String openRouteRequest = "https://api.openrouteservice.org/directions?api_key=58d904a497c67e00015b45fccdf4078addb04c539c260648be859a7c&coordinates=147.3741657,-36.6735138|148.7039834,-34.3311298&profile=driving-car&preference=fastest&format=json&units=m&geometry=true&geometry_format=geojson&geometry_simplify=&instructions=true&instructions_format=text&roundabout_exits=&attributes=&maneuvers=&radiuses=&bearings=&continue_straight=&elevation=&extra_info=&optimized=true";
	URL url = new URL(graphhopperRequest);

	Scanner scan = new Scanner(url.openStream());
    String str = new String();
    while(scan.hasNext())
    	System.out.println(scan.nextLine());
    scan.close();
    System.out.println(str);
   
		
//	String graphhopperRequest = "https://graphhopper.com/api/1/route?point=-36.6735138,147.3741657&point=-34.3311298,148.7039834&instructions=false&vehicle=car&key=7bf24aff-c48e-469f-a680-3d6fbe65388e";
//	URL url = new URL(graphhopperRequest);
//
//	Scanner scan = new Scanner(url.openStream());
//    String str = new String();
//    while(scan.hasNext())
//    	str += scan.nextLine();
//    scan.close();
//    JSONObject obj = new JSONObject(str);
//    JSONObject duration = obj.getJSONArray("paths").getJSONObject(0);
//     
//    System.out.println(duration.getInt("time"));
}

}
