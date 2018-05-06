package Handle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FromViaToProblemHandler extends BasicProblemHandler {

	static final Logger LOG = Logger.getLogger(FromViaToProblemHandler.class.getName());
	
	int countIssue = 0;
	
	Statement stm;
	Statement stm1;
	Statement stm2;
	Statement stm3;
	Statement stm4;
	Statement stm5;
	Connection con;
	
	List<String> wayIDs;
	
	@Override
	public void handleProblem() {
		// TODO Auto-generated method stub
		
		wayIDs = new ArrayList<>();
		
		long begin = 0;
		long end = 0;
		
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:osm.db");
			stm = con.createStatement();
			stm1 = con.createStatement();
			stm2 = con.createStatement();
			stm3 = con.createStatement();
			stm4 = con.createStatement();
			stm5 = con.createStatement();
			
			begin = System.currentTimeMillis();
			getRelations();
			end = System.currentTimeMillis();
			
			stm.close();
			stm1.close();
			stm2.close();
			stm3.close();
			stm4.close();
			con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			LOG.log(Level.INFO, "the running time is " + (end - begin) + "ms");
			LOG.log(Level.INFO, "how many relations have from_via_to problem:" + countIssue);
		}
		
		
	}

	private void getRelations() throws SQLException {
		// TODO Auto-generated method stub
		String sql_wayID = "SELECT ID FROM ways";
		ResultSet res_wayID = stm5.executeQuery(sql_wayID);
		while(res_wayID.next()) {
			wayIDs.add(res_wayID.getString("ID"));
		}
		stm5.close();
		
		String sql_size = "SELECT COUNT(ID) AS RelationSize FROM relations";
		ResultSet res_size = stm.executeQuery(sql_size);
		int size = res_size.getInt("RelationSize");
		
		String sql = "SELECT ID FROM relations";
		ResultSet res = stm1.executeQuery(sql);
		int count = 0;
		int percent = 0;
		while(res.next()) {
			count++;
			if(count >= size/10) {
				percent = percent + 10;
				LOG.log(Level.INFO, percent + "% data has been finished");
				count = 0;
			}
			getMembers(res.getInt("ID"));
		}
	}

	private void getMembers(int relationID) throws SQLException {
		// TODO Auto-generated method stub
		String sql = "SELECT member_type, member_ref,member_role FROM relations_members WHERE relation_id='"+relationID+"'";
		ResultSet res = stm2.executeQuery(sql);
		
		String[] nodes = new String[3];
		
		int test = 0;
		int check = 0;
		while(res.next()) {
			test++;
			if(res.getString("member_role").equals("from")) {
				nodes[0] = res.getString("member_ref");
				check = test;
			}
			if(res.getString("member_role").equals("via") && (check == test + 1)) {
				nodes[1] = res.getString("member_ref");
			}
			if(res.getString("member_role").equals("to") && (check == test + 2)) {
				nodes[2] = res.getString("member_ref");
			}
		}
		
		if(wayIDs.contains(nodes[0]) && wayIDs.contains(nodes[2])) {
			check(nodes,relationID);
		}
	}

	private void check(String[] nodes, int relationID) throws SQLException {
		// TODO Auto-generated method stub
		if(!(nodes[0].equals("") || nodes[1].equals("") || nodes[2].equals(""))) {
			String sql_from = "SELECT node_id FROM ways_nodes WHERE way_id='"+nodes[0]+"'";
			ResultSet res_from = stm3.executeQuery(sql_from);
			String last_node = "";
			while(res_from.next()) {
				last_node = res_from.getString("node_id");
			}
			String sql_to = "SELECT node_id FROM ways_nodes WHERE way_id='"+nodes[2]+"'";
			ResultSet res_to = stm4.executeQuery(sql_to);
			String first_node = "";
			while(res_to.next()) {
				first_node = res_to.getString("node_id");
				break;
			}
			if(!(first_node.equals(last_node) && first_node.equals(nodes[1]))) {
				countIssue++;
				LOG.log(Level.INFO, "the relation(" + relationID + ") has from_via_to problem");
			}
		}
		
	}
	
}
