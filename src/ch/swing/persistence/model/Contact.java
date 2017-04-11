package ch.swing.persistence.model;

import java.util.Date;

public class Contact {

	private int contactId;
	private String title;
	private String givenName;
	private String familyName;
	private String road;
	private String city;
	private int gender;
	private String organization;
	private String period;
	private Telecom telecom;
	private Date creationDate;
	private Date lastUpdate;
	private Date deletionDate;
	
	public int getContactId() {
		return contactId;
	}
	public void setContactId(int contactId) {
		this.contactId = contactId;
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
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public Telecom getTelecom() {
		return telecom;
	}
	public void setTelecom(Telecom telecom) {
		this.telecom = telecom;
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
