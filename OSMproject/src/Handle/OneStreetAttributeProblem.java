package Handle;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Deprecated
public class OneStreetAttributeProblem {

	static final Logger LOG = Logger.getLogger(OneStreetAttributeProblem.class.getName());
	
	/*
	 * analyze
	 * */
	public static void differentAttribute(List<Way> ways) {
		String w1_name;
		String w2_name;
		String w1_oneway;
		String w2_oneway;
		List<String> street = new ArrayList<String>();
		for(int i=0;i<ways.size();i++) {
			w1_name = ways.get(i).name;
			w1_oneway = ways.get(i).oneWay;
			for(int j=i+1;j<ways.size()-1;j++) {
				w2_name = ways.get(j).name;
				w2_oneway = ways.get(j).oneWay;
				if(w1_name.equals(w2_name)) {
					int gap = Math.abs(w1_oneway.length() - w2_oneway.length());
					if(!(w1_oneway.equals(w2_oneway)) && gap == 1) {
						if(!(street.contains(w1_name))) {
							street.add(w1_name);
						}
					}
				}
			}
		}
		LOG.log(Level.INFO, "one street has different 'oneway': " + street.size());
	}

}
