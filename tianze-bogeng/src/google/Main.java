
package google;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.*;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		
		int amount = Integer.parseInt(args[0]);
		double difGap = Double.parseDouble(args[1]);
		
		presentation(amount,difGap);
		
	}
	
	public static void presentation(int amount, double difGap){
		Sender sender = new Sender();
		Handler handler = new Handler();
		List<Route> record = new ArrayList<Route>();
		Route route;
		System.out.printf("%-5s%-24s%-24s%-13s%-17s%-15s","No.","Origin","Destination","OSM(m)","Google(m)","Diff Ratio");
		System.out.println();
		System.out.println();
		int no = 0;
		for(int i = 1; i <= amount; i ++){
			route = sender.ranRoute();
			if(route.disDif >= difGap){
				no ++;
				record.add(route);
				System.out.printf("%-5s%-24s%-24s%-13s%-17s%-15s",no,route.orig.lat+","+route.orig.lon,route.dest.lat+","+route.dest.lon,route.oDis,route.gDis,route.disDif);
				System.out.println();
			}
		}
		
		System.out.println();
		System.out.println("Please enter the route number to analysis it:");
		Scanner scan = new Scanner(System.in);
		Route choice = record.get(scan.nextInt());
		try {
			handler.analyse(choice);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
		

	
	
	