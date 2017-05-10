package ch.swing.bc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryRequestComponent;
import org.hl7.fhir.dstu3.model.Bundle.HTTPVerb;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.exceptions.FHIRException;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.BasicAuthInterceptor;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ch.swing.helper.CodingSystems;
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

	public void convertPatient(ch.swing.persistence.model.Patient source) throws FHIRException {

		// Create a patient object
		Patient patient = new Patient();

		@SuppressWarnings("serial")
		final Map<Short, AdministrativeGender> genderMap = new HashMap<Short, AdministrativeGender>() {
			{
				put((short) 0, AdministrativeGender.UNKNOWN);
				put((short) 1, AdministrativeGender.FEMALE);
				put((short) 2, AdministrativeGender.MALE);
			}
		};

		patient.setBirthDate(source.getBirthDate());

		// Add the Birthdate of the Patient
		// patient.getBirthDateElement().setValueAsString(source.getBirthDate().toString());
		// Add the all the identifiers of the Patient
		patient.addIdentifier().setSystem(CodingSystems.SYSTEM_PATIENT_INTERNAL_ID)
				.setValue(Integer.toString(source.getSmisPatientId()));
		patient.addIdentifier().setSystem(CodingSystems.SYSTEM_PATIENT_EXTERNAL_ID)
				.setValue(Integer.toString(source.getSwingPatientId()));
		if (source.getInsuranceCard() != null) {
			patient.addIdentifier().setSystem(CodingSystems.ZSR_OID)
					.setValue(Integer.toString(source.getInsuranceCard().getCardNumber()));
		}

		// Add the Family and Given Name of the Patient
		patient.addName().setFamily(source.getFamilyName()).addGiven(source.getGivenName());
		// Add the Gender of the Patient
		patient.setGender(genderMap.get(source.getGender()));

		// Add a new address to the patient
		final Address patientAddress = new Address() //
				.setCity(source.getCity()) //
				.setPostalCode(Integer.toString(source.getPostalCode())) //
				.setCountry(source.getCountry()).addLine(source.getRoad());
		patient.addAddress(patientAddress);

		// Only one telecom object is allowed
		if (source.getTelecom() != null) {
			patient.setTelecom(ConverterUtils.convertTelecom(Arrays.asList(source.getTelecom())));
		}

		// Create the Doctor for the Patient
		Practitioner practitioner = new Practitioner();
		// Add an address for the doctor
		if (source.getGeneralPractitioner() != null) {
			final Address practitionerAddress = new Address().setCity(source.getGeneralPractitioner().getCity())
					.setPostalCode(Integer.toString(source.getGeneralPractitioner().getPostalCode()))
					.setCountry(source.getGeneralPractitioner().getCountry())
					.addLine(source.getGeneralPractitioner().getRoad());
			practitioner.addAddress(practitionerAddress);

			// Adding the name of the practitioner
			practitioner.addName().setFamily(source.getGeneralPractitioner().getFamilyName())
					.addGiven(source.getGeneralPractitioner().getGivenName());

			// TODO evtl anpassen
			Reference ref = new Reference();
			ref.setUserData("generalPractitioner", practitioner);
			patient.setGeneralPractitioner(Arrays.asList(ref));

			patient.addGeneralPractitioner(new Reference(
					String.format("Practitioner/%s", Long.toString(source.getGeneralPractitioner().getContactId()))));
			patient.setManagingOrganization(
					new Reference(String.format("Organization/%s", source.getManagingOrganization())));
		}

		sendPatient(patient);
	}

	public void sendPatient(Patient patient) {
		// Creating the FHIR DSTU3 Context
		FhirContext ctx = FhirContext.forDstu3();
		Bundle bundle = new Bundle();
		// bundle.setType(BundleTypeEnum.TRANSACTION);

		bundle.addEntry().setFullUrl("Test").setResource(patient)
				.setRequest(new BundleEntryRequestComponent().setMethod(HTTPVerb.POST));

		// Log the request
		logger.info(ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(bundle));

		// Create a client and post the transaction to the server
		// IGenericClient client =
		// ctx.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu3");
		IGenericClient client = ctx.newRestfulGenericClient("https://smis-test.arpage.ch/smis2-importer/fhir");

		Bundle resp = client.transaction().withBundle(bundle).execute();

		// Create an HTTP basic auth interceptor
		String username = "2064905437901";
		String password = "Test1234.";
		BasicAuthInterceptor authInterceptor = new BasicAuthInterceptor(username, password);

		// Register the interceptor with your client (either style)
		client.registerInterceptor(authInterceptor);

		// Log the response
		logger.info(ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));

		// ----------------------------------------------------------------------

		try {
			// Invoke the server create method (and send pretty-printed JSON
			// encoding to the server
			// instead of the default which is non-pretty printed XML)
			MethodOutcome outcome = client.create().resource(patient).prettyPrint().encodedJson().execute();

			// The MethodOutcome object will contain information about the
			// response from the server, including the ID of the created
			// resource, the OperationOutcome response, etc. (assuming that
			// any of these things were provided by the server! They may not
			// always be)
			IdDt id = (IdDt) outcome.getId();

			// TODO anpassen für Trigger!!

			// if(id != null){
			// MasterDataController.getInstance().setCreationDate(id);
			// }
			logger.info("Got ID: " + id.getValue());
		} catch (BaseServerResponseException e) {
			logger.error("FHIR Server Access failed!!", e);
		}

	}

	// Iterates over the Patient result list and send every entry via FHIR to
	// the SMIS Service
	public void getPatientList() throws FHIRServiceException, FHIRException {
		List<ch.swing.persistence.model.Patient> patientList = MasterDataController.getInstance()
				.getMasterDataChanges();
		if (patientList != null) {
			for (int i = 0; i < patientList.size(); i++) {
				convertPatient(patientList.get(i));
			}
		} else {
			throw new FHIRServiceException("Database empty");
		}

	}

}
