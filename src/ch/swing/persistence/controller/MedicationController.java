package ch.swing.persistence.controller;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class MedicationController {
	final static Logger logger = Logger.getLogger(MedicationController.class);

	private static MedicationController mc = null;
	private static DB_Connection dc = DB_Connection.getInstance();
	private static Connection connection = null;

	public static MedicationController getInstance() {
		if (mc == null) {
			mc = new MedicationController();
		}
		return mc;
	}

	public MedicationController() {
		try {
			connection = dc.getConnection();
		} catch (SQLException e) {
			System.err.println("There was an error getting the connection: " + e.getMessage());
		}
	}

	/**
	 * Save all the medication PDF's in the database
	 * 
	 * @param in
	 * @param idPatient
	 */
	public void saveMedication(InputStream in, int idPatient) {
		String query = "INSERT INTO medication (medication, idPatient) VALUES (" + in + "," + idPatient + ");";
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			// stmt.executeQuery(query);
			stmt.executeQuery(query);

		} catch (SQLException err) {
			System.out.println(err.getMessage());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException err) {
					System.out.println(err.getMessage());
				}
			}
		}
	}

}
