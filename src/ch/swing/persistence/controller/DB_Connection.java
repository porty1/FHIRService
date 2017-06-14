package ch.swing.persistence.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.swing.helper.Configuration;

/**
 * Helper Klasse um die Datenbankverbindung herzustellen
 * 
 */
public class DB_Connection {
	private static DB_Connection dc = null;
	final static Logger logger = Logger.getLogger(DB_Connection.class);

	public static DB_Connection getInstance() {

		if (dc == null) {
			dc = new DB_Connection();
		}
		return dc;
	}

	public Connection getConnection() throws SQLException {
		Connection connection = DriverManager.getConnection(Configuration.DBURL, Configuration.DBUSER,
				Configuration.DBPASSWORD);
		logger.info("The connection is successfully obtained");
		return connection;
	}
}
