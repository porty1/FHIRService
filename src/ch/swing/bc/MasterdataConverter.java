package ch.swing.bc;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Practitioner;
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

/**
 * 
 * @author Yannis Portmann
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

	public void startMasterdataConverter() throws FHIRServiceException, FHIRException {
		List<ch.swing.persistence.model.Patient> patientList = getPatientList();
		for (int i = 0; i < patientList.size(); i++) {
			convertPatient(patientList.get(i));
		}

	}

	public void convertPatient(ch.swing.persistence.model.Patient source) throws FHIRException {
		// Create a patient object
		Patient patient = new Patient();

		patient.setBirthDate(source.getBirthDate());

		// Add the Birthdate of the Patient
		// patient.getBirthDateElement().setValueAsString(source.getBirthDate().toString());
		// Add the all the identifiers of the Patient
		if (source.getSmisPatientId() != 0) {
			patient.addIdentifier().setSystem(CodingSystems.SYSTEM_PATIENT_INTERNAL_ID)
					.setValue(Long.toString(source.getSmisPatientId()));
		}

		patient.addIdentifier().setSystem(CodingSystems.SYSTEM_PATIENT_EXTERNAL_ID)
				.setValue(Integer.toString(source.getSwingPatientId()));
		if (source.getInsuranceCard() != null) {
			patient.addIdentifier().setSystem(CodingSystems.ZSR_OID)
					.setValue(source.getInsuranceCard().getCardNumber());
		}

		// Add the Family and Given Name of the Patient
		patient.addName().setFamily(source.getFamilyName()).addGiven(source.getGivenName());

		// Add the Gender of the Patient
		if (source.getGender() == 99) {
			patient.setGender(AdministrativeGender.FEMALE);
		} else if (source.getGender() == 100) {
			patient.setGender(AdministrativeGender.MALE);
		} else {
			patient.setGender(AdministrativeGender.UNKNOWN);
		}

		// Add a new address to the patient
		final Address patientAddress = new Address().setCity(source.getCity())
				.setPostalCode(Integer.toString(source.getPostalCode())).setCountry(source.getCountry())
				.addLine(source.getRoad());
		patient.addAddress(patientAddress);

		// Only one telecom object is allowed
		if (source.getTelecom() != null) {
			patient.setTelecom(ConverterUtils.convertTelecom(Arrays.asList(source.getTelecom())));
		}

		Practitioner practitioner = new Practitioner();
		if (source.getGeneralPractitioner() != null) {
			final Address practitionerAddress = new Address().setCity(source.getGeneralPractitioner().getCity())
					.setPostalCode(Integer.toString(source.getGeneralPractitioner().getPostalCode()))
					.setCountry(source.getGeneralPractitioner().getCountry())
					.addLine(source.getGeneralPractitioner().getRoad());
			practitioner.addAddress(practitionerAddress);
		}
		// Adding the name of the practitioner
		practitioner.addName().setFamily(source.getGeneralPractitioner().getFamilyName())
				.addGiven(source.getGeneralPractitioner().getGivenName());

		// TODO evtl anpassen
		// Reference ref = new Reference();
		// ref.setUserData("generalPractitioner", practitioner);
		// patient.setGeneralPractitioner(Arrays.asList(ref));

		// patient.setGeneralPractitioner(Arrays
		// .asList(new Reference(String.format("General Practitioner",
		// source.getGeneralPractitioner()))));
		// patient.addGeneralPractitioner(new Reference(
		// String.format("Practitioner/%s",
		// Long.toString(source.getGeneralPractitioner().getContactId()))));
		// patient.setManagingOrganization(
		// new Reference(String.format("Organization/%s",
		// source.getManagingOrganization())));
		// }
		sendPatient(patient, source.getPatientId());
	}

	public void sendPatient(Patient patient, int patientId) {
		// Creating the FHIR DSTU3 Context
		FhirContext ctx = FhirContext.forDstu3();

		// Create an HTTP basic auth interceptor
		BasicAuthInterceptor authInterceptor = new BasicAuthInterceptor(Configuration.FHIRUSERNAME,
				Configuration.FHIRPASSWORD);

		// Create a client and post the transaction to the server
		IGenericClient client = ctx.newRestfulGenericClient(Configuration.FHIRSERVERURL);
		// Register the interceptor with your client (either style)
		client.registerInterceptor(authInterceptor);

		final MethodOutcome outcome;
		outcome = client.create().resource(patient).execute();
		logger.info("SMIS ID: " + outcome.getId().getIdPart());
		// Update the SMIS ID in the Database
		MasterDataController.getInstance().updateIdentifier(Long.parseLong(outcome.getId().getIdPart()), patientId);
		// Update the creation Date in the Database
		MasterDataController.getInstance().updateCreationDate(patientId);
	}

	public List<ch.swing.persistence.model.Patient> getPatientList() throws FHIRServiceException {
		List<ch.swing.persistence.model.Patient> patientList = MasterDataController.getInstance()
				.getMasterDataChanges();
		if (patientList != null) {
			return patientList;
		} else {
			throw new FHIRServiceException("Database empty");
		}
	}

}
