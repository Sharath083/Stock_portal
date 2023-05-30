package Methods;


import java.sql.SQLException;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

			



public class Listner implements ServletContextListener {
	static DataSource ds;
	static Logger log = Logger.getLogger("Listner");     
    public void contextInitialized(ServletContextEvent sce)  { 
//    	Connection connection = null;
    		UserMethods.logMethod();
    		try {			
    			Context initContext = new InitialContext();
    			ds=(DataSource) initContext.lookup("java:comp/env/jdbc/mydb");
    		}catch (Exception e) {
    			log.warning("Unable to start lookup");
//    			log.info(e.printStackTrace());
    		}
//    		try {
//    			
//    			System.out.println("In ds connection ");
//				connection=ds.getConnection();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
    		
    	
    	ServletContext sc=sce.getServletContext();
    	DataBase baseUtil=new DataBase();
    	sc.setAttribute("sqlEmployee", baseUtil);
    	     		
    }
    public void contextDestroyed(ServletContextEvent sce)  { 
		UserMethods.logMethod();

    	  try {
			DataBase.connection.close();
            log.info("DataBase Connection is closed ");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	        log.warning("Unable to close the connection");

		}                
   }
	
}