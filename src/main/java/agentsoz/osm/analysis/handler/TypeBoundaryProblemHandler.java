package agentsoz.osm.analysis.handler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TypeBoundaryProblemHandler extends PrerequisitesProblemHandler {

	static final Logger LOG = Logger.getLogger(TypeBoundaryProblemHandler.class.getName());
	
	Statement stm = null;
	Statement stm1 = null;
	Statement stm2 = null;
	Connection con = null;
	
	int countIssues;
	private String url;
	
	public TypeBoundaryProblemHandler(String databaseUrl)
	{
		url = "jdbc:sqlite:" + databaseUrl;
	}
	
	@Override
	public void handleProblem()
	{
		long begin = 0;
		long end = 0;
		
		try {
			Class.forName("org.sqlite.JDBC");
			con = super.connect(url);
			stm = con.createStatement();
			stm1 = con.createStatement();
			stm2 = con.createStatement();
			
			begin = System.currentTimeMillis();
			getRelations();
			end = System.currentTimeMillis();
			
			stm1.close();
			stm.close();
			con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			LOG.log(Level.INFO, "the running time is:" + (end - begin) + "ms");
			LOG.log(Level.INFO, "how many relations are missing attribute: " + countIssues);
		}
		
	}

	private void getRelations() throws SQLException {
		// TODO Auto-generated method stub
		String sql_size = "SELECT COUNT(ID) AS size FROM relations";
		ResultSet res_size = stm2.executeQuery(sql_size);
		int size = res_size.getInt("size");
		
		String sql = "SELECT ID FROM relations";
		ResultSet res = stm.executeQuery(sql);
		int current = 0;
		int percent = 0;
		while(res.next()) {
			current++;
			if(current >= size/10) {
				percent = percent + 10;
				LOG.log(Level.INFO, percent + "% data has been finished");
				current = 0;
			}
			getTags(res.getInt("ID"));
		}
	}

	private void getTags(int relationID) throws SQLException {
		// TODO Auto-generated method stub
		String sql = "SELECT tag_key,tag_value FROM relations_tags WHERE relation_id='"+relationID+"'";
		ResultSet res = stm1.executeQuery(sql);
		
		String type = "";
		String boundary = "";
		String admin_level = "";
		
		while(res.next()) {
			if(res.getString("tag_key").equals("type")) {
				type = res.getString("tag_key");
			}
			if(res.getString("tag_key").equals("boundary")) {
				boundary = res.getString("tag_key");
			}
			if(res.getString("tag_key").equals("admin_level")) {
				admin_level = res.getString("tag_key");
			}
		}
		
		if(type.equals("boundary")) {
			if(!(boundary.equals("boundary")) || !(admin_level.equals(""))) {
				LOG.log(Level.INFO, "the relation(" + relationID + ") miss the attribute");
				countIssues++;
			}
		}
	}

}
