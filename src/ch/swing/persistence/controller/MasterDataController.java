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

/**
 * This class implements all methods which are used in the CaseView GUI to
 * communicate with the database. There are getter methods<br>
 * and update methods implemented in JDBC. <br>
 * <br>
 * 
 * @author Shpend Vladi<br>
 *         <br>
 * 
 *         instance variables:<br>
 *         - PatientCaseController pc <br>
 *         - DB_Connection dc <br>
 *         - Connection connection <br>
 *         <br>
 * 
 *         Methods:<br>
 *         - String getPersonName (int pid)<br>
 *         - String getCaseID (int pid)<br>
 *         - Date getFromDate (int caseID)<br>
 *         - Date getToDate (int caseID)<br>
 *         - String getAnamnesis (int caseID)<br>
 *         - String getDiagnosis (int caseID)<br>
 *         - upDateName(String firstname, String lastname, int pid)<br>
 *         - upDateFromDate(Date FromDate, int caseID)<br>
 *         - upDateToDate(Date ToDate, int caseID)<br>
 *         - upDateAnamnesis(String anamnesis, int caseID)<br>
 *         - upDateDiagnosis(String diagnosis, int caseID)<br>
 * 
 *         <br>
 */

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
			System.err.println("There was an error getting the connection: " + e.getMessage());
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
				int idTelecom = rs.getInt("idTelecom");
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

				patient.setPatientId(patientId);
				patient.setSwingPatientId(swingPatientId);
				patient.setSmisPatientId(smisPatientId);
				patient.setInsuranceCard(getInsuranceCard(idSocialInsuranceCard));
				patient.setActive(active);
				patient.setTitle(title);
				patient.setGivenName(givenName);
				patient.setFamilyName(familyName);
				patient.setTelecom(getTelecom(idTelecom));
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
			logger.debug(rs.toString());
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
				int idTelecom = rs.getInt("idTelecom");
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

				patient.setPatientId(patientId);
				patient.setSwingPatientId(swingPatientId);
				patient.setSmisPatientId(smisPatientId);
				patient.setInsuranceCard(getInsuranceCard(idSocialInsuranceCard));
				patient.setActive(active);
				patient.setTitle(title);
				patient.setGivenName(givenName);
				patient.setFamilyName(familyName);
				patient.setTelecom(getTelecom(idTelecom));
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

	// TODO Methode anpassen!
	public void setCreationDate(int patientId) {

		java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());

		Statement stmt = null;

		// String query = "Update Patient set creationDate="+sqlDate+"where
		// patientId ="+patientId;
		String query2 = "Update Patient set familyName=" + "Hans" + "where patientId =" + patientId;

		try {

			stmt = connection.createStatement();
			// stmt.executeQuery(query);
			stmt.executeQuery(query2);

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
			logger.info("Patient with ID:" + patientId + " was succesfully updated (SMIS ID)");

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
			logger.info("Patient with ID:" + patientId + " was succesfully updated (creationDate)");

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
