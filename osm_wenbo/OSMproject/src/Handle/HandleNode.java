package Handle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Node;

public class HandleNode {

	static HandleNode instance;
	
	public static HandleNode getInstance() {
		
		if(instance == null) {
			instance = new HandleNode();
		}
		
		return instance;
	}
	
	
	public double getSlope(Node node1, Node node2) {
		return (node1.lat - node2.lat)/(node1.lon - node2.lon);
	}
	
	public boolean ifTurn(double k1, double k2) {
		double tan = k1-k2/1+k1*k2;
		if(tan > 0) {
			return false;
		}else {
			return true;
		}
	}
	
	public Node getNode(String id) {
		Connection con = null;
		Statement stm = null;
		
		Node node = new Node();
		node.id = id;
		
		try {
			
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:osm.db");
			stm = con.createStatement();
			String select = "SELECT * FROM nodes where id = '"+id+"'";
			ResultSet res = stm.executeQuery(select);
			String lat = res.getString("lat");
			String lon = res.getString("lon");
			node.lat = Double.parseDouble(lat);
			node.lon = Double.parseDouble(lon);
			
			stm.close();
			con.close();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return node;
		
	}
	
	

}
