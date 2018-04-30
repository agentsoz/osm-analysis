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

import Model.Member;
import Model.Relation;
import Model.Way;

public class WayOrderInRProblem {
	/*+
	 * As in one relation, the ways should be connected one by one, thus if the 
	 * final node of one way is not as same as the first node of the next way, it 
	 * should be a problem
	 * 
	 * to find this issue, I compare the id of last node of one way with
	 * the id of first node of the further way, if these two IDs are different, that
	 * means this two ways are not connected
	 * */
	static final Logger LOG = Logger.getLogger(WayOrderInRProblem.class.getName());
	

	int countIssues = 0;
	
	private List<String> ways_ref;
	
	private List<String> nodes_ref;
	
	List<Way> ways;
	
	private void compare(Relation re) {
		//获取所有的way
		for(Member m : re.members) {
			if(m instanceof Way) {
				ways_ref.add(String.valueOf(((Way) m).id));
			}
		}
		String last_node_ref = null;
		String first_node_ref = null;
		//outer loop used to find the last node of the first way
		for(int i=0;i<ways_ref.size()-1;i++) {
			
			for(int j=0;j<ways.size();j++) {
				if(ways_ref.get(i).equals(ways.get(j))) {
					
					last_node_ref = String.valueOf(ways.get(j).nodes_ref.get(ways.get(j).nodes_ref.size()-1));
				}
			}
			for(int j=0;j<ways.size();j++) {
				if(ways_ref.get(i+1).equals(ways.get(j))) {
					first_node_ref = String.valueOf(ways.get(j).nodes_ref.get(0));
				}
			}

			if(!(last_node_ref.equals(first_node_ref))) {
				countIssues++;
			} 
			
		}
	}
	
	public void handle() {
		
		ways_ref = new ArrayList<>();
		
		nodes_ref = new ArrayList<>();
		
		List<Relation> relations = DataHandle.getInstance().relations;
		
		ways = DataHandle.getInstance().ways;
		
			//检查每个relation
			for(Relation re : relations) {
				compare(re);
				ways_ref.clear();
				nodes_ref.clear();
				System.out.println("dd");
			}

			LOG.log(Level.INFO, "the issue that ways in one relation is not connected one by one:" + countIssues);

	}
}
