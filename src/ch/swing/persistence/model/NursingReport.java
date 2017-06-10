package ch.swing.persistence.model;

import java.util.Date;

/**
 * Model Class which represent the Nursing Report on the database
 * 
 * @author Yannis Portmann / Shpend Vladi
 *
 */
public class NursingReport {

	private int nursingReportId;
	private Date nursingReportDate;
	private String value;
	private Patient patient;
	private Date creationDate;
	private Date lastUpdate;
	private Date deletionDate;
	private Date sendDate;
	private int swingNursingReportId;
	private Long smisObservationId;

	public Long getSmisObservationId() {
		return smisObservationId;
	}

	public void setSmisObservationId(Long smisObservationId) {
		this.smisObservationId = smisObservationId;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public int getNursingReportId() {
		return nursingReportId;
	}

	public void setNursingReportId(int nursingReportId) {
		this.nursingReportId = nursingReportId;
	}

	public Date getNursingReportDate() {
		return nursingReportDate;
	}

	public void setNursingReportDate(Date nursingReportDate) {
		this.nursingReportDate = nursingReportDate;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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

	public int getSwingNursingReportId() {
		return swingNursingReportId;
	}

	public void setSwingNursingReportId(int swingNursingReportId) {
		this.swingNursingReportId = swingNursingReportId;
	}
}
