package ch.swing.bc;

import java.util.List;

import org.hl7.fhir.dstu3.model.Patient;

import ca.uhn.fhir.context.FhirContext;

public class Masterdata {

	public void createPatient(){
		//Creating the FHIR Context
		FhirContext ctx = FhirContext.forDstu3();
		// Working with RI structures is similar to how it works with the HAPI structures
		org.hl7.fhir.dstu3.model.Patient patient = new org.hl7.fhir.dstu3.model.Patient();
		patient.addName().addGiven("John").setFamily("Smith");
		patient.getBirthDateElement().setValueAsString("1998-02-22");
		
		
		
		
		// Parsing and encoding works the same way too
		String encoded = ctx.newJsonParser().encodeResourceToString(patient);
	}
	
	public List<Patient> getPatientList(){
		
		
		return null;
	}
	
}
