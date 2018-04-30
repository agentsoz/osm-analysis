package models;

import java.util.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import client.testClient;

/**
 * startDocument() and endDocument() defines what application does at the start and the end of document
 * startElement() and endElement() called at the start and end of an element
 * characters() called between the start and end of tags
 * 
 * @author Wenbo Peng
 * */

public class XMLHandler extends DefaultHandler{
	
	Hashtable<String, String> hashtable;
	SearchHandler searchHandler;
	Way way;
	int opt;
	String userInput;
	ArrayList<Way> ways;
	static boolean noRecord;
	
	public XMLHandler(int opt, ArrayList<String> list) {}
	
	public XMLHandler(int opt, String userInput) 
	{
		searchHandler = new SearchHandler() ;
		// register the callback for all logging output
		searchHandler.addLoggingCallback(new LoggingCallbackImpl());
		
		this.opt = opt; 
		this.userInput = userInput;
	}

	/**
	 * do-nothing method overridden here
	 * @param qName
	 *            name of the element
	 * */
	public void startElement(String uri, String localName, String qName, Attributes attributes) 
			throws SAXException
	{	
		hashtable = new Hashtable<String, String>();
		
		// if current element is way, create a new way
		if(qName.equalsIgnoreCase("way")) 
		{
			way = new Way();
			way.setWayId(attributes.getValue("id"));
		}
		else if(qName.equalsIgnoreCase("nd"))
		{
			way.addNodes(attributes.getValue("ref"));
		}
		else if(qName.equalsIgnoreCase("tag"))
		{
			way.addTags(attributes.getValue("k"), attributes.getValue("v"));
		}
	}
	
	public void endElement(String uri, String localName, String qName)  
			throws SAXException
	{
		// if end of "way", we could perform search function and loop through object "way"
 		if(qName.equalsIgnoreCase("way")) 
		{
 			if(opt == 1)
 				searchHandler.searchWayByNode(way, userInput);
 			else if(opt == 2)
 				searchHandler.searchSpeedByWay(way, userInput);
 			else if(opt == 3)
 				searchHandler.searchWayByName(way, userInput);
 			else if(opt == 4)
 				searchHandler.searchWayByNameWithSpeed(way, userInput); 	
 			else if(opt == 11)
 			{
 				Way wayFromNode = searchHandler.searchWayByNode(way, userInput);
 				if(wayFromNode != null)
 				System.out.println(wayFromNode.getWayId());
// 				testClient.nodeIds = wayFromNode.getNodes();
 			}
 			else if(opt == 110)
 			{
 				for(String ndId : testClient.nodeIds)
 					searchHandler.searchWayByNode(way, ndId);
 			}	
		}
	}
	
	public void startDocument()
	{
		noRecord = true;
		ways = new ArrayList<Way>();
	}
	
	public void endDocument() 
	{
		// if found nothing at the end of document, prompt user nothing was found 
		if(noRecord == true)
			searchHandler.loggingCallback.showNoRecord();
	}
}
