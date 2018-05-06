package google;

import java.io.IOException;
import java.net.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.Scanner;

import org.json.JSONObject;

public class Test {

	public static void main(String[] args) throws IOException {
		oneRoute();
//		printTable();
//		printJSON();
	}		

	public static void oneRoute(){
		Sender.genRoute();
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
