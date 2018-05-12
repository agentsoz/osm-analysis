package agentsoz.osm.analysis.handler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NameShortNameProblemHandler extends PrerequisitesProblemHandler {

	static final Logger LOG = Logger.getLogger(NameShortNameProblemHandler.class.getName());
	
	Statement stm;
	Statement stm1;
	Statement stm2;
	Connection con;
	private String url;
	
	int countIssue = 0;
	
	public NameShortNameProblemHandler(String databaseUrl)
	{
		url = "jdbc:sqlite:" + databaseUrl;
	}
	
	@Override
	public void handleProblem() {
		// TODO Auto-generated method stub
		
		long end = 0;
		long begin = 0;
		
		try {
			Class.forName("org.sqlite.JDBC");
			con = super.connect(url);
			stm = con.createStatement();
			stm1 = con.createStatement();
			stm2 = con.createStatement();
			
			begin = System.currentTimeMillis();
			getRelations();
			end = System.currentTimeMillis();
			
			con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			LOG.log(Level.INFO, "the running time is " + (end - begin) + "ms");
			LOG.log(Level.INFO, "how many relations have name-short_name problem:" + countIssue);
		}
		
		
	}

	private void getRelations() throws SQLException {
		// TODO Auto-generated method stub
		String sql_size = "SELECT COUNT(ID) AS size FROM relations";
		ResultSet res_size = stm2.executeQuery(sql_size);
		int size = res_size.getInt("size");
		stm2.close();
		
		String sql = "SELECT ID FROM relations";
		ResultSet res = stm.executeQuery(sql);
		int count = 0;
		int percent = 0;
		while(res.next()) {
			count++;
			if(count >= size/10) {
				percent = percent + 10;
				LOG.log(Level.INFO, percent + "% data has been finished");
				count = 0;
			}
			getTags(res.getString("ID"));
		}
		stm.close();
		stm1.close();
	}

	private void getTags(String relationID) throws SQLException {
		// TODO Auto-generated method stub
		String sql = "SELECT tag_key,tag_value FROM relations_tags WHERE relation_id='"+relationID+"'";
		ResultSet res = stm1.executeQuery(sql);
		
		String name = "";
		String short_name = "";
		while(res.next()) {
			if(res.getString("tag_key").equals("name")) {
				name = res.getString("tag_value");
			}
			if(res.getString("tag_key").equals("short_name")) {
				short_name = res.getString("tag_value");
			}
		}
		
		if(!(short_name.equals(""))) {
			if(name.equals("")) {
				countIssue++;
				LOG.log(Level.INFO, "the relation(" + relationID + ") has name-short_name problem");
			}
		}
	}

}
