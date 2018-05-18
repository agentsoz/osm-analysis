import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import agentsoz.osm.analysis.handler.LOGReadWrite;

public class LOGReadWriteTest {
	
		
		private LOGReadWrite ins;
		
		@Before
		public void setUp() throws Exception {
			
			ins = new LOGReadWrite("src/resources/logTest.txt");
			
		}
		
		@Test
		public void testWrite() {
			
			ins.write("test");

			assertEquals("test",ins.read());
			
		}

		@After
		public void tearDown() throws Exception {
			
			if(ins.getFile().exists()) {
				ins.getFile().delete();
			}
	}

}
