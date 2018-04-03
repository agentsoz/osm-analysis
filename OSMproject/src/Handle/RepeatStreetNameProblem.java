package Handle;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RepeatStreetNameProblem {

	static final Logger LOG = Logger.getLogger(RepeatStreetNameProblem.class.getName());
	
	//not finish yet
	public void repeatedStreetName(List<Way> ways) {
		String w1_name;
		String w2_name;
		int count_repeat = 0;
		for(int i=0;i<ways.size();i++) {
			w1_name = ways.get(i).name;
			for(int j=i+1;j<ways.size()-1;j++) {
				w2_name = ways.get(j).name;
				String w1_begin_node = ways.get(j).nodes_ref.get(0);
			//	String w1_end_node = ways.get(j)
				if(w1_name.equals(w2_name)) {
					count_repeat++;
				}
			}
		}
		LOG.log(Level.INFO, "the number of two streets having same name:" + count_repeat);
	}

}
