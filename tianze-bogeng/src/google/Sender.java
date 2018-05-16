package google;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import model.Node;
import model.Route;

//https://maps.googleapis.com/maps/api/directions/json?origin=-37.170539,149.0752711&destination=-35.5213982,144.0558473&key=AIzaSyD1nCcuJA3fw9gGmAOsRVqpaxpxWUxEH2I
//https://graphhopper.com/api/1/route?point=-37.170539,149.0752711&point=-35.5213982,144.0558473&points_encoded=true&key=7bf24aff-c48e-469f-a680-3d6fbe65388e

public class Sender {

	public static Route ranRoute(double radius){
		if(radius == 0)
			return ranRoute();
		//Random route with origin and radius specified
		else{
			Node ori;
			Node dest;
			
			while(true){
				try{
					while(true){
						ori = ranNode();
						dest = ranNode();
						if(Utilities.distance(ori,dest) <= radius){
							break;
						}
					}
					// Generate route
					Route route = goo(osm(ori,dest));
					System.out.println("Generated: "+route.orig.lat+","+route.orig.lon+"/"+route.dest.lat+","+route.dest.lon);

					return route;
				}catch(IOException e){
					continue;
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
	
	//Totally random route
	public static Route ranRoute(){
		while(true){
			
			Route route = null;
			try {
				route = goo(osm(ranNode(),ranNode()));
				return route;
			}
			catch (IOException e) {
				continue;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
	}
	
	public static Route genRoute(Node node1, Node node2){
			Route route;
			try {
				route = goo(osm(node1,node2));
				return route;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Route generation failed.");
			}
			return null;
	}
	
	// Request advice: Departure_time=1532959200 July 31st 2018
	// Request: Points_encoded = false
	
	// Store ori, dest, oTime, oDist into Route object and return the object.
		
	public static Route osm(Node node1, Node node2) throws IOException{
			
			String key = "6b8e1ba3-dcfd-46a1-872a-cce6c73a6831";
			String req = "https://graphhopper.com/api/1/route?point="+node1.lat+","+node1.lon+"&point="+node2.lat+","+node2.lon+"&points_encoded=false&key="+key;
			
			String res = readReq(req);

	        JSONObject json = new JSONObject(res);
			Double oDist = json.getJSONArray("paths").getJSONObject(0).getDouble("distance");
			int oTime = json.getJSONArray("paths").getJSONObject(0).getInt("time");
			
			// Store ori, dest, oTime, oDist 
			Route route = new Route(node1,node2,oTime,oDist);
			JSONArray coor = json.getJSONArray("paths").getJSONObject(0).getJSONObject("points").getJSONArray("coordinates");
			
			// Pick 20 nodes evenly.
			route.nodes.add(route.orig);
			for(int i = coor.length()/19; i < coor.length(); i += coor.length()/19){
				
					JSONArray iterator = coor.getJSONArray(i);
					double lat = iterator.getDouble(1);
					double lon = iterator.getDouble(0);
					route.nodes.add(new Node(lat,lon));
			}
			route.nodes.add(route.dest);
			return route;
	}
	
	//Only get oTime and oDis, without filling the nodes ArrayList of Route object
	public static Route osmSimple(Node node1, Node node2) throws IOException{
		String key = "6b8e1ba3-dcfd-46a1-872a-cce6c73a6831";
		String req = "https://graphhopper.com/api/1/route?point="+node1.lat+","+node1.lon+"&point="+node2.lat+","+node2.lon+"&points_encoded=false&key="+key;
		
		String res = readReq(req);

        JSONObject json = new JSONObject(res);
		Double oDist = json.getJSONArray("paths").getJSONObject(0).getDouble("distance");
		int oTime = json.getJSONArray("paths").getJSONObject(0).getInt("time");
		Route route = new Route(node1,node2,oTime,oDist);
	
		return route;
	}
	
	public static Route osm(String lat1, String lon1, String lat2, String lon2) throws IOException{
		
		String key = "6b8e1ba3-dcfd-46a1-872a-cce6c73a6831";
		String req = "https://graphhopper.com/api/1/route?point="+lat1+","+lon1+"&point="+lat1+","+lon2+"&points_encoded=false&key="+key;
		
		String res = readReq(req);
	
        JSONObject json = new JSONObject(res);
		Double oDist = json.getJSONArray("paths").getJSONObject(0).getDouble("distance");
		int oTime = json.getJSONArray("paths").getJSONObject(0).getInt("time");
		
		// Store ori, dest, oTime, oDist 
		Node node1 = new Node(); node1.lat = lat1; node1.lon = lon1;
		Node node2 = new Node(); node2.lat = lat2; node2.lon = lon2;
		Route route = new Route(node1,node2,oTime,oDist);
		JSONArray coor = json.getJSONArray("paths").getJSONObject(0).getJSONObject("points").getJSONArray("coordinates");
		
		// Pick about 20 nodes evenly.
		for(int i = coor.length()/19; i < coor.length(); i = i+coor.length()/19){
				JSONArray iterator = coor.getJSONArray(i);
				double lat = iterator.getDouble(1);
				double lon = iterator.getDouble(0);
				route.nodes.add(new Node(lat,lon));
		}
		return route;
}
	
	// Only get gTime and gDis without specifying waypoints.
	public static Route gooSimple(Route route) throws IOException{
		String key = "AIzaSyD1nCcuJA3fw9gGmAOsRVqpaxpxWUxEH2I";
		String req = "https://maps.googleapis.com/maps/api/directions/json?origin="+route.orig.lat+","+route.orig.lon+"&destination="+route.dest.lat+","+route.dest.lon+"&departure_time=1532966400&key=" + key;
		String res = readReq(req);
		JSONObject json = new JSONObject(res);
		JSONArray legs = json.getJSONArray("routes").getJSONObject(0).getJSONArray("legs");
		int time = legs.getJSONObject(0).getJSONObject("duration").getInt("value");
		int dis = legs.getJSONObject(0).getJSONObject("distance").getInt("value");
		route.gTime = time*1000;
		route.gDis = dis;
		route.format();
		return route;
	}
	
	public static Route goo(Route route) throws IOException{
			
			String key = "AIzaSyD1nCcuJA3fw9gGmAOsRVqpaxpxWUxEH2I";
			String req = "https://maps.googleapis.com/maps/api/directions/json?origin="+route.orig.lat+","+route.orig.lon+"&destination="+route.dest.lat+","+route.dest.lon+"&departure_time=1532966400&waypoints=";
			
			// Insert all nodes into request.
			req += (route.nodes.get(0).lat + "," + route.nodes.get(0).lon);
			for(int i = 1; i < route.nodes.size(); i ++){
				req += ("|" + route.nodes.get(i).lat + "," + route.nodes.get(i).lon);
			}
			
			req += ("&key=" + key);
			String res = readReq(req);
			JSONObject json = new JSONObject(res);
			int time = 0;
			JSONArray legs = json.getJSONArray("routes").getJSONObject(0).getJSONArray("legs");
			for (int i = 0; i < legs.length(); i ++){
				time += legs.getJSONObject(i).getJSONObject("duration").getInt("value");
			}
			
			time *= 1000;
			route.gTime = time;
			
			int dis = 0;
			for (int i = 0; i < legs.length(); i ++){
				dis += legs.getJSONObject(i).getJSONObject("distance").getInt("value");
			}
			
			route.gDis = dis;
			route.format();
			return route;
	}
	
	public static String readReq(String req) throws IOException{
		
		String res = new String();
		
		try {
			
			URL url = new URL(req);
			Scanner scan = new Scanner(url.openStream());
	        
	        while(scan.hasNext())
	        	res += scan.nextLine();
	        scan.close();
	        
		} catch (MalformedURLException e) {
			
			e.printStackTrace();
			
		}
		
		return res;
	}
	
	public static Node ranNode() throws ClassNotFoundException, SQLException{
		Class.forName("org.sqlite.JDBC");
		Connection con = DriverManager.getConnection("jdbc:sqlite:osm.db");
		ResultSet rs = con.createStatement().executeQuery("SELECT * FROM nodes ORDER BY RANDOM() LIMIT 1");
		rs.next();
		Node node = new Node(rs.getString("lat"),rs.getString("lon"),rs.getString("id"));
		return node;
	}
}
