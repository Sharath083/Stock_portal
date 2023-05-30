package Methods;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DataBase {
	static Logger log = Logger.getLogger("DataBase");     
	static Connection connection;

//	static DataSource ds;
//	public  static DataSource dataSource() {
//		
//		try {			
//			Context initContext = new InitialContext();
//			ds=(DataSource) initContext.lookup("java:comp/env/jdbc/mydb");
//		}catch (Exception e) {
//			System.out.println("data base error");
//			System.out.println(e);
//		}
//		return ds;
//		
//	}
//		

	public Connection getConnector(String name) {
		UserMethods.logMethod();
		try {
			connection=Listner.ds.getConnection();
			log.info("getting connection for "+name);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.warning("Error in databaseutil");
		}
		return connection;
	}

}