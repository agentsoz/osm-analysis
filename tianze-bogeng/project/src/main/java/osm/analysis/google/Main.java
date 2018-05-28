
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
		
		
		while (validation(args) == true){
			parse(args);
			return;
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
		double distDiffPer = -1;
		double timeDiffKm = -1;
		double timeDiffPer = -1;
		double timeDiffMs = -1;
		boolean distCompare = false;
		boolean timeCompare = false;
		String summaryPath = null;
		String detailPath = null;
		ArrayList<Route> routes = new ArrayList<Route>();
		for(int i = 0; i < args.length; i++){
			if(args[i].equals("--summary-store-path"))
				summaryPath = args[i+1];
			if(args[i].equals("--detail-store-path"))
				detailPath = args[i+1];
			if(args[i].equals("--list-of-orig/dest")){
				
				 routes = Utilities.parseCoorList(args[i+1]);
			}
			else if(args[i].equals("--generate-random-origins")){
				num = Integer.parseInt(args[i+1]);
				for(int a = 0; a < num; a++){
					Route route = Sender.ranRoute(radius);
					routes.add(route);
				}
			}
			if(args[i].equals("--radius-of-dest"))
				radius = Double.parseDouble(args[i+1])*1000;
			if(args[i].equals("--dist-diff-reporting-threshold-percent")){
				distDiffPer = Double.parseDouble(args[i+1])/100;
				distCompare = true;
			}
			if(args[i].equals("--dist-diff-reporting-threshold-km")){
				timeDiffKm = Double.parseDouble(args[i+1]);
				distCompare = true;
			}
			if(args[i].equals("--time-diff-reporting-threshold-percent")){
				timeDiffPer = Double.parseDouble(args[i+1])/100;
				timeCompare = true;
			}
			if(args[i].equals("--time-diff-reporting-threshold-hr")){
				timeDiffMs = Double.parseDouble(args[i+1])*60*60*1000;
				timeCompare = true;
			}
		}
		
		ArrayList<Route> record = new ArrayList<Route>();
		//Analyse ALL routes
		if(distCompare == true){
			//Default 20%
			if(distDiffPer == -1 && timeDiffKm == -1)
				record = Utilities.dFilter(routes, 0.2);
			else if(distDiffPer != -1)
				record = Utilities.dFilter(routes, distDiffPer);
			else
				record = Utilities.dFilter(routes, timeDiffKm);
		}
		else if(timeCompare == true){
			if(timeDiffPer == -1 && timeDiffMs == -1)
				record = Utilities.tFilter(routes, 0.2);
			else if(timeDiffPer != -1)
				record = Utilities.tFilter(routes, timeDiffPer);
			else
				record = Utilities.tFilter(routes, timeDiffMs);
		}
		
		Utilities.printSummary(record);
		if(summaryPath != null)
			Utilities.storeSummary(record,summaryPath);
		if(detailPath != null)
			Utilities.storeDetail(record, detailPath);

	}
}
	


	
	
	