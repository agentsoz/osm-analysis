package google;
import java.sql.*;
import java.text.DecimalFormat;
import java.io.IOException;

import java.net.URL;
import java.util.Scanner;

import org.json.JSONObject;

public class Handler {

	public static void getStreetName(String lat1, String lon1, String lat2, String lon2) throws Exception{
		String key = "AIzaSyD1nCcuJA3fw9gGmAOsRVqpaxpxWUxEH2I";
		String request = "https://maps.googleapis.com/maps/api/directions/json?origin="+lat1+","+lon1+"&destination="+lat2+","+lon2+"&key="+key;
		URL url = new URL(request);
		
			Scanner scan = new Scanner(url.openStream());
	        String str = new String();
	        while(scan.hasNext())
	        	str += scan.nextLine();
	        scan.close();
	        JSONObject obj = new JSONObject(str);
	        JSONObject duration = obj.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("duration");
	       
		
	}
	
	
	public void createTable() throws ClassNotFoundException, SQLException{
		Class.forName("org.sqlite.JDBC");
		Connection con = DriverManager.getConnection("jdbc:sqlite:routing.db");
		Statement statement = con.createStatement();
		String query = "CREATE TABLE Comparison(Id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "Node1 TEXT,"
				+ "Lat1 TEXT,"
				+ "Lon1 TEXT,"
				+ "Node2 TEXT,"
				+ "Lat2 TEXT,"
				+ "Lon2 TEXT,"
				+ "OSM TEXT,"
				+ "Google TEXT,"
				+ "Diff TEXT)";
		statement.executeUpdate(query);
		con.close();
	}

	// Randomly generate 100 pairs of nodes into database
	public void generate100Routes () throws Exception{
		Class.forName("org.sqlite.JDBC");
		Connection con1 = DriverManager.getConnection("jdbc:sqlite:osm.db");
		Connection con2 = DriverManager.getConnection("jdbc:sqlite:routing.db");
		PreparedStatement ps;
		// Get RAMDOM nodes with method RAND()
		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM nodes ORDER BY RANDOM() LIMIT 200");
		System.out.println("Start generating...");
		while(rs.next()){
			String id1,lat1,lon1,id2,lat2,lon2;
			id1 = rs.getString("id");
			lat1 = rs.getString("lat");
			lon1 = rs.getString("lon");
			rs.next();
			id2 = rs.getString("id");
			lat2 = rs.getString("lat");
			lon2 = rs.getString("lon");
			// Store 2 nodes (1 pair) into each row.
			ps = con2.prepareStatement("INSERT INTO Comparison values(?,?,?,?,?,?,?,?,?,?)");
			ps.setString(2, id1);
			ps.setString(3, lat1);
			ps.setString(4, lon1);
			ps.setString(5, id2);
			ps.setString(6, lat2);
			ps.setString(7, lon2);
			int timeGraphopper = sendRequestToGraphhopper(setDecimalTo3(lat1),setDecimalTo3(lon1),setDecimalTo3(lat2),setDecimalTo3(lon2));
			ps.setString(8, msToHour(timeGraphopper));
			String timeGoogle = sendRequestToGoogle(setDecimalTo3(lat1),setDecimalTo3(lon1),setDecimalTo3(lat2),setDecimalTo3(lon2));
			ps.setString(9, timeGoogle);
			ps.execute();
		}
		System.out.println("Generation finished.");
		con1.close();
		con2.close();
	}
	
	public String setDecimalTo3(String s){
		double d = Double.parseDouble(s);
		DecimalFormat df = new DecimalFormat("0.000");
		df.format(d);
		return Double.toString(d);
	}
	
	public String msToHour(int ms){
		double d1 = (double)ms/3600000;
		int hr = (int)d1;
		double d2 = (d1 - hr) * 60;
		int min = (int)d2;
		if(hr == 0){
			if(min == 0)
				return "0";
			else
			return min+" mins";
		}
		else if(hr == 1)
			return hr+" hour "+min+" mins";
		else
			return hr+" hours "+min+" mins";
	}
	
	public String sendRequestToGoogle(String lat1, String lon1, String lat2, String lon2) throws Exception{
		String key = "AIzaSyD1nCcuJA3fw9gGmAOsRVqpaxpxWUxEH2I";
		String request = "https://maps.googleapis.com/maps/api/directions/json?origin="+lat1+","+lon1+"&destination="+lat2+","+lon2+"&key="+key;
		URL url = new URL(request);
		try{
			Scanner scan = new Scanner(url.openStream());
	        String str = new String();
	        while(scan.hasNext())
	        	str += scan.nextLine();
	        scan.close();
	        JSONObject obj = new JSONObject(str);
	        JSONObject duration = obj.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("duration");
	        return duration.getString("text");
		} catch(Exception e){ 
			return null;
		}
	}
     
	public int sendRequestToGraphhopper(String lat1, String lon1, String lat2, String lon2) throws IOException{
		
		String key = "7bf24aff-c48e-469f-a680-3d6fbe65388e";
		String request = "https://graphhopper.com/api/1/route?point="+lat1+","+lon1+"&point="+lat2+","+lon2+"&instructions=false&vehicle=car&key="+key;
		URL url = new URL(request);
		try{
			Scanner scan = new Scanner(url.openStream());
	        String str = new String();
	        while(scan.hasNext())
	        	str += scan.nextLine();
	        scan.close();
	        JSONObject obj = new JSONObject(str);
	        JSONObject duration = obj.getJSONArray("paths").getJSONObject(0);
	        return duration.getInt("time");
		}catch(Exception e){
			return 0;
		}
        
	}
	

	

	
	
	
}
