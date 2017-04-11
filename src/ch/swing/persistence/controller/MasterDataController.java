package ch.swing.persistence.controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ch.swing.persistence.model.Contact;
import ch.swing.persistence.model.InsuranceCard;
import ch.swing.persistence.model.Patient;
import ch.swing.persistence.model.Telecom;

/**
 * This class implements all methods which are used in the CaseView GUI to communicate with the database. There are getter methods<br>
 * and update methods implemented in JDBC. <br>
 * <br>
 * 
 * @author Shpend Vladi<br>
 * <br>
 * 
 *         instance variables:<br>
 *         - PatientCaseController pc <br>
 *         - DB_Connection dc <br>
 *         - Connection connection <br>
 * <br>
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
 * <br>
 */

public class MasterDataController {
	// Defining the objects that are needed to create the methods
		private static MasterDataController pc = null;
		private static DB_Connection dc = DB_Connection.getInstance();
		private static Connection connection = null;
		
		/**
		 * With the getInstance method we make sure that only one object of the PatientCaseController will be used<br>
		 * <br>
		 *
		 * @param none
		 * @return pc
		 * 
		 * <br>
		 */
		public static MasterDataController getInstance() {
			
			if (pc == null) {
				pc = new MasterDataController();
			}
			return pc;
		}
		
		/**
		 * PatientCaseController constructor<br>
		 * gets the connection with the database which is used for the methods<br>
		 * <br>
		 *
		 * @param none
		 * @exception the SQL Exception is catched and shows if the connection failed
		 * 
		 * <br>
		 */
		// Static block for initialization
		public MasterDataController() {
		

			try {
				connection = dc.getConnection();
			} catch (SQLException e) {
				System.err.println("There was an error getting the connection: "
						+ e.getMessage());
			}

		
		}
		
		/**
		 * The getPersonName method gets the Firstname and Last name of the Person from the pid and puts it to one String together.<br>
		 * <br>
		 * @param pid - the parameter which should be given to the method to find the right Person name
		 * @return name - name of the Person which fits with the pid in the database will be returned
		 * @exception the SQL Excepion is catched and shows if the connection failed
		 * 
		 * <br>
		 */
		public  List<Patient> getMasterDataChanges ()
			   {
				Patient patient = new Patient();
				InsuranceCard insuranceCard = new InsuranceCard();
				List<Patient> patientList = new ArrayList<Patient>();
				
			    Statement stmt = null;
			    String query = "select * from Patient where CreationDate="+"NULL";
			    try {
			    	
			        stmt = connection.createStatement();
			        ResultSet rs = stmt.executeQuery(query);
			        while (rs.next()) {
			            int 	patientId = rs.getInt("patientId");
			            int		swingPatientId = rs.getInt("swingPatientId");
			            int	 	smisPatientId = rs.getInt("smisPatientId");
			            int 	idSocialInsuranceCard = rs.getInt("idSocialInsuranceCard");
			            int 	active = rs.getInt("active");
			            String 	title = rs.getString("title");
			            String 	givenName = rs.getString("givenName");
			            String 	familyName = rs.getString("familyName");
			            int 	idTelecom = rs.getInt("idTelecom");
			            int		gender = rs.getInt("gender");
			            Date	birthDate = rs.getDate("birthDate");
			            Date	deceasedDate = rs.getDate("deceasedDate");
			            String 	road = rs.getString("road");
			            String 	city = rs.getString("city");
			            int		postalCode = rs.getInt("postalCode");
			            String 	country = rs.getString("country");
			            String 	communicationLanguage = rs.getString("communicationLanguage");
			            int		idGeneralPractitioner = rs.getInt("idGeneralPractitioner");
			            String 	managingOrganziation = rs.getString("managingOrganziation");
			            Date 	creationDate = rs.getDate("creationDate");
			            Date 	lastUpdate = rs.getDate("lastUpdate");
			            Date 	deletionDate = rs.getDate("deletionDate");
			            
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
			            
			           
			            
			           
			            
			            
			            patientList.add(patient);
			            
			            return patientList;
			        }
			    } catch (SQLException err ) {
			    	System.out.println(err.getMessage());
			    } finally {
			        if (stmt != null) { try {
						stmt.close();
					} catch (SQLException err) {
						System.out.println(err.getMessage());
					} }
			    }
				return null;
				
			}
		
