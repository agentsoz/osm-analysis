package handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import models.Node;

public class HandleNode {

	static HandleNode instance;
	
	public static HandleNode getInstance() {
		
		if(instance == null) {
			instance = new HandleNode();
		}
		
		return instance;
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
		
			e.printStackTrace();
		}
		
		return node;	
	}
}
