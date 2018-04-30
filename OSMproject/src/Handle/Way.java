package Handle;

import java.util.ArrayList;
import java.util.List;

public class Way {
	
	List<String> nodes_ref;
	int id;
	int maxspeed;
	String name;
	//tag how many marked traffic lanes there are on a highway
	String lanes;
	String oneWay;
	
	public Way() {
		nodes_ref = new ArrayList<>();
		name = new String();
		lanes = new String();
		oneWay = new String();
	}
	
}
