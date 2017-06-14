package ch.swing.persistence.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Kontrollerklasse für die Interaktion mit der Datenbank
 * 
 * @author Yannis Portmann
 *
 */
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
			logger.error(e.getMessage());
		}
	}

	/**
	 * Speichert das PDF in der Datenbank
	 * 
	 * @param medicationFile
	 * @param idPatient
	 */
	public void saveMedication(byte[] medicationFile, int idPatient) {

		PreparedStatement pstmt = null;
		try {
			String INSERT_RECORD = "insert into dbo.medication(idPatient, medication, creationDate) values(?, ?, ?)";
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
			logger.info("Medication from Patient: " + idPatient + " updated");

		} catch (SQLException err) {
			logger.error(err.getMessage());
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException err) {
					logger.error(err.getMessage());
				}
			}
		}
	}

	/**
	 * Holt die Medikation zu einem bestimmten Patienten aus der Datenbank
	 * 
	 * @param patientId
	 * @return
	 */
	public List<byte[]> getMedication(int patientId) {
		Statement stmt = null;
		String query = "select * from dbo.Medication where idPatient=" + patientId;
		List<byte[]> medList = new ArrayList<byte[]>();
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				byte[] medication = rs.getBytes("medication");
				medList.add(medication);
			}
			return medList;
		} catch (SQLException err) {
			logger.error(err.getMessage());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException err) {
					logger.error(err.getMessage());
				}
			}
		}
		return null;
	}

	/**
	 * Löscht eine bestimmte Medikation aus der Datenbank
	 * 
	 * @param medicationFile
	 * @param idPatient
	 */
	public void deleteMedication(byte[] medicationFile, int idPatient) {
		PreparedStatement pstmt = null;
		try {
			String INSERT_RECORD = "delete frob dbo.medication(idPatient, medication, creationDate) values(?, ?, ?)";
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
			logger.info("Medication from Patient: " + idPatient + " updated");

		} catch (SQLException err) {
			logger.error(err.getMessage());
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException err) {
					logger.error(err.getMessage());
				}
			}
		}
	}
}
