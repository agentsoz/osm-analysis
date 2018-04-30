import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;

public class Store implements InsertData {
	
	private static Store instance;
	
	public static Store getInstance() {
		
		if(instance == null) {
			instance = new Store();
		}
		
		return instance;
	}
	
	@Override
	public void insert(Statement stm, Connection con) {
		// TODO Auto-generated method stub
		/*
		 * change the path because the data.osm is too big, thus it is unnecessary to upload
		 * */
		String path = "F:\\forthedata\\data.osm";
		File filename = new File(path);
		InputStreamReader reader;
		try {
			reader = new InputStreamReader(new FileInputStream(filename));
			BufferedReader br = new BufferedReader(reader);
			String line = "";
			String id = "";
			String current = "";
			while((line = br.readLine()) != null) {

				if(line.contains("<node")) {
					current = "node";
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
					
				}else if(line.contains("<way")) {
					current = "way";
					String[] ways = line.split("\"");
					id = ways[1];
					if(ways[9].contains("'")) {
						ways[9] = ways[9].replace("'", "''");
					}
					
					String way = "INSERT INTO ways VALUES('"+ways[1]+"',"
							+ "'"+ways[3]+"',"
							+ "'"+ways[5]+"',"
							+ "'"+ways[7]+"',"
							+ "'"+ways[9]+"',"
							+ "'"+ways[11]+"')";
					stm.executeUpdate(way);
					
				}else if(line.contains("<relation")) {
					current = "relation";
					String[] relations = line.split("\"");
					id = relations[1];
					if(relations[9].contains("'")) {
						relations[9] = relations[9].replace("'", "''");
					}
					
					String relation = "INSERT INTO relations VALUES('"+relations[1]+"',"
							+ "'"+relations[3]+"',"
							+ "'"+relations[5]+"',"
							+ "'"+relations[7]+"',"
							+ "'"+relations[9]+"',"
							+ "'"+relations[11]+"')";
					stm.executeUpdate(relation);
					
				}else if(line.contains("tag")) {
					String[] tags = line.split("\"");

					if(tags[3].contains("'")) {
						tags[3] = tags[3].replace("'", "''");
					}
					
					String table = "";
					if(current.equals("node")) {
						table = "nodes_tags";
					}else if(current.equals("way")) {
						table = "ways_tags";
					}else if(current.equals("relation")) {
						table = "relations_tags";
					}
					String tag = "INSERT INTO '"+table+"' VALUES('"+id+"',"
							+ "'"+tags[1]+"',"
							+ "'"+tags[3]+"')";
					
					stm.executeUpdate(tag);	

				}else if(line.contains("member")) {
					String[] members = line.split("\"");
					
					String member = "INSERT INTO relations_members VALUES('"+id+"',"
							+ "'"+members[1]+"',"
							+ "'"+members[3]+"',"
							+ "'"+members[5]+"')";
					stm.executeUpdate(member);
				}else if(line.contains("nd")) {
					String[] ways = line.split("\"");
					String way = "INSERT INTO ways_nodes VALUES('"+id+"',"
							+ "'"+ways[1]+"')";
					stm.executeUpdate(way);
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
