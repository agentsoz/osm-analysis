
package google;

import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormat;

import model.*;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		
		Route route;
		
		int amount = Integer.parseInt(args[0]);
		double difGap = Double.parseDouble(args[1]);
		
		System.out.printf("%-24s%-24s%-13s%-17s%-15s","Origin","Destination","OSM(m)","Google(m)","Diff Ratio");
		System.out.println();
		System.out.println();
		for(int i = 1; i <= amount; i ++){
			route = Sender.genRoute();
			if(route.disDif >= difGap){
				System.out.printf("%-24s%-24s%-13s%-17s%-15s",route.orig.lat+","+route.orig.lon,route.dest.lat+","+route.dest.lon,route.oDis,route.gDis,route.disDif);
				System.out.println();
			}
		}
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
		

	
	
	