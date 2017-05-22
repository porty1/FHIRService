package ch.swing.bc;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpGet;
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
	public void getMedication() {
		List<Patient> patientList = MasterDataController.getInstance().getIDPatients();

		for (int i = 0; i < patientList.size(); i++) {
			getPDFFromURL(patientList.get(i).getSmisPatientId(), patientList.get(i).getPatientId());
		}
	}

	private String getBasicAuthenticationEncoding() {
		String userPassword = Configuration.MEDICATIONUSERNAME + ":" + Configuration.MEDICATIONPASSWORD;
		return new String(Base64.encodeBase64(userPassword.getBytes()));
	}

	/**
	 * Retrieve all the PDF's from the SMIS System
	 * 
	 * @param orgId
	 * @param SMISPatientId
	 * @param patientId
	 */
	public void getPDFFromURL(Long SMISPatientId, int patientId) {
		// https://smis-test.arpage.ch:443/smis2-core/orgs/{orgId}/patients/{patientId}/mediPlan?dateFrom=yyyymmdd&dateTo=yyyydd

		final String urlSMISString = Configuration.MEDICATIONURL + Configuration.MEDICATIONORGID + "/patients/"
				+ SMISPatientId + "/mediPlan?dateFrom=" + Configuration.MEDICATIONDATEFROM + "&dateTo="
				+ Configuration.MEDICATIONDATETO;
		try {
			HttpGet getRequest = new HttpGet(urlSMISString);
			getRequest.addHeader("Authorization", "Basic " + getBasicAuthenticationEncoding());
			URL url = new URL(urlSMISString);
			InputStream in = url.openStream();
			byte[] medicationFile = IOUtils.toByteArray(in);
			MedicationController.getInstance().saveMedication(medicationFile, patientId);
			in.close();
		} catch (IOException e) {
			logger.error(e.getStackTrace());
		}
	}
}
