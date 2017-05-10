package ch.swing.persistence.model;

import java.util.Date;

/**
 * Model class which represents the patient on the database
 * 
 * @author Yannis Portmann / Shpend Vladi
 *
 */
public class Patient {

	private int patientId;
	private int swingPatientId;
	private int smisPatientId;
	private InsuranceCard insuranceCard;
	private int active;
	private String title;
	private String givenName;
	private String familyName;
	private Telecom telecom;
	// 0=male;1=female
	private int gender;
	private Date birthDate;
	private Date deceasedDate;
	private String road;
	private String city;
	private int postalCode;
	private String country;
	private String communicationLanguage;
	private Contact generalPractitioner;
	private String managingOrganization;
	private Date creationDate;
	private Date lastUpdate;
	private Date deletionDate;

	public int getPatientId() {
		return patientId;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public int getSwingPatientId() {
		return swingPatientId;
	}

	public void setSwingPatientId(int swingPatientId) {
		this.swingPatientId = swingPatientId;
	}

	public int getSmisPatientId() {
		return smisPatientId;
	}

	public void setSmisPatientId(int smisPatientId) {
		this.smisPatientId = smisPatientId;
	}

	public InsuranceCard getInsuranceCard() {
		return insuranceCard;
	}

	public void setInsuranceCard(InsuranceCard insuranceCard) {
		this.insuranceCard = insuranceCard;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public Telecom getTelecom() {
		return telecom;
	}

	public void setTelecom(Telecom telecom) {
		this.telecom = telecom;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Date getDeceasedDate() {
		return deceasedDate;
	}

	public void setDeceasedDate(Date deceasedDate) {
		this.deceasedDate = deceasedDate;
	}

	public String getRoad() {
		return road;
	}

	public void setRoad(String road) {
		this.road = road;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(int postalCode) {
		this.postalCode = postalCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCommunicationLanguage() {
		return communicationLanguage;
	}

	public void setCommunicationLanguage(String communicationLanguage) {
		this.communicationLanguage = communicationLanguage;
	}

	public Contact getGeneralPractitioner() {
		return generalPractitioner;
	}

	public void setGeneralPractitioner(Contact generalPractitioner) {
		this.generalPractitioner = generalPractitioner;
	}

	public String getManagingOrganization() {
		return managingOrganization;
	}

	public void setManagingOrganization(String managingOrganization) {
		this.managingOrganization = managingOrganization;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Date getDeletionDate() {
		return deletionDate;
	}

	public void setDeletionDate(Date deletionDate) {
		this.deletionDate = deletionDate;
	}

}
