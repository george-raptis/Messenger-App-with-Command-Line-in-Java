package project;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
	
	static Connection connection;
	DriverManager dm;
	Statement stm;
	
	public Database() {
		// TODO Auto-generated constructor stub
	}
	
	public static java.sql.Connection connect(String _DB_URL, String _username, String _password) {
		try {
			connection = DriverManager.getConnection(_DB_URL, _username, _password);
			return connection;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
