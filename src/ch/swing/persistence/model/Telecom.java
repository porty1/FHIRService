package ch.swing.persistence.model;

import java.util.Date;

/**
 * Model class which represents the telecom on the database
 * 
 * @author Yannis Portmann / Shpend Vladi
 *
 */
public class Telecom {

	private int telecomId;
	private int system;
	private int value;
	private Date telecomUse;
	private Date creationDate;
	private Date lastUpdate;
	private Date deletionDate;

	public int getTelecomId() {
		return telecomId;
	}

	public void setTelecomId(int telecomId) {
		this.telecomId = telecomId;
	}

	public int getSystem() {
		return system;
	}

	public void setSystem(int system) {
		this.system = system;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Date getTelecomUse() {
		return telecomUse;
	}

	public void setTelecomUse(Date telecomUse) {
		this.telecomUse = telecomUse;
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
