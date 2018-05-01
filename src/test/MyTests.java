package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.*;

import agentsoz.osm.analysis.app.models.Relation;
import agentsoz.osm.analysis.app.models.Way;


/**
 * need to optimize our current structure to make JUnit test easy
 * one method does one function
 * getWays() -> non static?
 *   getRootElements()
 *   setWays() : return a list?
 * */
public class MyTests {

	
	static Connection conn = null;
	
	@BeforeClass
	public static void initializeGlobal()
	{
		try 
		{
			String url = "jdbc:sqlite:osm.db";
			// create a connection to the database
			conn = DriverManager.getConnection(url);
			System.out.println("Connection to database has been eshtablished.");
		}catch(SQLException e){
			System.out.println(e.getMessage());
		}
	}
	
	@Before
	public void initializeResources() {}
	
	@Test
//	public void setWaysShouldReturnListOfWays() 
//	{
//		// check whether the given list is empty or not 
//		Assert.assertFalse(tester.getWays().isEmpty());
//	}
	
	public void CheckGetWaysInRelation() 
	{
//		HandleRelation hr = new HandleRelation();
//		
//		List<Integer> ways1 = new ArrayList<>(Arrays.asList(5150372, 8160806, 8160807, 8160808)); 
//		Assert.assertEquals(ways1, hr.getWaysInRelation(1148));
	}
	
	@AfterClass
	public static void closeResources()
	{
		try 
		{
			if(conn != null) {
				conn.close();
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
