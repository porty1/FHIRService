package ch.swing.bc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryRequestComponent;
import org.hl7.fhir.dstu3.model.Bundle.HTTPVerb;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Reference;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.BasicAuthInterceptor;
import ch.swing.helper.CodingSystems;
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

	public void convertPatient(ch.swing.persistence.model.Patient source) {

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

		// Add the Birthdate of the Patient
		patient.getBirthDateElement().setValueAsString(source.getBirthDate().toString());
		// Add the all the identifiers of the Patient
		patient.addIdentifier().setSystem(CodingSystems.SYSTEM_PATIENT_INTERNAL_ID)
				.setValue(Integer.toString(source.getSmisPatientId()));
		patient.addIdentifier().setSystem(CodingSystems.SYSTEM_PATIENT_EXTERNAL_ID)
				.setValue(Integer.toString(source.getSwingPatientId()));
		patient.addIdentifier().setSystem(CodingSystems.ZSR_OID)
				.setValue(Integer.toString(source.getInsuranceCard().getCardNumber()));
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

		// TODO
		ContactPoint contact = new ContactPoint();
		// contact.set

		// Creates the Contact Details for the Patient
		List<ContactPoint> contactList = new ArrayList<ContactPoint>();
		contactList.add(contact);
		patient.setTelecom(contactList);

		// Create the Doctor for the Patient
		Practitioner practitioner = new Practitioner();
		// Add an address for the doctor
		final Address practitionerAddress = new Address().setCity(source.getGeneralPractitioner().getCity())
				.setPostalCode(Integer.toString(source.getGeneralPractitioner().getPostalCode()))
				.setCountry(source.getGeneralPractitioner().getCountry())
				.addLine(source.getGeneralPractitioner().getRoad());
		practitioner.addAddress(practitionerAddress);

		practitioner.addName().setFamily(source.getGeneralPractitioner().getFamilyName())
				.addGiven(source.getGeneralPractitioner().getGivenName());

		// patient.setGeneralPractitioner(theGeneralPractitioner);

		// TODO practitioner an patient zuweisen

		Reference ref = new Reference();

		// patient.setGeneralPractitioner(theGeneralPractitioner)

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
		logger.info("Got ID: " + id.getValue());

	}

	// Iterates over the Patient result list and send every entry via FHIR to
	// the SMIS Service
	public void getPatientList() {
		List<ch.swing.persistence.model.Patient> patientList = MasterDataController.getInstance()
				.getMasterDataChanges();

		for (int i = 0; i < patientList.size(); i++) {
			convertPatient(patientList.get(i));
		}
	}

}
