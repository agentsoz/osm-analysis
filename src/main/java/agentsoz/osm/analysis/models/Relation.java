package agentsoz.osm.analysis.models;

public class Relation {

	private String id;
	private int maxspeed;
	
	public void setId(String id) {this.id = id;}
	
	public String getId() {return id;}

	public void setSpeed(int speed) {this.maxspeed = speed;}
	
	public int getSpeed() {return maxspeed;}
}
