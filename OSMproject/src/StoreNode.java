import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;

public class StoreNode implements InsertData {
	
	private static StoreNode instance;
	
	public static StoreNode getInstance() {
		
		if(instance == null) {
			instance = new StoreNode();
		}
		
		return instance;
	}
	
	@Override
	public void insert(Statement stm, Connection con) {
		// TODO Auto-generated method stub
		/*
		 * change the path because the data.osm is too big, thus it is unnecessary to upload
		 * */
		String path = "F:\\New folder\\data.osm";
		File filename = new File(path);
		InputStreamReader reader;
		try {
			reader = new InputStreamReader(new FileInputStream(filename));
			BufferedReader br = new BufferedReader(reader);
			String line = "";
			String id = "";
			while((line = br.readLine()) != null) {
				
				if(line.contains("<node")) {
					String[] nodes = line.split("\"");
					id = nodes[1];

					if(nodes[9].contains("'")) {
						nodes[9] = nodes[9].replace("'", "''");
					}
					
					String node = "INSERT INTO nodes VALUES('"+nodes[1]+"',"
							+ "'"+nodes[3]+"',"
							+ "'"+nodes[5]+"',"
							+ "'"+nodes[7]+"',"
							+ "'"+nodes[9]+"',"
							+ "'"+nodes[11]+"',"
							+ "'"+nodes[13]+"',"
							+ "'"+nodes[15]+"')";
					stm.executeUpdate(node);
					
				}else if(line.contains("tag")) {
					String[] tags = line.split("\"");

					if(tags[3].contains("'")) {
						tags[3] = tags[3].replace("'", "''");
					}
					
					String tag = "INSERT INTO nodes_tags VALUES('"+id+"',"
							+ "'"+tags[1]+"',"
							+ "'"+tags[3]+"')";
					stm.executeUpdate(tag);
				}
			}
			
			con.commit();
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
