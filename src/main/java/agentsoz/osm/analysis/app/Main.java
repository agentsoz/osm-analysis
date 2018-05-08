package agentsoz.osm.analysis.app;

import agentsoz.osm.analysis.handler.*;

public class Main {
	
	public static String OSM_FILE;
	private static BasicProblemHandler handler;
	
	/**
	 * program entry point
	 * call methods based on user input returned by parse args[]
	 * type of handler is defined in parse() and then it calls handleProblem()
	 * */
	public static void main(String args[]) 
	{
		int ref_input = parse(args);
		
		if(ref_input == 1)
		{
			DataBaseHandler.getInstance().createTables();
		}
		else if(ref_input == 2)
		{
			handler.handleProblem();
		}	
	}
	
	/**
	 * Parse args[] here and set variables from commandline options
	 * For instance, --way-file FILE could be used to set the var WAY_FILE to some
	 * user specified value.
	 * */
	public static int parse(String[] args) 
	{
		for(int i = 0; i < args.length-1; i++) 
		{
			if(args[i].equals("-file")) 
			{
				OSM_FILE = args[i+1];
				return 1;
			}
			else if(args[i].equals("-maxspeedgapproblem")) 
			{
				int speed = Integer.valueOf(args[i+1]);
				String dbUrl = args[i+2];	
			 	handler = new MaxSpeedGapProblemHandler(dbUrl, speed); 
			}
			else if(args[i].equals("-relationspeedprolem"))
			{
				String dbUrl = args[i+1];
				handler = new RelationSpeedProblemHandler(dbUrl);
			}
			else if(args[i].equals("-sameRouteInRWProblem"))
			{
				String dbUrl = args[i+1];
				handler = new SameRouteInRWProblemHandler(dbUrl);
			}
			else if(args[i].equals("-FromViaToProblemHandler")) 
			{
				String dbUrl = args[i+1];
				handler = new FromViaToProblemHandler(dbUrl);
			}
			else if(args[i].equals("-TypeBoundaryProblem")) 
			{
				String dbUrl = args[i+1];
				handler = new TypeBoundaryProblemHandler(dbUrl);
			}
			else if(args[i].equals("-TypeRestrictionProblem")) 
			{
				String dbUrl = args[i+1];
				handler = new TypeRestrictionProblemHandler(dbUrl);
			}
			else if(args[i].equals("-NameShortNameProblem")) 
			{
				String dbUrl = args[i+1];
				handler = new NameShortNameProblemHandler(dbUrl);
			}
		}
		return 2;
		
	} 

}
