package ch.swing.bc;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;

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
		final String orgId = "1457020138649054";

		for (int i = 0; i < patientList.size(); i++) {
			getPDFFromURL(orgId, patientList.get(i).getSmisPatientId(), patientList.get(i).getPatientId());
		}
	}

	private String getBasicAuthenticationEncoding() {
		//TODO
		String username = "";
		String password = "";

		String userPassword = username + ":" + password;
		return new String(Base64.encodeBase64(userPassword.getBytes()));
	}

	/**
	 * Retrieve all the PDF's from the SMIS System
	 * 
	 * @param orgId
	 * @param SMISPatientId
	 * @param patientId
	 */
	public void getPDFFromURL(String orgId, Long SMISPatientId, int patientId) {
		// https://smis-test.arpage.ch:443/smis2-core/orgs/{orgId}/patients/{patientId}/mediPlan?dateFrom=yyyymmdd&dateTo=yyyydd

		// TODO Konfiguration auslagern
		// try {
		// FileInputStream inputStream = new FileInputStream("test.txt");
		// String everything = IOUtils.toString(inputStream, "UTF-8");
		// } catch (IOException e) {
		// logger.error(e.getStackTrace());
		// }

		final String dateFrom = "20170101";
		final String dateTo = "20180101";

		final String urlSMISString = "https://smis-test.arpage.ch:443/smis2-core/orgs/" + orgId + "/patients/"
				+ SMISPatientId + "/mediPlan?dateFrom=" + dateFrom + "&dateTo=" + dateTo;
		try {
			HttpGet getRequest = new HttpGet(urlSMISString);
			getRequest.addHeader("Authorization", "Basic " + getBasicAuthenticationEncoding());
			URL url = new URL(urlSMISString);
			InputStream in = url.openStream();
			//TODO FIle parsen
			
			byte[] medicationFile = IOUtils.toByteArray(in);
			


			MedicationController.getInstance().saveMedication(medicationFile, patientId);
			// Files.copy(in, Paths.get("medication.pdf"),
			// StandardCopyOption.REPLACE_EXISTING);
			in.close();
		} catch (IOException e) {
			logger.error(e.getStackTrace());
		}
	}
}
