package ch.swing.persistence.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class helps to get the connection to the database.<br>
 * 
 * @author Shpend Vladi<br>
 * <br>
 * 
 *         instance variables:<br>
 *         - String DB_URL <br>
 *         - String DB_USER <br>
 *         - DB_PASSWORD <br>
 *         - DB_Connection dc <br>
 *         
 * <br>
 * 
 *         Methods:<br>
 *         - DB_Connection getInstance()
 *         	
 * <br>
 */
public class DB_Connection {
	// defining database connection information which we need for allowing us the connection
		private static String DB_URL = "jdbc:sqlserver://vmbalios.bfh.ch:1433;DatabaseName=Swing";
		private static String DB_USER = "Swing";
		private static String DB_PASSWORD = "GOLf1720";
		
		private static DB_Connection dc = null;
		
		/**
		 * With the getInstance method we make sure that only one object of the DB_Connection will be used<br>
		 * <br>
		 *
		 * @param none
		 * @return dc
		 * 
		 * <br>
		 */
		public static DB_Connection getInstance() {
			
			if (dc == null) {
				dc = new DB_Connection();
			}
			return dc;
		}
		
		/**
		 * the getConnection method helps us to get the connection with the database with the given informations<br>
		 * <br>
		 *
		 * @param none
		 * @throws SQL Exception
		 * @return connection
		 * 
		 * <br>
		 */
		public Connection getConnection() throws SQLException {
						Connection connection = DriverManager.getConnection(DB_URL, DB_USER,
			                DB_PASSWORD);
						System.err.println("The connection is successfully obtained");
			        return connection;
			    }

}
