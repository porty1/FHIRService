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
			logger.error(e.getMessage());
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
				Long smisObservationId = rs.getLong("smisObservationId");

				observation.setObservationId(observationId);
				observation.setEffectiveDate(effectiveDate);
				observation.setValue(value);
				observation.setCode(code);
				observation.setPatient(getPatient(idPatient));
				observation.setCreationDate(creationDate);
				observation.setLastUpdate(lastUpdate);
				observation.setDeletionDate(deletionDate);
				observation.setSendDate(sendDate);
				observation.setSmisObservationId(smisObservationId);

				observationList.add(observation);
			}
			return observationList;
		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
			}
		}
		return null;

	}

	public void updateSendDateObservation(int patientId) {
		PreparedStatement pstmt = null;
		try {
			String update_record = "UPDATE dbo.Observation SET sendDate = ? WHERE idPatient = ?";
			pstmt = connection.prepareStatement(update_record);

			java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
			pstmt.setDate(1, sqlDate);
			pstmt.setInt(2, patientId);

			pstmt.executeUpdate();
			logger.info("Observation - Patient with ID:" + patientId + " was succesfully updated new sendDate");

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

	public void updateSendDateNursingReport(int patientId) {
		PreparedStatement pstmt = null;
		try {
			String update_record = "UPDATE dbo.NursingReport SET sendDate = ? WHERE idPatient = ?";
			pstmt = connection.prepareStatement(update_record);

			java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
			pstmt.setDate(1, sqlDate);
			pstmt.setInt(2, patientId);

			pstmt.executeUpdate();
			logger.info("Observation: Patient with ID:" + patientId + " was succesfully updated new sendDate");

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

	public void updateSMISIDNursingReport(int patientId, long smisNursingReportId) {
		PreparedStatement pstmt = null;
		try {
			String update_record = "UPDATE dbo.NursingReport SET smisObservationId = ? WHERE idPatient = ?";
			pstmt = connection.prepareStatement(update_record);

			pstmt.setLong(1, smisNursingReportId);
			pstmt.setInt(2, patientId);

			pstmt.executeUpdate();
			logger.info("NursingReport: " + smisNursingReportId + " with PatientID:" + patientId
					+ " was succesfully updated new smisObservationId");

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

	public void updateSMISIDObservation(int patientId, long smisObservationId) {
		PreparedStatement pstmt = null;
		try {
			String update_record = "UPDATE dbo.Observation SET smisObservationId = ? WHERE idPatient = ?";
			pstmt = connection.prepareStatement(update_record);

			pstmt.setLong(1, smisObservationId);
			pstmt.setInt(2, patientId);

			pstmt.executeUpdate();
			logger.info("Observation: " + smisObservationId + " with PatientID:" + patientId
					+ " was succesfully updated (smisObservationId)");
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
