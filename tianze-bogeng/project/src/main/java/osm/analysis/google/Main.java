
package osm.analysis.google;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import osm.analysis.model.Route;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		System.out.println();
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

		for(int i = 0; i < args.length; i++){
			if(args[0].equals(null) || args[0].equals("-help") || args[0].equals("-h"))
				Utilities.printHelp();
			if(args[i].equals("--osm-read-path"))
				DatabaseHandler.osmPath = args[i+1];
			if(args[i].equals("--db-store-path")){
				DatabaseHandler.dbPath = args[i+1];
				DatabaseHandler.generate();
			}
			if(args[i].equals("--list-of-orig/dest")){
				 Utilities.inputRoutes = Utilities.parseCoorList(args[i+1]);
			}
			if(args[i].equals("--generate-random-origins")){
				Utilities.num = Integer.parseInt(args[i+1]);
				Utilities.random = true;
			}
			if(args[i].equals("--db-read-path"))
				Utilities.dbPath = args[i+1];
			if(args[i].equals("--radius-of-dest"))
				Utilities.radius = Double.parseDouble(args[i+1]);
			if(args[i].equals("--dist-diff-reporting-threshold-percent")){
				Utilities.distDiffPer = Double.parseDouble(args[i+1]);
				Utilities.distCompare = true;
			}
			if(args[i].equals("--dist-diff-reporting-threshold-km")){
				Utilities.timeDiffKm = Double.parseDouble(args[i+1]);
				Utilities.distCompare = true;
			}
			if(args[i].equals("--time-diff-reporting-threshold-percent")){
				Utilities.timeDiffPer = Double.parseDouble(args[i+1]);
				Utilities.timeCompare = true;
			}
			if(args[i].equals("--time-diff-reporting-threshold-hr")){
				Utilities.timeDiffHr = Double.parseDouble(args[i+1]);
				Utilities.timeCompare = true;
			}
			if(args[i].equals("--summary-store-path"))
				Utilities.summaryPath = args[i+1];
			if(args[i].equals("--input-summary-file"))
				Utilities.inputSummaryPath = args[i+1];
			if(args[i].equals("--detail-store-path")){
				Utilities.detailPath = args[i+1];
			}
			if(args[i].equals("--analyze-route-id")){
				Utilities.idList = Utilities.parseIdList(args[i+1]);
				Utilities.detail = true;
			}
			
		}
		Utilities.run();
		
	}
}
	


	
	
	