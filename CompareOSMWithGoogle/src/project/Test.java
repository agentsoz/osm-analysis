package project;

import java.io.IOException;
import java.net.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.Scanner;

import org.json.JSONObject;

public class Test {

	public static void main(String[] args) throws IOException {
		
		printTable();
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

public static void printJSON(){
	
//	String graphhopperRequest = "https://maps.googleapis.com/maps/api/directions/json?origin=-36.6735138,147.3741657&destination=-34.3311298,148.7039834&key=AIzaSyD1nCcuJA3fw9gGmAOsRVqpaxpxWUxEH2I";
//	URL url = new URL(graphhopperRequest);
//
//	Scanner scan = new Scanner(url.openStream());
//    String str = new String();
//    while(scan.hasNext())
//    	System.out.println(scan.nextLine());
//    scan.close();
   
		
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
