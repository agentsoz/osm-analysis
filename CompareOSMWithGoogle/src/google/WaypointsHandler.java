
package google;
import model.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.Scanner;

import org.json.*;
public class WaypointsHandler {

	
	
	public WaypointsHandler(){
		
	}
	
	public Waypoints generateWaypoints() throws ClassNotFoundException, SQLException, IOException{
		while(true){
			try{
				Node origin = this.randomNode();
				Node destination = this.randomNode();
				Waypoints waypoints = this.requestOSMGeocodedFalse(origin, destination);
				int actualTime = this.waypointsDuration(this.requestGoogleWaypoints(waypoints));
				waypoints.actualTime = actualTime;
				DecimalFormat df = new DecimalFormat("0.000");
				waypoints.timeDiff = Double.parseDouble(df.format(Math.abs((waypoints.actualTime - waypoints.assumedTime)/(double)waypoints.assumedTime)));
				
				return waypoints;
			}catch (IOException e){
				continue;
			}
		}
	}
	
	// Get a random Node.
	// Question: Do we pick nodes TOTALLY randomly? Region / distance restriction.
	public Node randomNode() throws ClassNotFoundException, SQLException{
		Class.forName("org.sqlite.JDBC");
		Connection con = DriverManager.getConnection("jdbc:sqlite:osm.db");
		ResultSet rs = con.createStatement().executeQuery("SELECT * FROM nodes ORDER BY RANDOM() LIMIT 1");
		rs.next();
		Node node = new Node(rs.getString("lat"),rs.getString("lon"),rs.getString("id"));
		return node;
	}
	

	
	//departure_time=1532959200 July 31st 2018
	public Waypoints requestOSMGeocodedFalse(Node node1, Node node2) throws IOException{
		String key = "7bf24aff-c48e-469f-a680-3d6fbe65388e";
		String request = "https://graphhopper.com/api/1/route?point="+node1.lat+","+node1.lon+"&point="+node2.lat+","+node2.lon+"&points_encoded=false&key="+key;
		
		URL url = new URL(request);
		Scanner scan;
		
		scan = new Scanner(url.openStream());
		
		String response = new String();
        
        while(scan.hasNext())
        	response += scan.nextLine();
        scan.close();
        JSONObject json = new JSONObject(response);

		Double distance = json.getJSONArray("paths").getJSONObject(0).getDouble("distance");
		int assumedTime = json.getJSONArray("paths").getJSONObject(0).getInt("time");
		
		Waypoints waypoints = new Waypoints(assumedTime,distance,node1,node2);
		JSONArray coordinates = json.getJSONArray("paths").getJSONObject(0).getJSONObject("points").getJSONArray("coordinates");
		
		// Evenly pick 20 nodes from hundreds of waypoints.
		for(int i = coordinates.length()/20; i < coordinates.length(); i = i+coordinates.length()/20){
			
				JSONArray iterator = coordinates.getJSONArray(i);
				double lat = iterator.getDouble(1);
				double lon = iterator.getDouble(0);
				waypoints.nodes.add(new Node(lat,lon));
			
		}
		return waypoints;
			
		
        
	}
	
	public String requestGoogleWaypoints(Waypoints waypoints) throws IOException{
		
		String key = "AIzaSyD1nCcuJA3fw9gGmAOsRVqpaxpxWUxEH2I";
		String request = "https://maps.googleapis.com/maps/api/directions/json?origin="+waypoints.origin.lat+","+waypoints.origin.lon+"&destination="+waypoints.destination.lat+","+waypoints.destination.lon+"&waypoints=";
		
		request += (waypoints.nodes.get(0).lat + "," + waypoints.nodes.get(0).lon);
		
		for(int i = 1; i < waypoints.nodes.size(); i ++){
			request += ("|" + waypoints.nodes.get(i).lat + "," + waypoints.nodes.get(i).lon);
		}
		
		request += ("&key=" + key);
		
		URL url = new URL(request);
		Scanner scan = new Scanner(url.openStream());
        String response = new String();
        while(scan.hasNext())
        	response += scan.nextLine();
        scan.close();
        
        return response;	
	}
	
	
	// Unit: ms
	public int waypointsDuration(String response){
		JSONObject json = new JSONObject(response);
		int time = 0;
		JSONArray legs = json.getJSONArray("routes").getJSONObject(0).getJSONArray("legs");
		for (int i = 0; i < legs.length(); i ++){
			time += legs.getJSONObject(i).getJSONObject("duration").getInt("value");
		}
		
		time = time * 1000;
		return time;
	}
	
//	public Waypoints extractStreetNames(String response){
//		
//		JSONObject json = new JSONObject(response);
//		JSONArray instructions = json.getJSONArray("paths").getJSONObject(0).getJSONArray("instructions");
//
//		String distance = json.getJSONArray("paths").getJSONObject(0).getString("distance");
//		String time = json.getJSONArray("paths").getJSONObject(0).getString("time");
//		
//		Waypoints waypoints = new Waypoints(time,distance);
//		
//		for(int i = 0; i < instructions.length(); i ++){
//			try{
//				JSONObject temp = instructions.getJSONObject(i);
//				waypoints.list.add(new Way(temp.getString("street_name"),temp.getString("time"),temp.getString("distance")));
//			}
//			catch(JSONException e){
//				break;
//			}
//		}
//		return waypoints;
//	}
	
//public Waypoints extractWayPointsCoor(String response){
//		
//		JSONObject json = new JSONObject(response);
//		JSONArray instructions = json.getJSONArray("paths").getJSONObject(0).getJSONArray("instructions");
//
//		String distance = json.getJSONArray("paths").getJSONObject(0).getString("distance");
//		String time = json.getJSONArray("paths").getJSONObject(0).getString("time");
//		
//		Waypoints waypoints = new Waypoints(time,distance);
//		
//		for(int i = 0; i < instructions.length(); i ++){
//			try{
//				JSONObject temp = instructions.getJSONObject(i);
//				waypoints.nodeList.add(new Way(temp.getString("street_name"),temp.getString("time"),temp.getString("distance")));
//			}
//			catch(JSONException e){
//				break;
//			}
//		}
//		return waypoints;
//	}
	
}