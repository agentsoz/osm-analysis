
package osm.analysis.google;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import osm.analysis.model.*;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		
		
		if (validation(args) == true){
			parse(args);
		}
	}
	
	public static boolean validation(String args[]){
		// Both --list-of-origin/dest and --generate-random
		int val = 0;
		for(String s : args){
			if (s.equals("--list-of-origin/dest"))
				val++;
			if(s.equals("--generate-random-origins"))
				val++;
		}
		if(val == 2)
			return false;
		
		return true;
	}
	
	public static void parse(String args[]){
		int num;
		double radius = 0;
		double distPercentDiff = 0;
		double kmDiff = 0;
		double timePercentDiff = 0;
		double minDiff = 0;
		boolean distCompare = false;
		boolean timeCompare = false;
		
		// Choice 1: Random routes (with specific radius).
		if(args[0].equals("--generate-random-origins")){
			
			// Get num, radius, percent, diff 's value
			num = Integer.parseInt(args[1]);
			for(int i = 2; i < args.length; i++){
				if(args[i].equals("--radius-of-dest"))
					radius = Double.parseDouble(args[i+1])*1000;
				if(args[i].equals("--dist-diff-reporting-threshold-percent")){
					distPercentDiff = Double.parseDouble(args[i+1])/100;
					distCompare = true;
				}
				if(args[i].equals("--dist-diff-reporting-threshold-km")){
					kmDiff = Double.parseDouble(args[i+1]);
					distCompare = true;
				}
				if(args[i].equals("--time-diff-reporting-threshold-percent")){
					timePercentDiff = Double.parseDouble(args[i+1]);
					timeCompare = true;
				}
				if(args[i].equals("--time-diff-reporting-threshold-hr")){
					minDiff = Double.parseDouble(args[i+1])*60;
					timeCompare = true;
				}
			}
			
			// Generate and store all random routes
			ArrayList<Route> routes = new ArrayList<Route>();
			for(int i = 0; i < num; i++){
				Route route;
				route = Sender.ranRoute(radius);
				routes.add(route);
			}
			
			//Analyse ALL routes
			if(distCompare == true){
				//Default 20%
				if(distPercentDiff == 0 && kmDiff == 0)
					Utilities.analyseDist(routes, 0.2);
				else if(distPercentDiff != 0)
					Utilities.analyseDist(routes, distPercentDiff);
				else
					Utilities.analyseDist(routes, kmDiff);
			}
			else if(timeCompare == true){
				if(timePercentDiff == 0 && minDiff == 0)
					Utilities.analyseTime(routes, 0.2);
				else if(timePercentDiff != 0)
					Utilities.analyseTime(routes, timePercentDiff);
				else
					Utilities.analyseTime(routes, minDiff);
			}
		}
	
		
		//Choice 2: User input a list of ori/dest
		else if(args[0].equals("--list-of-origin/dest")){
			ArrayList<Route> routes = Utilities.parseJsonCoorList(args[1]);
			
			for(int i = 2; i < args.length; i++){
				if(args[i].equals("--dist-diff-reporting-threshold-percent")){
					distPercentDiff = Double.parseDouble(args[i+1])/100;
					distCompare = true;
				}
				if(args[i].equals("--dist-diff-reporting-threshold-km")){
					kmDiff = Double.parseDouble(args[i+1]);
					distCompare = true;
				}
				if(args[i].equals("--time-diff-reporting-threshold-percent")){
					timePercentDiff = Double.parseDouble(args[i+1])/100;
					timeCompare = true;
				}
				if(args[i].equals("--time-diff-reporting-threshold-hr")){
					minDiff = Double.parseDouble(args[i+1])*60;
					timeCompare = true;
				}
			}
			
			//Analyse ALL routes
			if(distCompare == true){
				//Default 20%
				if(distPercentDiff == 0 && kmDiff == 0)
					Utilities.analyseDist(routes, 0.2);
				else if(distPercentDiff != 0)
					Utilities.analyseDist(routes, distPercentDiff);
				else
					Utilities.analyseDist(routes, kmDiff);
			}
			else if(timeCompare == true){
				if(timePercentDiff == 0 && minDiff == 0)
					Utilities.analyseTime(routes, 0.2);
				else if(timePercentDiff != 0)
					Utilities.analyseTime(routes, timePercentDiff);
				else
					Utilities.analyseTime(routes, minDiff);
			}
		}
	}
}
	


	
	
	