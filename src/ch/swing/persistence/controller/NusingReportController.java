package ch.swing.persistence.controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ch.swing.persistence.model.NursingReport;

public class NusingReportController extends MasterDataController {

	private static NusingReportController nc = null;
	private static DB_Connection dc = DB_Connection.getInstance();
	private static Connection connection = null;

	public static NusingReportController getInstance() {

		if (nc == null) {
			nc = new NusingReportController();
		}
		return nc;
	}

	public NusingReportController() {
		try {
			connection = dc.getConnection();
		} catch (SQLException e) {
			System.err.println("There was an error getting the connection: " + e.getMessage());
		}
	}

	/**
	 * 
	 * @return
	 */
	public List<NursingReport> getNursingReportChanges() {
		List<NursingReport> nursingReportList = new ArrayList<NursingReport>();
		Statement stmt = null;
		String query = "select * from NursingReport where sendDate IS NULL";
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				NursingReport nursingReport = new NursingReport();
				int nursingReportId = rs.getInt("nursingReportId");
				Date nursingReportDate = rs.getDate("nursingReportDate");
				String value = rs.getString("value");
				int idPatient = rs.getInt("idPatient");
				Date creationDate = rs.getDate("creationDate");
				Date lastUpdate = rs.getDate("lastUpdate");
				Date deletionDate = rs.getDate("deletionDate");
				Date sendDate = rs.getDate("sendDate");

				nursingReport.setNursingReportId(nursingReportId);
				nursingReport.setNursingReportDate(nursingReportDate);
				nursingReport.setValue(value);
				nursingReport.setPatient(getPatient(idPatient));
				nursingReport.setCreationDate(creationDate);
				nursingReport.setLastUpdate(lastUpdate);
				nursingReport.setDeletionDate(deletionDate);
				nursingReport.setSendDate(sendDate);

				nursingReportList.add(nursingReport);
			}
			return nursingReportList;
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
		return null;

	}

	/**
	 * 
	 * @param patientId
	 */
	public void updateSendDate(int patientId) {
		PreparedStatement pstmt = null;
		try {
			String update_record = "UPDATE dbo.NursingReport SET sendDate = ? WHERE idPatient = ?";
			pstmt = connection.prepareStatement(update_record);

			java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
			pstmt.setDate(1, sqlDate);
			pstmt.setInt(2, patientId);

			pstmt.executeUpdate();
			logger.info("Patient with ID:" + patientId + " was succesfully updated (sendDate)");

		} catch (SQLException err) {
			logger.error(err.getStackTrace());
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException err) {
					logger.error(err.getStackTrace());
				}
			}
		}
	}
}
