package agentsoz.osm.analysis.handler;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class XMLReader {

	XMLHandler handler;
	File file;
	String WAY_FILE;
	
	public XMLReader(String file) 
	{
		WAY_FILE = file;
	}
	
	public void readXML() 
	{
		try
		{
			file = new File(WAY_FILE);
		
			SAXParserFactory spf = SAXParserFactory.newInstance(); 
			SAXParser saxParser = spf.newSAXParser();
		
			handler = new XMLHandler();
			saxParser.parse(file, handler);
			
		}
		catch (ParserConfigurationException e) 
		{	
			e.printStackTrace();
		}
		catch (SAXException e)
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

}
