package ch.swing.bc;

import java.util.List;

import org.apache.log4j.Logger;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryRequestComponent;
import org.hl7.fhir.dstu3.model.Bundle.HTTPVerb;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.Patient;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.IGenericClient;
import ch.swing.persistence.controller.MasterDataController;

public class Masterdata {
	final static Logger logger = Logger.getLogger(Masterdata.class);
	
	public static void main(String[] args) {
		MasterDataController mdController = new MasterDataController();
//		mdController.getPatient(2);
		mdController.setCreationDate();
		
		//createPatient();
	}

	public static void createPatient() {

		
		
		// Creating the FHIR DSTU3 Context
		FhirContext ctx = FhirContext.forDstu3();
		// Create a patient object
		Patient patient = new Patient();
		// Add the Birthdate of the Patient
		patient.getBirthDateElement().setValueAsString("1998-02-22");
		// Add the all the identifiers of the Patient
		patient.addIdentifier().setSystem("http://acme.org/mrns").setValue("12345");
		patient.addIdentifier().setSystem("Swing ID").setValue("1234");
		patient.addIdentifier().setSystem("Swing ID").setValue("1234");
		patient.addIdentifier().setSystem("AHV-Nummer").setValue("756.1012.1018.52");
		// Add the Family and Given Name of the Patient
		patient.addName().setFamily("Jameson").addGiven("Jonah");
		// Add the Gender of the Patient
		patient.setGender(AdministrativeGender.MALE);
		// Add the Address of the Patient
		patient.addAddress().setPostalCode("PLZ").setCity("city").setText("Street");

		Bundle bundle = new Bundle();
		// bundle.setType(BundleTypeEnum.TRANSACTION);

		bundle.addEntry().setFullUrl("Test").setResource(patient).setRequest(new BundleEntryRequestComponent().setMethod(HTTPVerb.POST));

		// Log the request
		System.out.println(ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(bundle));

		// Create a client and post the transaction to the server
		IGenericClient client = ctx.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu3");
		Bundle resp = client.transaction().withBundle(bundle).execute();

		// Log the response
		System.out.println(ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));

	}

	public List<Patient> getPatientList() {

		return null;
	}

}
