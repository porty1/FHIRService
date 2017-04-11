package ch.swing.persistence.model;

import java.util.Date;

public class InsuranceCard {

	private int insuranceCardId;
	private int cardNumber;
	private Date validForm;
	private Date validTo;
	private String insurance;
	private Date creationDate;
	private Date lastUpdate;
	private Date deletionDate;
	public int getInsuranceCardId() {
		return insuranceCardId;
	}
	public void setInsuranceCardId(int insuranceCardId) {
		this.insuranceCardId = insuranceCardId;
	}
	public int getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(int cardNumber) {
		this.cardNumber = cardNumber;
	}
	public Date getValidForm() {
		return validForm;
	}
	public void setValidForm(Date validForm) {
		this.validForm = validForm;
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
