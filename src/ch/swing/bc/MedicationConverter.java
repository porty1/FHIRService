package ch.swing.bc;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import ch.swing.helper.Configuration;
import ch.swing.persistence.controller.MasterDataController;
import ch.swing.persistence.controller.MedicationController;
import ch.swing.persistence.model.Patient;

/**
 * Diese Klasse ruft das PDF von der gewünschten SMIS URL ab und speichert es in
 * die Datenbank
 * 
 * @author Yannis Portmann / Shpend Vladi
 *
 */
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
	 * Startpunkt für den Scheduler, holt alle Patienten aus der Datenbank
	 * 
	 */
	public void startMedicationConverter() {
		List<Patient> patientList = MasterDataController.getInstance().getIDPatients();

		for (int i = 0; i < patientList.size(); i++) {
			getPDFFromURL(patientList.get(i).getSmisPatientId(), patientList.get(i).getPatientId());
		}
	}

	/**
	 * Hilfsmethode für das Login auf dem SMIS Server
	 * 
	 * @return
	 */
	private String getBasicAuthenticationEncoding() {
		String authStr = Configuration.MEDICATIONUSERNAME + ":" + Configuration.MEDICATIONPASSWORD;
		return Base64.getEncoder().encodeToString(authStr.getBytes());
	}

	/**
	 * Ruft die aktuelle Medikation, welche vom SMIS Server bereitgestellt wird
	 * ab und speichert diese in der Datenbank ab
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
			// Entsprechende Codes vom Server müssen behandelt werden!
			final int code = connection.getResponseCode();
			if (code < 200 || code >= 300 || code == 403) {
				logger.info("smisid: " + String.valueOf(SMISPatientId) + ", response code: " + String.valueOf(code));
				throw new IOException(String.valueOf(code));
			}
			InputStream in = connection.getInputStream();

			if (in.read() != -1) {
				byte[] medicationFile = IOUtils.toByteArray(in);
				List<byte[]> medList = MedicationController.getInstance().getMedication(patientId);

				if (medList.isEmpty()) {
					MedicationController.getInstance().saveMedication(medicationFile, patientId);
				} else {
					int index = medList.size() - 1;
					if (index >= 0 && (medList.get(index).length == medicationFile.length)) {
						logger.info("The Medication from Patient: " + patientId + " is up to date!");
					} else {
						MedicationController.getInstance().saveMedication(medicationFile, patientId);
						logger.info("Medication from Patient: " + patientId + " updated");
					}
				}
			} else {
				logger.error("No Medication for Patient: " + patientId);
			}
			in.close();
		} catch (IOException e) {
			logger.error(e.getStackTrace() + e.getMessage());
		}
	}
}
