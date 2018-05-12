package agentsoz.osm.analysis.handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LOGReadWrite {
	
	private String filepath;
	
	private File file;
	
	public LOGReadWrite(String filepath) {
		this.filepath = filepath;
	}

	public void write(String content) {
		
		file = new File(filepath);
		
		try {
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.append(content);
			
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String read() {
		
		file = new File(filepath);
		
		String content = "";
		
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line = null;
			while((line = br.readLine()) != null) {
				content = content + line;
			//	System.out.println(line);
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
		
		return content;
		
	}
	
	public File getFile() {
		
		return file;
		
	}
	
}
