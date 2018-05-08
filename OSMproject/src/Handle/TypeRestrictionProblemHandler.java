package Handle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TypeRestrictionProblemHandler extends PrerequisitesProblemHandler {

	static final Logger LOG = Logger.getLogger(TypeRestrictionProblemHandler.class.getName());
	
	Statement stm;
	Statement stm1;
	Connection con;
	
	int countIssue = 0;
	
	@Override
	public void handleProblem() {
		// TODO Auto-generated method stub
		
		long begin = 0;
		long end = 0;
		
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:osm.db");
			stm = con.createStatement();
			stm1 = con.createStatement();
			
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
			LOG.log(Level.INFO, "how many relations have type-restriction problem:" + countIssue);
		}
	}

	private void getRelations() throws SQLException {
		// TODO Auto-generated method stub
		String sql = "SELECT ID FROM relations";
		ResultSet res = stm.executeQuery(sql);
		while(res.next()) {
			getTags(res.getInt("ID"));
		}
		
		stm.close();
		stm1.close();
	}

	private void getTags(int relationID) throws SQLException {
		// TODO Auto-generated method stub
		String sql = "SELECT tag_key,tag_value FROM relations_tags WHERE relation_id='"+relationID+"'";
		ResultSet res = stm.executeQuery(sql);
		String type = "";
		String restriction = "";
		while(res.next()) {
			if(res.getString("tag_key").equals("type")) {
				type = res.getString("tag_value");
			}
			if(res.getString("tag_key").equals("restriction")) {
				restriction = res.getString("tag_value");
			}
		}
		
		if(type.equals("restriction")) {
			if(restriction.equals("")) {
				countIssue++;
				LOG.log(Level.INFO, "the relation(" + relationID + ") has type-restriction problem");
			}
		}
		
	}

}
