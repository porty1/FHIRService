package ch.swing.persistence.controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ch.swing.persistence.model.Contact;
import ch.swing.persistence.model.InsuranceCard;
import ch.swing.persistence.model.Patient;
import ch.swing.persistence.model.Telecom;

public class MasterDataController {
	final static Logger logger = Logger.getLogger(MasterDataController.class);

	private static MasterDataController mc = null;
	private static DB_Connection dc = DB_Connection.getInstance();
	private static Connection connection = null;

	public static MasterDataController getInstance() {

		if (mc == null) {
			mc = new MasterDataController();
		}
		return mc;
	}

	public MasterDataController() {
		try {
			connection = dc.getConnection();
		} catch (SQLException e) {
			logger.error("There was an error getting the connection: " + e.getMessage());
		}

	}

	public List<Patient> getMasterDataChanges() {

		List<Patient> patientList = new ArrayList<Patient>();

		Statement stmt = null;
		String query = "select * from dbo.Patient WHERE creationDate IS NULL";
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				Patient patient = new Patient();
				int patientId = rs.getInt("patientId");
				int swingPatientId = rs.getInt("swingPatientId");
				Long smisPatientId = rs.getLong("smisPatientId");
				int idSocialInsuranceCard = rs.getInt("idSocialInsuranceCard");
				int active = rs.getInt("active");
				String title = rs.getString("title");
				String givenName = rs.getString("givenName");
				String familyName = rs.getString("familyName");
				// int idTelecom = rs.getInt("idTelecom");
				int gender = rs.getInt("gender");
				Date birthDate = rs.getDate("birthDate");
				Date deceasedDate = rs.getDate("deceasedDate");
				String road = rs.getString("road");
				String city = rs.getString("city");
				int postalCode = rs.getInt("postalCode");
				String country = rs.getString("country");
				String communicationLanguage = rs.getString("communicationLanguage");
				int idGeneralPractitioner = rs.getInt("idGeneralPractitioner");
				String managingOrganization = rs.getString("managingOrganization");
				Date creationDate = rs.getDate("creationDate");
				Date lastUpdate = rs.getDate("lastUpdate");
				Date deletionDate = rs.getDate("deletionDate");
				String telecom = rs.getString("telecom");
				String socialInsuranceNumber = rs.getString("socialInsuranceNumber");

				patient.setPatientId(patientId);
				patient.setSwingPatientId(swingPatientId);
				patient.setSmisPatientId(smisPatientId);
				patient.setInsuranceCard(getInsuranceCard(idSocialInsuranceCard));
				patient.setActive(active);
				patient.setTitle(title);
				patient.setGivenName(givenName);
				patient.setFamilyName(familyName);
				patient.setGender(gender);
				patient.setBirthDate(birthDate);
				patient.setDeceasedDate(deceasedDate);
				patient.setRoad(road);
				patient.setCity(city);
				patient.setPostalCode(postalCode);
				patient.setCountry(country);
				patient.setCommunicationLanguage(communicationLanguage);
				patient.setGeneralPractitioner(getGeneralPractitioner(idGeneralPractitioner));
				patient.setManagingOrganization(managingOrganization);
				patient.setCreationDate(creationDate);
				patient.setLastUpdate(lastUpdate);
				patient.setDeletionDate(deletionDate);
				patient.setTelecom(telecom);
				patient.setSocialInsuranceNumber(socialInsuranceNumber);

				patientList.add(patient);
			}
			return patientList;

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

