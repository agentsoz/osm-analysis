package Issues;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import model.Way;

public class WaysWithoutNodes{
	public void getWays() {
		SAXReader reader = new SAXReader();
		try {	
			
			int i=0;
			Document document = reader.read(new File("src/res/way.xml"));
			Element root = document.getRootElement();
			List<Element> list = root.elements();
			
			String[] id = new String[1000000];
			for(Element e:list) {
				List<Element> wList = e.elements();
				boolean judge = false;
				
				
				for(Element e1:wList) {
					if(e1.getName().equals("nd")&&e1.attributeValue("ref").equals(null)) {
						judge = true;
						String wayId=e.attributeValue("id");
						id[i] = wayId;
						i++;
					}
				}
				
			}
			for(i=0;i<=1000000;i++) {
				System.out.println(id[i]);
				
			}
		}catch(Exception e) {
			
		}
		
	}
	public void getWays2() {
		SAXReader reader = new SAXReader();
		try {	
			
			int i=0;
			Document document = reader.read(new File("src/res/way.xml"));
			Element root = document.getRootElement();
			List<Element> list = root.elements();
			
			String[] id = new String[1000000];
			for(Element e:list) {
				List<Element> nList = e.elements();
				boolean judge = true;
				
				for(Element e1:nList) {
					if(e1.getName().equals("nd")) 
						judge = false;
					
				}
				
				if (judge == true) {
					String wayId=e.attributeValue("id");
					
					id[i] = wayId;
					i++;
				}
			}
			for(i=0;i<=1000000;i++) {
				System.out.println(id[i]);
				
			}
		}catch(Exception e) {
			
		}
		
	}
}