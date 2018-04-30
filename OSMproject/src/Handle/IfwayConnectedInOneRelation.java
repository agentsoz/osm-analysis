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

public class IfwayConnectedInOneRelation {
	/*+
	 * As in one relation, the ways should be connected one by one, thus if the 
	 * final node of one way is not as same as the first node of the next way, it 
	 * should be a problem
	 * 
	 * to find this issue, I compare the id of last node of one way with
	 * the id of first node of the further way, if these two IDs are different, that
	 * means this two ways are not connected 
	 * */
	static final Logger LOG = Logger.getLogger(IfwayConnectedInOneRelation.class.getName());
	
	Connection con = null;
	Statement stm = null;
	
	int countIssues = 0;
	
	private List<String> ways_ref;
	
	private List<String> nodes_ref;
	
	private void compare(String relation_id) {
		//获取所有的way
		String sql = "SELECT member_ref FROM relations_members WHERE relation_id = '"+relation_id+"' AND member_type = 'way' ";
		try {
			//get the all the way id of one relation
			ResultSet res = stm.executeQuery(sql);
			//把way id 加入到集合
			while(res.next()) {
				ways_ref.add(res.getString("member_ref"));
			}
			
			String last_node_ref = null;
			String first_node_ref = null;
			//outer loop used to find the last node of the first way
			
			for(int i=0;i<ways_ref.size();i++) {
				sql = "SELECT node_id FROM ways_nodes WHERE way_id = '"+ways_ref.get(i)+"'";
				res = stm.executeQuery(sql);
				while(res.next()) {
				//	System.out.println(res.getString("node_id"));
					nodes_ref.add(res.getString("node_id"));
				}
			//	System.out.println(nodes_ref.size());
				last_node_ref = nodes_ref.get(nodes_ref.size()-1);
				
				//inner loop used to find the first node of the second way
				for(int j=i+1;j<ways_ref.size()-1;j++) {
					sql = "SELECT node_id FROM ways_nodes WHERE way_id = '"+ways_ref.get(j)+"'";
					res = stm.executeQuery(sql);
					res.next();
					first_node_ref = res.getString("node_id");
				}
				
				if(!(last_node_ref.equals(first_node_ref))) {
					countIssues++;
				} 
				
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void check() {
		
		ways_ref = new ArrayList<>();
		
		nodes_ref = new ArrayList<>();
		
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:osm.db");
			stm = con.createStatement();
			
			String sql = "SELECT id FROM relations";
			ResultSet res = stm.executeQuery(sql);
			//检查每个relation
			while(res.next()) {
				compare(res.getString("id"));
				ways_ref.clear();
				nodes_ref.clear();
			}
		
			con.close();
			stm.close();

		} catch(Exception e) {
			e.printStackTrace();
		}finally {
			LOG.log(Level.INFO, "the issue that ways in one relation is not connected one by one:" + countIssues);
		}
	}
}