		public  InsuranceCard getInsuranceCard (int insuranceCardId)
		   {
			InsuranceCard insuranceCard = new InsuranceCard();
			
		    Statement stmt = null;
		    String query = "select * from InsuranceCard where insuranceCardId="+insuranceCardId;
		    try {
		    	
		        stmt = connection.createStatement();
		        ResultSet rs = stmt.executeQuery(query);
		        while (rs.next()) {
		            int 	InsuranceCardID = rs.getInt("insuranceCardId");
		            int		cardNumber = rs.getInt("cardNumber");
		            Date 	validFrom = rs.getDate("smisPatientId");
		            Date 	validTo = rs.getDate("valdiFrom");
		            String 	insurance = rs.getString("validFrom");
		            Date 	creationDate = rs.getDate("creationDate");
		            Date 	lastUpdate = rs.getDate("lastUpdate");
		            Date 	deletionDate = rs.getDate("deletionDate");
		            
		            insuranceCard.setInsuranceCardId(InsuranceCardID);;
		            insuranceCard.setCardNumber(cardNumber);
		            insuranceCard.setValidFrom(validFrom);
		            insuranceCard.setValidTo(validTo);
		            insuranceCard.setInsurance(insurance);
		            insuranceCard.setCreationDate(creationDate);
		            insuranceCard.setLastUpdate(lastUpdate);
		            insuranceCard.setDeletionDate(deletionDate);           
		            
		            return insuranceCard;
		        }
		    } catch (SQLException err ) {
		    	System.out.println(err.getMessage());
		    } finally {
		        if (stmt != null) { try {
					stmt.close();
				} catch (SQLException err) {
					System.out.println(err.getMessage());
				} }
		    }
			return null;
			
		}
		
		public  Telecom getTelecom (int telecomId)
		   {
			Telecom telecom = new Telecom();
			
		    Statement stmt = null;
		    String query = "select * from Telecom where telecomId="+telecomId;
		    try {
		    	
		        stmt = connection.createStatement();
		        ResultSet rs = stmt.executeQuery(query);
		        while (rs.next()) {
		        	
		            int 	idTelecom = rs.getInt("telecomId");
		            int		system = rs.getInt("system");
		            int 	value = rs.getInt("value");
		            Date 	telecomUse = rs.getDate("telecomUse");
		            Date 	creationDate = rs.getDate("creationDate");
		            Date 	lastUpdate = rs.getDate("lastUpdate");
		            Date 	deletionDate = rs.getDate("deletionDate");
		            
		            
		            telecom.setTelecomId(idTelecom);
		            telecom.setSystem(system);
		            telecom.setValue(value);
		            telecom.setTelecomUse(telecomUse);
		            telecom.setCreationDate(creationDate);
		            telecom.setLastUpdate(lastUpdate);
		            telecom.setDeletionDate(deletionDate);
		            
		            
		            return telecom;
		        }
		    } catch (SQLException err ) {
		    	System.out.println(err.getMessage());
		    } finally {
		        if (stmt != null) { try {
					stmt.close();
				} catch (SQLException err) {
					System.out.println(err.getMessage());
				} }
		    }
			return null;
			
		}
		
		public  Contact getGeneralPractitioner (int contactId)
		   {
			Contact contact = new Contact();
			
		    Statement stmt = null;
		    String query = "select * from Telecom where contactId="+contactId;
		    try {
		    	
		        stmt = connection.createStatement();
		        ResultSet rs = stmt.executeQuery(query);
		        while (rs.next()) {
		        	
		            int 	idContact = rs.getInt("contactId");
		            String 	title = rs.getString("title");
		            String 	givenName = rs.getString("givenName");
		            String 	familyName = rs.getString("familyName");
		            String 	road = rs.getString("road");
		            String 	city = rs.getString("city");
		            int		postalCode = rs.getInt("postalCode");
		            String 	country = rs.getString("country");
		            int		gender = rs.getInt("gender");
		            String	organization = rs.getString("organization");
		            String	period = rs.getString("period");
		            int		idTelecom = rs.getInt("idTelecom");
		            Date 	creationDate = rs.getDate("creationDate");
		            Date 	lastUpdate = rs.getDate("lastUpdate");
		            Date 	deletionDate = rs.getDate("deletionDate");
		          
		            contact.setContactId(idContact);
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
		    } catch (SQLException err ) {
		    	System.out.println(err.getMessage());
		    } finally {
		        if (stmt != null) { try {
					stmt.close();
				} catch (SQLException err) {
					System.out.println(err.getMessage());
				} }
		    }
			return null;
			
		}
}
