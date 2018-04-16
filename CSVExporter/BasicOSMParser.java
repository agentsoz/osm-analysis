import java.io.File;
import java.io.IOException;

import org.xml.sax.SAXException;

/**
 * This class is a simple command line interface.
 * It parses OSM XML and create a CSV export.
 * You have to use it like this : basicosmparser <Input OSM XML> <Output folder>
 */
public class BasicOSMParser {
//OTHER METHODS
	public static void main(String[] args) {
		//Check arguments
		if(args.length != 2) {
			System.out.println("Invalid parameters.\nCommand usage: basicosmparser <Input OSM XML> <Output folder>");
		}
		else {
			File input = new File(args[0]);
			File output = new File(args[1]);

			//Check input
			if(!input.exists() || !input.isFile() || !input.canRead()) {
				System.out.println("Can't read input OSM XML.\nCheck if file exists and is readable.");
			}
			else {
				//Check output
				if(!output.exists() || !output.isDirectory()) {
					System.out.println("Invalid destination folder.");
				}
				else {
					//Convert data
					OSMParser parser = new OSMParser();
					CSVExporter exporter = new CSVExporter();

					try {
						exporter.export(parser.parse(input), output);
						System.out.println("Exported data to "+output.getPath()+" without errors.");
					} catch (IOException | SAXException e) {
						System.err.println("Error during data export.");
						e.printStackTrace();
					}
				}
			}
		}
	}
}
