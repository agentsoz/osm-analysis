package handler;

import java.util.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import models.Way;

/**
 * startDocument() and endDocument() defines what application does at the start and the end of document
 * startElement() and endElement() called at the start and end of an element
 * characters() called between the start and end of tags
 * 
 * */

public class XMLHandler extends DefaultHandler{
	
	Way way;
	private List<Way> ways;
	
	/**
	 * do-nothing method overridden here
	 * @param qName
	 *            name of the element
	 * */
	public void startElement(String uri, String localName, String qName, Attributes attributes) 
			throws SAXException
	{	
		// if current element is way, create a new way
		if(qName.equalsIgnoreCase("way")) 
		{
			way = new Way();
			way.setId(attributes.getValue("id"));
		}
		else if(qName.equalsIgnoreCase("nd"))
		{
			way.addNodesRef(attributes.getValue("ref"));
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
 				ways.add(way);
		}
	}
	
	public void startDocument()
	{
		ways = new ArrayList<Way>();
	}
	
	public void endDocument() {}
	
	public List<Way> getWays()
	{
		return ways;
	}
}
