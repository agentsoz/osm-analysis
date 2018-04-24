package project;

public class Way {

	public String name;
	public int time;
	public double distance;
	
	public Way(String name, String time,String distance){
		this.name = name;
		this.time = Integer.parseInt(time);
		this.distance = Double.parseDouble(distance);
	}
	
}