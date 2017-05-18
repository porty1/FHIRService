package ch.swing.persistence.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
	 * @param medicationFile
	 * @param idPatient
	 */
	public void saveMedication(byte[] medicationFile, int idPatient) {

		PreparedStatement pstmt = null;
		try {
			String INSERT_RECORD = "insert into medication(idPatient, medication, creationDate) values(?, ?, ?)";
			pstmt = connection.prepareStatement(INSERT_RECORD);
			// Insert the Patient ID
			pstmt.setInt(1, idPatient);
			// Insert the medication PDF File
			pstmt.setBytes(2, medicationFile);
			// Insert the current Date as creationDate
			java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
			pstmt.setDate(3, sqlDate);
			// Execute the changes
			pstmt.executeUpdate();

		} catch (SQLException err) {
			System.out.println(err.getMessage());
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException err) {
					System.out.println(err.getMessage());
				}
			}
		}
	}

}
