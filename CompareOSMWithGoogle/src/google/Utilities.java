package google;

import java.io.FileWriter;
import java.io.IOException;

import model.*;

public class Utilities {

	public static void recordCoor(Route route){
		try {
			FileWriter fw = new FileWriter("co.txt");
			for(Node n : route.nodes){
				fw.write(n.lat+","+n.lon+"\n");
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void writeOSM(String res){
		try {
			FileWriter fw = new FileWriter("osm.txt");
			fw.write(res);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void writeGoo(String res){
		try {
			FileWriter fw = new FileWriter("goo.txt");
			fw.write(res);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
