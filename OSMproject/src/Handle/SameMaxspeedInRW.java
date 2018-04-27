package Handle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Model.Member;
import Model.Relation;
import Model.Way;

public class SameMaxspeedInRW {
	
	private static SameMaxspeedInRW instance = new SameMaxspeedInRW();
	
	public static SameMaxspeedInRW getInstance() {
		return instance;
	}
	
	private int count = 0;
	
	static final Logger LOG = Logger.getLogger(SameMaxspeedInRW.class.getName());
	
	private List<Integer> issuedways;
	
	Connection con;
	Statement stm;
	
	public void handle(List<Way> ways, List<Relation> relations) {
		
		issuedways = new ArrayList<>();
		
		if(con == null && stm == null) {
			try {
				Class.forName("org.sqlite.JDBC");
				con = DriverManager.getConnection("jdbc:sqlite:osm.db");
				stm = con.createStatement();
				con.setAutoCommit(false);	
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for(Relation re : relations) {
			if(re.maxspeed != 0) {
				
				for(Member m : re.members) {
					
					if(m instanceof Way) {
						
						if(((Way) m).maxspeed != re.maxspeed) {
							count++;
							issuedways.add(((Way) m).id);
						}
					}
				}
			}
			
			try {
				
				String add;
				
				for(int id : issuedways) {
					add = "INSERT INTO maxspeed VALUES('"+re.id+"','"+id+"')";
					stm.executeUpdate(add);
					
				}
				con.commit();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		LOG.log(Level.INFO, "the number that relation and its ways have different max speed: " + count);
	}
	
}
