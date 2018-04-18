package project;

import java.sql.*;
import project.Handler;
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Handler.createTable();
			Handler.generate100Routes();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	
	

}
