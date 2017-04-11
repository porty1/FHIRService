package ch.swing.persistence.model;

import java.util.Date;

public class Observation {

	private int observationId;
	private Date effectiveDate;
	private int value;
	private String code;
	private Patient patient;
	private Date creationDate;
	private Date lastUpdate;
	private Date deletionDate;
	public int getObservationId() {
		return observationId;
	}
	public void setObservationId(int observationId) {
		this.observationId = observationId;
	}
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
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
