package Model;

import java.util.ArrayList;
import java.util.List;

public class Way extends Member {
	
	public List<String> nodes_ref;
	public int id;
	public int maxspeed;
	public String name;
	//tag how many marked traffic lanes there are on a highway
	public String lanes;
	public String oneWay;
	
	public Way() {
		nodes_ref = new ArrayList<>();
		name = new String();
		lanes = new String();
		oneWay = new String();
	}
	
}
