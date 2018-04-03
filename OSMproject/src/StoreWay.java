import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class StoreWay implements InsertData {
	
	private static StoreWay instance;
	
	public static StoreWay getInstance() {
		
		if(instance == null) {
			instance = new StoreWay();
		}
		
		return instance;
	}
	
	@Override
	public void insert(Statement stm, Connection con) {
		// TODO Auto-generated method stub
		SAXReader reader = new SAXReader();
		try {	
			Document document = reader.read(new File("src/res/way.xml"));
			Element root = document.getRootElement();
			List<Element> list = root.elements();
			String replace;
			for(Element e:list) {
				replace = e.attributeValue("user");
				if(replace.contains("'")) {
					replace = replace.replace("'", "''");
				}
				String basic = "INSERT INTO ways VALUES('"+Integer.parseInt(e.attributeValue("id"))+"',"
						+ "'"+e.attributeValue("version")+"',"
						+ "'"+e.attributeValue("timestamp")+"',"
						+ "'"+e.attributeValue("uid")+"',"
						+ "'"+replace+"',"
						+ "'"+e.attributeValue("changeset")+"')";
				stm.executeUpdate(basic);
				
				List<Element> cList = e.elements();
				for(Element c:cList) {
					if(c.getName().equals("nd")) {
						String node = "INSERT INTO ways_nodes VALUES('"+e.attributeValue("id")+"',"
								+ "'"+c.attributeValue("ref")+"')";
						stm.executeUpdate(node);
					}else if(c.getName().equals("tag")){
						replace = c.attributeValue("v");
						if(replace.contains("'")) {
							replace = replace.replace("'", "''");
						}
						String tag = "INSERT INTO ways_tags VALUES('"+e.attributeValue("id")+"',"
								+ "'"+c.attributeValue("k") + "-" + replace+"')";
						stm.executeUpdate(tag);
					}

				}
			}
			con.commit();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 

	}
}
