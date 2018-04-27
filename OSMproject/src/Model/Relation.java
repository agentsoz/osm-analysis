package Model;

import java.util.ArrayList;
import java.util.List;

public class Relation {
	
	public int id;
	
	public int maxspeed;
	
	public String route;
	
	public List<Member> members;
	
	public Relation() {
		members = new ArrayList<>();
	}

}
