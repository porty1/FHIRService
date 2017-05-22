package ch.swing.bc;

import java.util.List;

import org.apache.log4j.Logger;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryRequestComponent;
import org.hl7.fhir.dstu3.model.Bundle.HTTPVerb;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.exceptions.FHIRException;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.BasicAuthInterceptor;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ch.swing.helper.CodingSystems;
import ch.swing.helper.Configuration;
import ch.swing.helper.FHIRServiceException;
import ch.swing.persistence.controller.NusingReportController;
import ch.swing.persistence.controller.ObservationController;

/**
 * 
 * @author Yannis Portmann
 *
 */
public class ObservationConverter {
	final static Logger logger = Logger.getLogger(ObservationConverter.class);

	private static ObservationConverter mc = null;

	public static ObservationConverter getInstance() {

		if (mc == null) {
			mc = new ObservationConverter();
		}
		return mc;
	}

	/**
	 * Converts the vital data to a FHIR observation object
	 */
	public void convertObservation(ch.swing.persistence.model.Observation source) {
		Observation observation = new Observation();

		// Add the all the identifiers of the Patient
		observation.addIdentifier().setSystem(CodingSystems.SYSTEM_PATIENT_INTERNAL_ID)
				.setValue(Long.toString(source.getPatient().getSmisPatientId()));
		observation.addIdentifier().setSystem(CodingSystems.SYSTEM_PATIENT_EXTERNAL_ID)
				.setValue(Integer.toString(source.getPatient().getSwingPatientId()));

		if (source.getPatient() != null && source.getPatient().getInsuranceCard() != null) {
			observation.addIdentifier().setSystem(CodingSystems.ZSR_OID)
					.setValue(source.getPatient().getInsuranceCard().getCardNumber());
		}

		observation.getCode().addCoding().setSystem(CodingSystems.VITALDATAURL);
		observation.setValue(new StringType(source.getValue()));
		// observation.setEffective(new DateType(source.getEffectiveDate()));
		observation.setEffective(new DateTimeType(source.getEffectiveDate()));
		sendObservation(observation);
	}

	/**
	 * Converts the nursing report to a FHIR observation object
	 * 
	 * @param source
	 */
	public void convertNursingReport(ch.swing.persistence.model.NursingReport source) {
		Observation nursingReport = new Observation();

		// Add the all the identifiers of the Patient
		nursingReport.addIdentifier().setSystem(CodingSystems.SYSTEM_PATIENT_INTERNAL_ID)
				.setValue(Long.toString(source.getPatient().getSmisPatientId()));
		nursingReport.addIdentifier().setSystem(CodingSystems.SYSTEM_PATIENT_EXTERNAL_ID)
				.setValue(Integer.toString(source.getPatient().getSwingPatientId()));

		if (source.getPatient() != null && source.getPatient().getInsuranceCard() != null) {
			nursingReport.addIdentifier().setSystem(CodingSystems.ZSR_OID)
					.setValue(source.getPatient().getInsuranceCard().getCardNumber());
		}
		nursingReport.getCode().addCoding().setSystem(CodingSystems.NURSINGREPORTURL);
		nursingReport.setValue(new StringType(source.getValue()));
		// nursingReport.setEffective(new
		// DateType(source.getNursingReportDate()));
		nursingReport.setEffective(new DateTimeType(source.getNursingReportDate()));
		sendObservation(nursingReport);
	}

	/**
	 * Sends the observation via FHIR to the SMIS Service
	 */
	public void sendObservation(Observation observation) {
		// Creating the FHIR DSTU3 Context
		FhirContext ctx = FhirContext.forDstu3();
		Bundle bundle = new Bundle();
		// bundle.setType(BundleTypeEnum.TRANSACTION);

		bundle.addEntry().setFullUrl("Test").setResource(observation)
				.setRequest(new BundleEntryRequestComponent().setMethod(HTTPVerb.POST));

		// Log the request
		logger.info(ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(bundle));

		// Create a client and post the transaction to the server
		// IGenericClient client =
		// ctx.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu3");
		IGenericClient client = ctx.newRestfulGenericClient(Configuration.FHIRSERVERURL);

		Bundle resp = client.transaction().withBundle(bundle).execute();

		// Create an HTTP basic auth interceptor
		BasicAuthInterceptor authInterceptor = new BasicAuthInterceptor(Configuration.FHIRUSERNAME,
				Configuration.FHIRPASSWORD);

		// Register the interceptor with your client (either style)
		client.registerInterceptor(authInterceptor);

		// Log the response
		logger.info(ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));

		// ----------------------------------------------------------------------

		try {
			// Invoke the server create method (and send pretty-printed JSON
			// encoding to the server
			// instead of the default which is non-pretty printed XML)
			MethodOutcome outcome = client.create().resource(observation).prettyPrint().encodedJson().execute();

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

	/**
	 * Retrieves the observation List from the Database
	 * 
	 * @throws FHIRException
	 */
	public void getObservationList() throws FHIRServiceException {
		List<ch.swing.persistence.model.Observation> observationList = ObservationController.getInstance()
				.getObservationChanges();

		if (observationList != null) {
			for (int i = 0; i < observationList.size(); i++) {
				convertObservation(observationList.get(i));
			}
		} else {
			throw new FHIRServiceException("Database List empty!");
		}

	}

	/**
	 * Retrieves all the Nursing Reports from the System
	 * 
	 * @throws FHIRException
	 * @throws FHIRServiceException
	 */
	public void getObservationNursingReportList() throws FHIRServiceException {
		List<ch.swing.persistence.model.NursingReport> nursingReportList = NusingReportController.getInstance()
				.getNursingReportChanges();

		if (nursingReportList != null) {
			for (int i = 0; i < nursingReportList.size(); i++) {
				convertNursingReport(nursingReportList.get(i));
			}
		} else {
			throw new FHIRServiceException("Database List empty!");
		}

	}
}
