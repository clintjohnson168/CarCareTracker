import java.sql.Connection;
import java.sql.DriverManager;

public class MySQLConnection {
	private Connection connection = null;
	private boolean isSucessful = false;
	
	public MySQLConnection(String user, String password) {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://bananaserver.hopto.org:3306/CarDB?" + 
					"//media/Shares/databases/mysql/CarDB", user, password);
			
			System.out.println("Connection Successful");
			isSucessful = true;
			
		} catch (Exception e) {
			System.out.println("Incorrect username and password combination");
			//e.printStackTrace();
		}		
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public boolean getSucessfulConnection() {
		return isSucessful;
	}
}