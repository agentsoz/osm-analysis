import java.sql.Connection;
import java.sql.Statement;

public interface InsertData {
	
	void insert(Statement stm, Connection con);
	
}
