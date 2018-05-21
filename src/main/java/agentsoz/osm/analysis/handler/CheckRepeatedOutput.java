package agentsoz.osm.analysis.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CheckRepeatedOutput {
	
	private File file;
	
	public CheckRepeatedOutput(String file_path) {

		file = new File(file_path);
		
	}
	
	public boolean check(String newInput) {
		
		boolean repeated = false;
		
		FileReader fr;
		
		try {
			
			fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			
			String line = null;
			while((line = br.readLine()) != null) {
				if(newInput.equals(line)) {
					repeated = true;
					break;
				}
			}
			
			br.close();
			fr.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return repeated;
	
	}
	
}
