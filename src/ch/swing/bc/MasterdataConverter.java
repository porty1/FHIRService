package ch.swing.bc;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.exceptions.FHIRException;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.BasicAuthInterceptor;
import ch.swing.helper.CodingSystems;
import ch.swing.helper.Configuration;
import ch.swing.helper.ConverterUtils;
import ch.swing.helper.FHIRServiceException;
import ch.swing.persistence.controller.MasterDataController;
import ch.swing.persistence.model.Telecom;

/**
 * Converter Klasse um alle Stammdaten in eine FHIR Ressource umzuwandeln
 * 
 * @author Yannis Portmann / Shpend Vladi
 *
 */
public class MasterdataConverter {
	final static Logger logger = Logger.getLogger(MasterdataConverter.class);

	private static MasterdataConverter mc = null;

	public static MasterdataConverter getInstance() {

		if (mc == null) {
			mc = new MasterdataConverter();
		}
		return mc;
	}

	/**
	 * Startpunkt für die Konvertierung
	 * 
	 * @throws FHIRServiceException
	 * @throws FHIRException
	 */
	public void startMasterdataConverter() throws FHIRServiceException, FHIRException {
		List<ch.swing.persistence.model.Patient> patientList = getPatientList();
		for (int i = 0; i < patientList.size(); i++) {
			convertPatient(patientList.get(i));
		}
	}

	/**
	 * Diese Methode konvertiert die Daten aus dem Source in die FHIR Ressource
	 * 
	 * @param source
	 * @throws FHIRException
	 * @throws FHIRServiceException
	 */
	public void convertPatient(ch.swing.persistence.model.Patient source) throws FHIRException, FHIRServiceException {
		// Create a patient object
		Patient patient = new Patient();

		// All the required fields need to be != null to send the patient to the
		// SMIS Webservice
		if (source.getFamilyName() != null && source.getGivenName() != null && source.getBirthDate() != null
				&& source.getSwingPatientId() != 0 && source.getGender() != 0 && source.getPostalCode() != 0) {
			patient.addName().setFamily(source.getFamilyName()).addGiven(source.getGivenName());
			patient.setBirthDate(source.getBirthDate());
			patient.addIdentifier().setSystem(CodingSystems.SYSTEM_PATIENT_EXTERNAL_ID) //
					.setValue(Integer.toString(source.getSwingPatientId()));
			final Address patientAddress = new Address().setCity(source.getCity())
					.setPostalCode(Integer.toString(source.getPostalCode())).setCountry(source.getCountry())
					.addLine(source.getRoad());
			patient.addAddress(patientAddress);
			// Add the Gender of the Patient this is converted from the SWING
			// Coding
			if (source.getGender() == 99) {
				patient.setGender(AdministrativeGender.FEMALE);
			} else if (source.getGender() == 100) {
				patient.setGender(AdministrativeGender.MALE);
			} else {
				patient.setGender(AdministrativeGender.UNKNOWN);
			}
		} else {
			logger.error("Required Field missing!");
			throw new FHIRServiceException("Required field missing!");
		}
		// SMIS ID
		if (source.getSmisPatientId() != 0) {
			patient.addIdentifier().setSystem(CodingSystems.SYSTEM_PATIENT_INTERNAL_ID)
					.setValue(Long.toString(source.getSmisPatientId()));
		}
		// AHV-Nummer
		if (source.getSocialInsuranceNumber() != null) {
			patient.addIdentifier().setSystem(CodingSystems.ZSR_OID).setValue(source.getSocialInsuranceNumber());
		}

		// Needs to be adapted in a further step
		// It is now static to match the FHIR requirements
		Telecom telecom = new Telecom();
		telecom.setSystem("phone");
		telecom.setTelecomUse("home");
		telecom.setValue(source.getTelecom());

		// Only one telecom object is allowed from the SMIS Service
		if (source.getTelecom() != null) {
			patient.setTelecom(ConverterUtils.convertTelecom(Arrays.asList(telecom)));
		}
		sendPatient(patient, source.getPatientId(), source.getSmisPatientId());
	}

	/**
	 * Diese Methode sendet die erstellte Ressource an den FHIR Server
	 * 
	 * @param patient
	 * @param patientId
	 * @param smisPatientId
	 */
	public void sendPatient(Patient patient, int patientId, Long smisPatientId) {
		// Creating the FHIR DSTU3 Context
		FhirContext ctx = FhirContext.forDstu3();

		// Create an HTTP basic auth interceptor with the credentials
		BasicAuthInterceptor authInterceptor = new BasicAuthInterceptor(Configuration.FHIRUSERNAME,
				Configuration.FHIRPASSWORD);

		// Create a client and post the transaction to the server
		IGenericClient client = ctx.newRestfulGenericClient(Configuration.FHIRSERVERURL);
		// Register the interceptor with your client (either style)
		client.registerInterceptor(authInterceptor);

		final MethodOutcome outcome;
		outcome = client.create().resource(patient).execute();
		logger.info("SMIS ID: " + outcome.getId().getIdPart());

		// Update the SMIS ID in the Database for further changes
		// The ID must not change!
		if (smisPatientId == 0) {
			MasterDataController.getInstance().updateIdentifier(Long.parseLong(outcome.getId().getIdPart()), patientId);
		}
		// Update the send Date in the Database because of the Trigger
		MasterDataController.getInstance().updateSendDate(patientId);
	}

	/**
	 * Hilfsmethode, um alle Patienten aus der Datenbank zu laden
	 * 
	 * @return
	 * @throws FHIRServiceException
	 */
	public List<ch.swing.persistence.model.Patient> getPatientList() throws FHIRServiceException {
		List<ch.swing.persistence.model.Patient> patientList = MasterDataController.getInstance()
				.getMasterDataChanges();
		if (patientList != null) {
			return patientList;
		} else {
			logger.info("Database is empty");
			throw new FHIRServiceException("Database is empty");
		}
	}
}
