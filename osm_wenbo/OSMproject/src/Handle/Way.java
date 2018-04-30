package Handle;

import java.util.ArrayList;
import java.util.List;

public class Way {
	
	List<String> nodes_ref;
	int id;
	int maxspeed;
	String name;
	 
	public Way() {
		nodes_ref = new ArrayList<>();
		name = new String();
	}
	
	public void setId(int id) 
	{
		this.id = id;
	}

	public void setSpeed(int speed)
	{
		this.maxspeed = speed;
	}
}
