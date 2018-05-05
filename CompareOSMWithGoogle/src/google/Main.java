
package google;

import java.io.IOException;
import java.sql.*;
import model.*;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		// TODO Auto-generated method stub
		WaypointsHandler wh = new WaypointsHandler();
		int number = Integer.parseInt(args[0]);
		double diff = Double.parseDouble(args[1]);
		Waypoints waypoints;
		System.out.printf("%-24s%-24s%-13s%-17s%-15s","Origin","Destination","OSM(ms)","Google(ms)","Diff Ratio");
		System.out.println();
		System.out.println();
		for(int i = 1; i <= number; i ++){
			waypoints = wh.generateWaypoints();
			if(waypoints.timeDiff > diff){
				System.out.printf("%-12s%-12s%-12s%-12s%-13s%-17s%-15s",waypoints.origin.lat,waypoints.origin.lon,waypoints.destination.lat,waypoints.destination.lon,waypoints.assumedTime,waypoints.actualTime,waypoints.timeDiff);
				System.out.println();
			}
		}
	}
}
		

	
	
	