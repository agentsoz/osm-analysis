package agentsoz.osm.analysis.app;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import agentsoz.osm.analysis.handler.*;

public class Main {
	
	public static String OSM_FILE;
	private static BasicProblemHandler handler;
	static String dbUrl;
	static Options options;
	public static String file_path;
	static String type;
	
	/**
	 * program entry point
	 * call methods based on user input returned by parse args[]
	 * type of handler is defined in parse() and then it calls handleProblem()
	 * */
	public static void main(String args[]) 
	{
		parse(args);
		
		if(handler != null) 
		{	
			handler.handleProblem();
		}
	}
	
	/**
	 * Parse args[] here and set variables from commandline options
	 * For instance, --way-file FILE could be used to set the var WAY_FILE to some
	 * user specified value.
	 * */
	public static void parse(String[] args) 
	{	
		// create command line parser
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd;
		
		// create the options
		options = new Options();
		
		options.addOption("osm", "write-osm-to-database", true, "Write given osm file into database");
		options.addOption("f", "in-file", true,  "Input database");
		options.addOption("w", "out-file", true, "Write output to file");
		options.addOption("s", "search-missing", true, "Search missing attribute of certain type");
		options.addOption("v", "value", true, "value of searching operation");
		options.addOption("way", false, "choose type way");
		options.addOption("relation", true, "choose type relation");
		options.addOption("opt1", "get-ways-speed-change", true, "get two adjacent way speed difference greater than input value");
		options.addOption("opt2", "get-ways-relation-speed-change", false, "get ways' max_speed exceed its relation");
		
		try 
		{	
			cmd = parser.parse(options, args);
	
			// run main without command line arguments
			// if return an empty array of the processed options 
			if(cmd.getOptions().length == 0) 
			{
				help();
			}
			// get input osm file from user
			if(cmd.hasOption("osm")) 
			{
				OSM_FILE = cmd.getOptionValue("osm");
				handler = new DataBaseHandler();
			}
			// get input database file from user
			if(cmd.hasOption("f")) 
			{
				dbUrl = cmd.getOptionValue("f");	
			}
			// write file
			if(cmd.hasOption("w")) 
			{
				String path = cmd.getOptionValue("w");
				file_path = path;
			}
			if(cmd.hasOption("way")) 
			{
				type = "ways";
			} 
			// search missing value : get type of the value
			if(cmd.hasOption("s")) 
			{
				String type = cmd.getOptionValue("s");
				SearchMissingAttribute.choice = type;
			}
			// search missing value : get value 
			if(cmd.hasOption("v")) 
			{
				String attribute = cmd.getOptionValue("v");
				SearchMissingAttribute search = new SearchMissingAttribute(dbUrl); 
				search.search(attribute);
			}
			if(cmd.hasOption("opt1")) 
			{
				int speed_threshold = Integer.parseInt(cmd.getOptionValue("opt1"));
				handler = new MaxSpeedGapProblemHandler(dbUrl, speed_threshold);
			}
			if(cmd.hasOption("opt2")) 
			{
				handler = new RelationSpeedProblemHandler(dbUrl);
			}
		}
		catch(ParseException e)
		{
			System.out.println(e.getMessage());
			help();
		}
	} 

	private static void help() 
	{
		HelpFormatter formatter = new HelpFormatter();
		
		formatter.printHelp("Menu", options);
		
		System.exit(0);
	}
}
