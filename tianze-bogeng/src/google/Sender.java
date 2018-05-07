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

	
	
	public static Route genRoute(){
		while(true){
			Route route = null;
			try {
				route = goo_Wp(osm_Co(ranNode(),ranNode()));
				int big;
				// Store time diff
				if(route.oTime > route.gTime)
					big = route.oTime;
				else
					big = route.gTime;
				DecimalFormat df = new DecimalFormat("0.0000");
				route.timeDif = Double.parseDouble(df.format(Math.abs((route.gTime - route.oTime)/(double)big)));
				
				// Store distance diff
				Double big2 ;
				if(route.oDis > route.gDis)
					big2 =  route.oDis;
				
				else
					big2 = route.gDis;
				
				route.disDif = Double.parseDouble(df.format(Math.abs((route.gDis - route.oDis)/big2)));
				
				return route;
				
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				continue;
			}
			
		}
	}
	
	// Request advice: Departure_time=1532959200 July 31st 2018
	// Request: Points_encoded = false
	
	// Store ori, dest, oTime, oDist into Route object and return the object.
		
	public static Route osm_Co(Node node1, Node node2) throws IOException{
			
			String key = "7bf24aff-c48e-469f-a680-3d6fbe65388e";
			String req = "https://graphhopper.com/api/1/route?point="+node1.lat+","+node1.lon+"&point="+node2.lat+","+node2.lon+"&points_encoded=false&key="+key;
			
			String res = readReq(req);
			//*********//
	        Utilities.writeOSM(res);;
	        //*********//
	        JSONObject json = new JSONObject(res);
			Double oDist = json.getJSONArray("paths").getJSONObject(0).getDouble("distance");
			int oTime = json.getJSONArray("paths").getJSONObject(0).getInt("time");
			
			// Store ori, dest, oTime, oDist 
			Route route = new Route(node1,node2,oTime,oDist);
			JSONArray coor = json.getJSONArray("paths").getJSONObject(0).getJSONObject("points").getJSONArray("coordinates");
			
			// Pick about 50 nodes evenly.
			for(int i = coor.length()/23; i < coor.length(); i = i+coor.length()/23){
				
					JSONArray iterator = coor.getJSONArray(i);
					double lat = iterator.getDouble(1);
					double lon = iterator.getDouble(0);
					route.nodes.add(new Node(lat,lon));
			}
			return route;
	}
	
	// Return time (ms)
	public static Route goo_Wp(Route route) throws IOException{
			
			String key = "AIzaSyD1nCcuJA3fw9gGmAOsRVqpaxpxWUxEH2I";
			String req = "https://maps.googleapis.com/maps/api/directions/json?origin="+route.orig.lat+","+route.orig.lon+"&destination="+route.dest.lat+","+route.dest.lon+"&waypoints=";
			
			// Insert all nodes into request.
			req += (route.nodes.get(0).lat + "," + route.nodes.get(0).lon);
			for(int i = 1; i < route.nodes.size(); i ++){
				req += ("|" + route.nodes.get(i).lat + "," + route.nodes.get(i).lon);
			}
			
			req += ("&key=" + key);
			
			String res = readReq(req);
			
			//*********//
	        Utilities.writeGoo(res);;
	        //*********//
	        
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
	// Question: Do we pick nodes TOTALLY randomly? Region / distance restriction.
	public static Node ranNode() throws ClassNotFoundException, SQLException{
		Class.forName("org.sqlite.JDBC");
		Connection con = DriverManager.getConnection("jdbc:sqlite:osm.db");
		ResultSet rs = con.createStatement().executeQuery("SELECT * FROM nodes ORDER BY RANDOM() LIMIT 1");
		rs.next();
		Node node = new Node(rs.getString("lat"),rs.getString("lon"),rs.getString("id"));
		return node;
	}
}