	public Patient getPatient(int idPatient) {
		Statement stmt = null;
		String query = "select * from Patient where patientId=" + idPatient;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				Patient patient = new Patient();
				int patientId = rs.getInt("patientId");
				int swingPatientId = rs.getInt("swingPatientId");
				Long smisPatientId = rs.getLong("smisPatientId");
				int idSocialInsuranceCard = rs.getInt("idSocialInsuranceCard");
				int active = rs.getInt("active");
				String title = rs.getString("title");
				String givenName = rs.getString("givenName");
				String familyName = rs.getString("familyName");
				// int idTelecom = rs.getInt("idTelecom");
				int gender = rs.getInt("gender");
				Date birthDate = rs.getDate("birthDate");
				Date deceasedDate = rs.getDate("deceasedDate");
				String road = rs.getString("road");
				String city = rs.getString("city");
				int postalCode = rs.getInt("postalCode");
				String country = rs.getString("country");
				String communicationLanguage = rs.getString("communicationLanguage");
				int idGeneralPractitioner = rs.getInt("idGeneralPractitioner");
				String managingOrganization = rs.getString("managingOrganization");
				Date creationDate = rs.getDate("creationDate");
				Date lastUpdate = rs.getDate("lastUpdate");
				Date deletionDate = rs.getDate("deletionDate");
				String telecom = rs.getString("telecom");
				String socialInsuranceNumber = rs.getString("socialInsuranceNumber");

				patient.setPatientId(patientId);
				patient.setSwingPatientId(swingPatientId);
				patient.setSmisPatientId(smisPatientId);
				patient.setInsuranceCard(getInsuranceCard(idSocialInsuranceCard));
				patient.setActive(active);
				patient.setTitle(title);
				patient.setGivenName(givenName);
				patient.setFamilyName(familyName);
				patient.setTelecom(telecom);
				patient.setGender(gender);
				patient.setBirthDate(birthDate);
				patient.setDeceasedDate(deceasedDate);
				patient.setRoad(road);
				patient.setCity(city);
				patient.setPostalCode(postalCode);
				patient.setCountry(country);
				patient.setCommunicationLanguage(communicationLanguage);
				patient.setGeneralPractitioner(getGeneralPractitioner(idGeneralPractitioner));
				patient.setManagingOrganization(managingOrganization);
				patient.setCreationDate(creationDate);
				patient.setLastUpdate(lastUpdate);
				patient.setDeletionDate(deletionDate);
				patient.setSocialInsuranceNumber(socialInsuranceNumber);

				return patient;
			}
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
	 * @param insuranceCardId
	 * @return
	 */
	public InsuranceCard getInsuranceCard(int insuranceCardId) {
		Statement stmt = null;
		String query = "select * from dbo.InsuranceCard where insuranceCardId=" + insuranceCardId;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				InsuranceCard insuranceCard = new InsuranceCard();
				int InsuranceCardID = rs.getInt("insuranceCardId");
				String cardNumber = rs.getString("cardNumber");
				Date validFrom = rs.getDate("validFrom");
				Date validTo = rs.getDate("validTo");
				String insurance = rs.getString("insurance");
				Date creationDate = rs.getDate("creationDate");
				Date lastUpdate = rs.getDate("lastUpdate");
				Date deletionDate = rs.getDate("deletionDate");

				insuranceCard.setInsuranceCardId(InsuranceCardID);
				insuranceCard.setCardNumber(cardNumber);
				insuranceCard.setValidFrom(validFrom);
				insuranceCard.setValidTo(validTo);
				insuranceCard.setInsurance(insurance);
				insuranceCard.setCreationDate(creationDate);
				insuranceCard.setLastUpdate(lastUpdate);
				insuranceCard.setDeletionDate(deletionDate);

				return insuranceCard;
			}
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
	 * @param telecomId
	 * @return
	 */
	public Telecom getTelecom(int telecomId) {

		Statement stmt = null;
		String query = "select * from Telecom where telecomId=" + telecomId;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				Telecom telecom = new Telecom();
				int idTelecom = rs.getInt("telecomId");
				String system = rs.getString("system");
				String value = rs.getString("value");
				String telecomUse = rs.getString("telecomUse");
				Date creationDate = rs.getDate("creationDate");
				Date lastUpdate = rs.getDate("lastUpdate");
				Date deletionDate = rs.getDate("deletionDate");

				telecom.setTelecomId(idTelecom);
				telecom.setSystem(system);
				telecom.setValue(value);
				telecom.setTelecomUse(telecomUse);
				telecom.setCreationDate(creationDate);
				telecom.setLastUpdate(lastUpdate);
				telecom.setDeletionDate(deletionDate);

				return telecom;
			}
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
	 * @param cId
	 * @return
	 */
	public Contact getGeneralPractitioner(int cId) {
		Statement stmt = null;
		String query = "select * from Contact where contactId=" + cId;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				Contact contact = new Contact();
				int contactId = rs.getInt("contactId");
				String title = rs.getString("title");
				String givenName = rs.getString("givenName");
				String familyName = rs.getString("familyName");
				String road = rs.getString("road");
				String city = rs.getString("city");
				int postalCode = rs.getInt("postalCode");
				String country = rs.getString("country");
				int gender = rs.getInt("gender");
				String organization = rs.getString("organization");
				String period = rs.getString("period");
				int idTelecom = rs.getInt("idTelecom");
				Date creationDate = rs.getDate("creationDate");
				Date lastUpdate = rs.getDate("lastUpdate");
				Date deletionDate = rs.getDate("deletionDate");

				contact.setContactId(contactId);
				contact.setTitle(title);
				contact.setGivenName(givenName);
				contact.setFamilyName(familyName);
				contact.setRoad(road);
				contact.setCity(city);
				contact.setPostalCode(postalCode);
				contact.setCountry(country);
				contact.setGender(gender);
				contact.setOrganization(organization);
				contact.setPeriod(period);
				contact.setTelecom(getTelecom(idTelecom));
				contact.setCreationDate(creationDate);
				contact.setLastUpdate(lastUpdate);
				contact.setDeletionDate(deletionDate);

				return contact;
			}
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
	 * @return
	 */
	public List<Patient> getIDPatients() {
		ArrayList<Patient> patientList = new ArrayList<Patient>();
		Statement stmt = null;
		String query = "select * from Patient";
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			logger.debug(rs.toString());
			while (rs.next()) {
				Patient patient = new Patient();
				int patientId = rs.getInt("patientId");
				int swingPatientId = rs.getInt("swingPatientId");
				Long smisPatientId = rs.getLong("smisPatientId");

				patient.setPatientId(patientId);
				patient.setSwingPatientId(swingPatientId);
				patient.setSmisPatientId(smisPatientId);

				patientList.add(patient);
			}

			return patientList;
		} catch (SQLException err) {
			logger.error(err.getStackTrace());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException err) {
					logger.error(err.getStackTrace());
				}
			}
		}
		return null;

	}

	/**
	 * Updates the SMIS Identifier in the Connection Database
	 * 
	 * @param SMISID
	 * @param patientId
	 */
	public void updateIdentifier(long SMISID, int patientId) {

		PreparedStatement pstmt = null;
		try {
			String insert_record = "UPDATE dbo.Patient SET smisPatientId = ? WHERE patientId = ?";
			pstmt = connection.prepareStatement(insert_record);

			pstmt.setLong(1, SMISID);
			pstmt.setInt(2, patientId);
			pstmt.executeUpdate();
			logger.info("Patient with ID:" + patientId + " was succesfully updated - new SMIS ID " + SMISID);

		} catch (SQLException err) {
			logger.error(err.getStackTrace());
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
	 * Updates the creation Date in the Patients record
	 * 
	 * @param patientId
	 */
	public void updateCreationDate(int patientId) {
		PreparedStatement pstmt = null;
		try {
			String update_record = "UPDATE dbo.Patient SET creationDate = ? WHERE patientId = ?";
			pstmt = connection.prepareStatement(update_record);

			java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
			pstmt.setDate(1, sqlDate);
			pstmt.setInt(2, patientId);

			pstmt.executeUpdate();
			logger.info("Patient with ID:" + patientId + " was succesfully updated - new creationDate");

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
