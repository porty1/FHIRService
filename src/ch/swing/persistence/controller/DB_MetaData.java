package ch.swing.persistence.controller;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import ch.swing.persistence.controller.DB_Connection;;

/**
 * This class helps to get the connection to the database.
 * 
 */
public class DB_MetaData {
	static DB_Connection dc = DB_Connection.getInstance();
	static Connection connection = null;
	static DatabaseMetaData metadata = null;
	final static Logger logger = Logger.getLogger(DB_MetaData.class);

	static {

		try {
			connection = dc.getConnection();
			metadata = connection.getMetaData();
		} catch (SQLException e) {
			logger.error("There was an error getting the connection: " + e.getMessage());
		}
	}

	public static void printGeneralMetadata() throws SQLException {
		logger.info("Database Product Name: " + metadata.getDatabaseProductName());
		logger.info("Database Product Version: " + metadata.getDatabaseProductVersion());
		logger.info("Logged User: " + metadata.getUserName());
		logger.info("JDBC Driver: " + metadata.getDriverName());
		logger.info("Driver Version: " + metadata.getDriverVersion());
	}

	public static ArrayList<String> getTablesMetadata() throws SQLException {
		String table[] = { "TABLE" };
		ResultSet rs = null;
		ArrayList<String> tables = null;

		// receive the Type of the object in a String array.
		rs = metadata.getTables(null, null, null, table);

		tables = new ArrayList<String>();

		while (rs.next()) {
			tables.add(rs.getString("TABLE_NAME"));
		}
		return tables;
	}

	public static void getColumnsMetadata(ArrayList<String> tables) throws SQLException {

		ResultSet rs = null;
		for (String actualTable : tables) {
			rs = metadata.getColumns(null, null, actualTable, null);
			while (rs.next()) {
				logger.info(rs.getString("COLUMN_NAME") + " " + rs.getString("TYPE_NAME") + " "
						+ rs.getString("COLUMN_SIZE"));
			}
		}
	}

	public static void main(String[] args) {
		try {
			printGeneralMetadata();
			getColumnsMetadata(getTablesMetadata());
		} catch (SQLException e) {
			logger.error("There was an error retrieving the metadata properties: " + e.getMessage());
		}
	}
}
