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
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.BasicAuthInterceptor;
import ch.swing.helper.CodingSystems;
import ch.swing.persistence.controller.MasterDataController;

public class Masterdata {
	final static Logger logger = Logger.getLogger(Masterdata.class);

	public static void main(String[] args) {
		MasterDataController mdController = new MasterDataController();
		// mdController.getPatient(2);
		// mdController.setCreationDate();

		// createPatient();
	}

	// TODO remove static
	public void convertPatient(ch.swing.persistence.model.Patient dbPatient) {

		// Create a patient object
		Patient patient = new Patient();

		/** My vaccine gender to FHIR gender conversion map. */
		@SuppressWarnings("serial")
		final Map<Short, AdministrativeGender> genderMap = new HashMap<Short, AdministrativeGender>() {
			{
				put((short) 0, AdministrativeGender.UNKNOWN);
				put((short) 1, AdministrativeGender.FEMALE);
				put((short) 2, AdministrativeGender.MALE);
			}
		};

		// Add the Birthdate of the Patient
		patient.getBirthDateElement().setValueAsString(dbPatient.getBirthDate().toString());
		// Add the all the identifiers of the Patient
		patient.addIdentifier().setSystem(CodingSystems.SYSTEM_PATIENT_INTERNAL_ID)
				.setValue(Integer.toString(dbPatient.getSmisPatientId()));
		patient.addIdentifier().setSystem(CodingSystems.SYSTEM_PATIENT_EXTERNAL_ID)
				.setValue(Integer.toString(dbPatient.getSwingPatientId()));
		patient.addIdentifier().setSystem(CodingSystems.ZSR_OID)
				.setValue(Integer.toString(dbPatient.getInsuranceCard().getCardNumber()));
		// Add the Family and Given Name of the Patient
		patient.addName().setFamily(dbPatient.getFamilyName()).addGiven(dbPatient.getGivenName());
		// Add the Gender of the Patient
		patient.setGender(genderMap.get(dbPatient.getGender()));

		// Add a new address to the patient
		final Address patientAddress = new Address() //
				.setCity(dbPatient.getCity()) //
				.setPostalCode(Integer.toString(dbPatient.getPostalCode())) //
				.setCountry(dbPatient.getCountry()).addLine(dbPatient.getRoad());
		patient.addAddress(patientAddress);

		// TODO
		ContactPoint contact = new ContactPoint();
		// .setSys

		// Creates the Contact Details for the Patient
		List<ContactPoint> contactList = new ArrayList<ContactPoint>();
		contactList.add(contact);
		patient.setTelecom(contactList);

		// Create the Doctor for the Patient
		Practitioner practitioner = new Practitioner();
		// Add an address for the doctor
		final Address practitionerAddress = new Address().setCity(dbPatient.getGeneralPractitioner().getCity())
				.setPostalCode(Integer.toString(dbPatient.getGeneralPractitioner().getPostalCode()))
				.setCountry(dbPatient.getGeneralPractitioner().getCountry())
				.addLine(dbPatient.getGeneralPractitioner().getRoad());
		practitioner.addAddress(practitionerAddress);

		practitioner.addName().setFamily(dbPatient.getGeneralPractitioner().getFamilyName())
				.addGiven(dbPatient.getGeneralPractitioner().getGivenName());

		// TODO practitioner an patient zuweisen

		Reference ref = new Reference();
		// ref.set

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
		System.out.println(ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(bundle));

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
		System.out.println(ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));
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
