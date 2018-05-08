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
public class motorWayWithoutRef {
	public void getMotorWayWithoutRef() {
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
					if(e1.getName().equals("tag")&&e1.attributeValue("k").equals("highway")&&e1.attributeValue("v").equals("motorway")&&(e1.attributeValue("k").equals("ref"))) {
						judge = true;
						
						String wayId=e.attributeValue("id");
						id[i] = wayId;
						i++;  
						System.out.println(wayId);
						
					}
				}
				
			
				
			}
		}catch(Exception e) {
			
		}
		
	}
		
	}


