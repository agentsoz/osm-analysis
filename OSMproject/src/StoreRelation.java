import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

@Deprecated
public class StoreRelation implements InsertData {

	private static StoreRelation instance;
	
	public static StoreRelation getInstance() {
		
		if(instance == null) {
			instance = new StoreRelation();
		}
		
		return instance;
	}
	
	@Override
	public void insert(Statement stm, Connection con) {
		
		SAXReader reader = new SAXReader();
		
		try {

			Document document = reader.read(new File("src/res/relation.xml"));
			Element root = document.getRootElement();
			List<Element> list = root.elements();
			String replace;
			
			for(Element e:list) {
				
				replace = e.attributeValue("user");
				if(replace.contains("'")) {
					replace = replace.replace("'", "''");
				}
				
				String relations = "INSERT INTO relations VALUES('"+Integer.parseInt(e.attributeValue("id"))+"',"
						+ "'"+e.attributeValue("version")+"',"
						+ "'"+e.attributeValue("timestamp")+"',"
						+ "'"+e.attributeValue("uid")+"',"
						+ "'"+replace+"',"
						+ "'"+e.attributeValue("changeset")+"')";
				stm.executeUpdate(relations);
				List<Element> cList = e.elements();
				for(Element c:cList) {
					if(c.getName().equals("member")) {
						String member = "INSERT INTO relations_members VALUES('"+Integer.parseInt(e.attributeValue("id"))+"',"
								+ "'"+c.attributeValue("type")+"',"
								+ "'"+c.attributeValue("ref")+"',"
								+ "'"+c.attributeValue("role")+"')";
						stm.executeUpdate(member);
						
					}else {
	
						replace = c.attributeValue("v");
						if(replace.contains("'")) {
							replace = replace.replace("'", "''");
						}
						
						String tags = "INSERT INTO relations_tags VALUES('"+Integer.parseInt(e.attributeValue("id"))+"',"
								+ "'"+c.attributeValue("k") + "-" + replace + "')";
						stm.executeUpdate(tags);
					}
				}
			}
			con.commit();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
