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


public class Sender {

	
	
	public Route genRoute(){
		while(true){
			
			Route route = null;
			try {
//				route = goo_Wp(osm_Co(ranNode(),ranNode()));
				Node node1 = new Node(); node1.lat = "-36.4333464";node1.lon ="148.6149967";
				Node node2 = new Node(); node2.lat = "-37.1625186";node2.lon = "145.8704504";
				route = goo(osm(node1,node2));
				route.format();
				return route;
			}
//			catch (ClassNotFoundException | SQLException e) {
//				e.printStackTrace();
//			}
			catch (Exception e) {
				continue;
			}
		}
	}
	
	public Route genRoute(Node node1, Node node2) throws IOException{
			Route route= goo(osm(node1,node2));
			route.format();
			
			
		return route;
	}
	
	// Request advice: Departure_time=1532959200 July 31st 2018
	// Request: Points_encoded = false
	
	// Store ori, dest, oTime, oDist into Route object and return the object.
		
	public Route osm(Node node1, Node node2) throws IOException{
			
			String key = "7bf24aff-c48e-469f-a680-3d6fbe65388e";
			String req = "https://graphhopper.com/api/1/route?point="+node1.lat+","+node1.lon+"&point="+node2.lat+","+node2.lon+"&points_encoded=false&key="+key;
			
			String res = readReq(req);

	        JSONObject json = new JSONObject(res);
			Double oDist = json.getJSONArray("paths").getJSONObject(0).getDouble("distance");
			int oTime = json.getJSONArray("paths").getJSONObject(0).getInt("time");
			
			// Store ori, dest, oTime, oDist 
			Route route = new Route(node1,node2,oTime,oDist);
			JSONArray coor = json.getJSONArray("paths").getJSONObject(0).getJSONObject("points").getJSONArray("coordinates");
			
			// Pick about 23 nodes evenly.
			route.nodes.add(route.orig);
			for(int i = coor.length()/18; i < coor.length(); i = i+coor.length()/18){
				
					JSONArray iterator = coor.getJSONArray(i);
					double lat = iterator.getDouble(1);
					double lon = iterator.getDouble(0);
					route.nodes.add(new Node(lat,lon));
			}
			route.nodes.add(route.dest);
			return route;
	}
	
	public Route osm(String lat1, String lon1, String lat2, String lon2) throws IOException{
		
		String key = "7bf24aff-c48e-469f-a680-3d6fbe65388e";
		String req = "https://graphhopper.com/api/1/route?point="+lat1+","+lon1+"&point="+lat1+","+lon2+"&points_encoded=false&key="+key;
		
		String res = readReq(req);
		//*********//
        Utilities.writeOSM(res);;
        //*********//
        JSONObject json = new JSONObject(res);
		Double oDist = json.getJSONArray("paths").getJSONObject(0).getDouble("distance");
		int oTime = json.getJSONArray("paths").getJSONObject(0).getInt("time");
		
		// Store ori, dest, oTime, oDist 
		Node node1 = new Node(); node1.lat = lat1; node1.lon = lon1;
		Node node2 = new Node(); node2.lat = lat2; node2.lon = lon2;
		Route route = new Route(node1,node2,oTime,oDist);
		JSONArray coor = json.getJSONArray("paths").getJSONObject(0).getJSONObject("points").getJSONArray("coordinates");
		
		// Pick about 20 nodes evenly.
		for(int i = coor.length()/18; i < coor.length(); i = i+coor.length()/18){
				JSONArray iterator = coor.getJSONArray(i);
				double lat = iterator.getDouble(1);
				double lon = iterator.getDouble(0);
				route.nodes.add(new Node(lat,lon));
		}
		return route;
}
	

	public Route goo(Route route) throws IOException{
			
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
	
	// Get a random Node.

	public static Node ranNode() throws ClassNotFoundException, SQLException{
		Class.forName("org.sqlite.JDBC");
		Connection con = DriverManager.getConnection("jdbc:sqlite:osm.db");
		ResultSet rs = con.createStatement().executeQuery("SELECT * FROM nodes ORDER BY RANDOM() LIMIT 1");
		rs.next();
		Node node = new Node(rs.getString("lat"),rs.getString("lon"),rs.getString("id"));
		return node;
	}
}
