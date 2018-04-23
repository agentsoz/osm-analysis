import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

@Deprecated
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
			int count = 0;
			for(Element e:list) {
				replace = e.attributeValue("user");
				count++;
			}
			System.out.println(count);
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
