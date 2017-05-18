package ch.swing.persistence.model;

import java.util.Date;

/**
 * Model Class which represents the insurance card on the database
 * 
 * @author Yannis Portmann / Shpend Vladi
 *
 */
public class InsuranceCard {

	private int insuranceCardId;
	private String cardNumber;
	private Date validFrom;
	private Date validTo;
	private String insurance;
	private Date creationDate;
	private Date lastUpdate;
	private Date deletionDate;

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public int getInsuranceCardId() {
		return insuranceCardId;
	}

	public void setInsuranceCardId(int insuranceCardId) {
		this.insuranceCardId = insuranceCardId;
	}

	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public Date getValidTo() {
		return validTo;
	}

	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}

	public String getInsurance() {
		return insurance;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
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
