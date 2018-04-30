package client;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import models.*;

public class testClient {
	
//	private static String WAY_FILE;
	public static String WAY_FILE = "way_test.xml";
	public static ArrayList<Way> ways;
	public static ArrayList<String> nodeIds;
	
// TODO: Make this a JUnit test instead
	public static void main(String[] args) 
	{
		XMLHandler handler;
		File file;
//		parse (args);
		try 
		{
			file = new File(WAY_FILE);
			
			SAXParserFactory spf = SAXParserFactory.newInstance(); 
			SAXParser saxParser = spf.newSAXParser();

			Scanner input = new Scanner(System.in);
			boolean mainloop = true;
			int choice;
			
			while(mainloop) 
			{
				System.out.println("\nMain Menu\n");
				System.out.print("1.) searchWayByNode\n");
				System.out.print("2.) searchSpeedByWay\n");
				System.out.print("3.) searchWayByName\n");
				System.out.print("4.) searchWayByNameWithSpeed\n");
				System.out.print("11.) searchWaysFromOneNode\n");
				choice = input.nextInt();
				// move the cursor on the next line
				input.nextLine();
				
				switch(choice) 
				{
				case 1:
					
					System.out.println("Please enter the Node ID");
					// instantiate the XMLHandler so we can make calls
					handler = new XMLHandler(1, input.nextLine());
					saxParser.parse(file, handler);
					break;
					
				case 2:
					
					System.out.println("Please enter the Way ID");
					handler = new XMLHandler(2, input.nextLine());
					saxParser.parse(file, handler);
					break;
					
				case 3:
					
					System.out.println("Please enter the name of the way");
					handler = new XMLHandler(3, input.nextLine());
					saxParser.parse(file, handler);
					break;
					
				case 4:
					
					System.out.println("Please enter the name of the way");
					handler = new XMLHandler(4, input.nextLine());
					saxParser.parse(file, handler);
					break;
					
				case 11:
						
					System.out.println("Please enter the node ID");
					handler = new XMLHandler(11, input.nextLine());
					saxParser.parse(file, handler);
//					handler = new XMLHandler(110, nodeIds);
//					saxParser.parse(file, handler);
					break;
						
					
				default:
					System.out.println("Not a valid option!");
					break;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	/**
	 * Parse args[] here and set variables from commandline options
	 * For instance, --way-file FILE could be used to set the var WAY_FILE to some
	 * user specified value.
	 * */
	private static void parse(String[] args) 
	{
		WAY_FILE = args[0];  
	}
}
