package ch.swing.persistence.controller;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ch.swing.persistence.controller.DB_Connection;;

/**
 * This class helps to get the connection to the database.<br>
 * 
 * @author Shpend Vladi<br>
 * <br>
 * 
 *         instance variables:<br>
 *         - DB_Connection dc <br>
 *         - Connection connection <br>
 *         - DatabaseMetaData metadata <br>
 *         
 * <br>
 * 
 *         Methods:<br>
 *         - printGeneralMetadata()<br>
 *         - getTablesMetadata()<br>
 *         - getColumnsMetadata(ArrayList<String> tables)<br>
 *         	
 * <br>
 */

public class DB_MetaData {
	static DB_Connection dc = DB_Connection.getInstance();
	static Connection connection = null;
	static DatabaseMetaData metadata = null;


	/**
	 * The static nlock for the initalization<br>
	 * <br>
	 *
	 * @exception SQL exception is catched and shown if the conncetion fails<br>
	 * 
	 * <br>
	 */
	static {

		try {
			connection = dc.getConnection();
		} catch (SQLException e) {
			System.err.println("There was an error getting the connection: "
					+ e.getMessage());
		}
		try {
			metadata = connection.getMetaData();
		} catch (SQLException e) {
			System.err.println("There was an error getting the metadata: "
					+ e.getMessage());
		}

	}
	
	
	/**
	 * The printGeneralMetadata() prints the general Information about the database in the console we connect into<br>
	 * <br>
	 *
	 * @throws SQL exception if the connection fails<br>
	 * 
	 * <br>
	 */
	public static void printGeneralMetadata() throws SQLException {
			        System.out.println("Database Product Name: " + metadata.getDatabaseProductName());
		
			        System.out.println("Database Product Version: " + metadata.getDatabaseProductVersion());
		
			        System.out.println("Logged User: " + metadata.getUserName());
		
			        System.out.println("JDBC Driver: " + metadata.getDriverName());
		
			        System.out.println("Driver Version: " + metadata.getDriverVersion());
		
			        System.out.println("\n");
		
			    }
	
	/**
	 * The getTablesMetadata() returns an ArrayList of the type String from the table data of the database<br>
	 * <br>
	 *
	 * @throws SQL exception - if the connection fails<br>
	 * @return tables - returns the tables of the database in an ArrayList<br>
	 * 
	 * <br>
	 */

	// Returnig with an Arraylist the tables names
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
	
	/**
	 * The getColumnsMetadata(ArrayList<String> tables) gets the columns metadata of the database<br>
	 * <br>
	 *
	 * @throws SQL exception - if the connection fails<br>
	 * @param tables - the ArrayList of the Type String which should be given to the method for finding the columns of the table<br>
	 * 
	 * <br>
	 */
	public static void getColumnsMetadata(ArrayList<String> tables)
	
		            throws SQLException {
	
		        ResultSet rs = null;
	
		        // Print the columns properties of the actual table
	
		        for (String actualTable : tables) {
	
		            rs = metadata.getColumns(null, null, actualTable, null);
	
		            System.out.println(actualTable.toUpperCase());
	
		            while (rs.next()) {
		                System.out.println(rs.getString("COLUMN_NAME") + " "
		                        + rs.getString("TYPE_NAME") + " "
		                        + rs.getString("COLUMN_SIZE"));
		            }
		            System.out.println("\n");
		        }
		    }
	
	
		// showing the metadata in the console in the main method
	 public static void main(String[] args) {
		 	        try {
		 	            printGeneralMetadata();
		 	            // Print all the tables of the database scheme, with their names and
		 	            // structure
		 	            getColumnsMetadata(getTablesMetadata());
		 	        } catch (SQLException e) {
		 	            System.err
		 	                    .println("There was an error retrieving the metadata properties: "
		 	                            + e.getMessage());
		 	        }
		 	    }


}
