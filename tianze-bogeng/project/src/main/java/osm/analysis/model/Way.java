package osm.analysis.model;

import java.util.ArrayList;
import java.util.List;

public class Way {
	
	public List<String> nodes_ref;
	public String id;
	public int maxspeed;
	public String name;
	
	public Way() {
		nodes_ref = new ArrayList<>();
		name = new String();
		
	}
	
}
