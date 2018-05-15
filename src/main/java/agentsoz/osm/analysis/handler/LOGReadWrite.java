package agentsoz.osm.analysis.handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LOGReadWrite {
	
	private String filepath;
	
	private File file;
	
//	public LOGReadWrite(String filepath) {
//		this.filepath = filepath;
//	}

	public static void write(String content, String filepath) {
		
//		file = new File(filepath);
		
		try (FileWriter fw = new FileWriter(filepath,true);
			 BufferedWriter bw = new BufferedWriter(fw);
			 PrintWriter out =new PrintWriter(bw))
		{
			out.println(content);
		}
		catch (IOException e) 
		{
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
