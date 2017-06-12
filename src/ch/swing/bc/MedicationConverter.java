package ch.swing.bc;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.List;

import org.apache.catalina.tribes.util.Arrays;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import ch.swing.helper.Configuration;
import ch.swing.persistence.controller.MasterDataController;
import ch.swing.persistence.controller.MedicationController;
import ch.swing.persistence.model.Patient;

public class MedicationConverter {
	final static Logger logger = Logger.getLogger(MedicationConverter.class);
	private static MedicationConverter mc = null;

	public static MedicationConverter getInstance() {

		if (mc == null) {
			mc = new MedicationConverter();
		}
		return mc;
	}

	/**
	 * Retrieve all the Patients from the Database from which we need to get the
	 * medication from the SMIS System
	 */
	public void startMedicationConverter() {
		List<Patient> patientList = MasterDataController.getInstance().getIDPatients();

		for (int i = 0; i < patientList.size(); i++) {
			getPDFFromURL(patientList.get(i).getSmisPatientId(), patientList.get(i).getPatientId());
		}
	}

	private String getBasicAuthenticationEncoding() {
		String authStr = Configuration.MEDICATIONUSERNAME + ":" + Configuration.MEDICATIONPASSWORD;
		return Base64.getEncoder().encodeToString(authStr.getBytes());
	}

	/**
	 * Retrieve all the PDF's from the SMIS System
	 * 
	 * @param orgId
	 * @param SMISPatientId
	 * @param patientId
	 */
	public void getPDFFromURL(Long SMISPatientId, int patientId) {
		final String urlSMISString = Configuration.MEDICATIONURL + Configuration.MEDICATIONORGID + "/patients/"
				+ SMISPatientId + "/mediPlan?dateFrom=" + Configuration.MEDICATIONDATEFROM + "&dateTo="
				+ Configuration.MEDICATIONDATETO;
		try {
			URL url = new URL(urlSMISString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Basic " + getBasicAuthenticationEncoding());
			connection.setRequestProperty("Accept", "application/pdf; charset=UTF-8");
			connection.connect();
			final int code = connection.getResponseCode();
			if (code < 200 || code >= 300) {
				logger.info("smisid: " + String.valueOf(SMISPatientId) + ", response code: " + String.valueOf(code));
				throw new IOException(String.valueOf(code));
			}
			InputStream in = connection.getInputStream();
			byte[] medicationFile = IOUtils.toByteArray(in);
			byte[] dbMedication = MedicationController.getInstance().getMedication(patientId);
			
			if (medicationFile.length == dbMedication.length) {
				logger.info("The Medication from Patient: "+patientId +" is up to date!");
			} else {
				MedicationController.getInstance().saveMedication(medicationFile, patientId);
			}
			in.close();
		} catch (IOException e) {
			logger.error(e.getStackTrace() + e.getMessage());
		}
	}
}
