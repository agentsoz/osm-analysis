import static org.junit.Assert.*;

import org.junit.Test;

public class MaxSpeedGapProblemHandlerTest {
	
MaxSpeedGapProblemHandler ins;
	
	List<Way> wayTest;
	
    @Before
    public void setUp() throws Exception {
        
        ins = new MaxSpeedGapProblemHandler("", 50);
        
        wayTest = new ArrayList<Way>();
        
        Way w1 = new Way();
        w1.setId("1");
        w1.setSpeed(20);
        w1.addNodesRef("1254");
        w1.addNodesRef("3265536");
        
        Way w2 = new Way();
        w2.setId("2");
        w2.setSpeed(100);
        w2.addNodesRef("3265536");
        w2.addNodesRef("1254346");
        
        Way w3 = new Way();
        w3.setId("3");
        w3.setSpeed(200);
        w3.addNodesRef("3265536");
        w3.addNodesRef("34534634");
        
        wayTest.add(w1);
        wayTest.add(w2);
        wayTest.add(w3);
        
    }
    
    @Test
    public void testCompare() {
        ins.testCompare(wayTest);
        assertEquals(1,ins.count_speed);
	}

}
