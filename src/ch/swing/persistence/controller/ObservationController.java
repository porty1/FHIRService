package ch.swing.persistence.controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ch.swing.persistence.model.Observation;

public class ObservationController extends MasterDataController {

	private static ObservationController oc = null;
	private static DB_Connection dc = DB_Connection.getInstance();
	private static Connection connection = null;

	public static ObservationController getInstance() {

		if (oc == null) {
			oc = new ObservationController();
		}
		return oc;
	}

	public ObservationController() {
		try {
			connection = dc.getConnection();
		} catch (SQLException e) {
			System.err.println("There was an error getting the connection: " + e.getMessage());
		}

	}

	public List<Observation> getObservationChanges() {
		List<Observation> observationList = new ArrayList<Observation>();
		Statement stmt = null;
		String query = "select * from Observation where sendDate IS NULL";
		try {

			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				Observation observation = new Observation();
				int observationId = rs.getInt("observationId");
				Date effectiveDate = rs.getDate("effectiveDate");
				String value = rs.getString("value");
				String code = rs.getString("code");
				int idPatient = rs.getInt("idPatient");
				Date creationDate = rs.getDate("creationDate");
				Date lastUpdate = rs.getDate("lastUpdate");
				Date deletionDate = rs.getDate("deletionDate");
				Date sendDate = rs.getDate("sendDate");

				observation.setObservationId(observationId);
				observation.setEffectiveDate(effectiveDate);
				observation.setValue(value);
				observation.setCode(code);
				observation.setPatient(getPatient(idPatient));
				observation.setCreationDate(creationDate);
				observation.setLastUpdate(lastUpdate);
				observation.setDeletionDate(deletionDate);
				observation.setSendDate(sendDate);

				observationList.add(observation);
			}
			return observationList;
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
	
	public void updateSendDate(int patientId) {
		PreparedStatement pstmt = null;
		try {
			String update_record = "UPDATE dbo.Observation SET sendDate = ? WHERE idPatient = ?";
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
