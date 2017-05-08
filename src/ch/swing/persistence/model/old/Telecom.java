package ch.swing.persistence.model.old;

import java.util.Date;

/**
 * Model class which represents the telecom on the database
 * 
 * @author Yannis Portmann / Shpend Vladi
 *
 */
@Deprecated
public class Telecom {

	private int telecomId;
	private String system;
	private String value;
	private String telecomUse;
	private Date creationDate;
	private Date lastUpdate;
	private Date deletionDate;

	public int getTelecomId() {
		return telecomId;
	}

	public void setTelecomId(int telecomId) {
		this.telecomId = telecomId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getTelecomUse() {
		return telecomUse;
	}

	public void setTelecomUse(String telecomUse) {
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
