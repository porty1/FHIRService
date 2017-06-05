package ch.swing.bc;

import java.util.List;

import org.apache.log4j.Logger;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.exceptions.FHIRException;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.BasicAuthInterceptor;
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

	public void startNursingReportConverter() throws FHIRServiceException, FHIRException {
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

	public void startObservationConverter() throws FHIRServiceException, FHIRException {
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
	 * Converts the vital data to a FHIR observation object
	 */
	public void convertObservation(ch.swing.persistence.model.Observation source) {
		Observation observation = new Observation();
		observation.setSubject(new Reference(Long.toString(source.getPatient().getSmisPatientId())));
		observation.getCode().addCoding().setSystem(CodingSystems.VITALDATAURL);
		observation.getCode().addCoding().setCode("vital-signs");
		observation.setValue(new StringType(source.getValue()));
		observation.setEffective(new DateTimeType(source.getEffectiveDate()));
		sendObservation(observation, source.getPatient().getPatientId());

	}

	/**
	 * Converts the nursing report to a FHIR observation object
	 * 
	 * @param source
	 */
	public void convertNursingReport(ch.swing.persistence.model.NursingReport source) {
		Observation nursingReport = new Observation();
		nursingReport.setSubject(new Reference(Long.toString(source.getPatient().getSmisPatientId())));
		nursingReport.getCode().addCoding().setSystem(CodingSystems.NURSINGREPORTURL);
		nursingReport.getCode().addCoding().setCode("nursing-report");
		nursingReport.setValue(new StringType(source.getValue()));
		nursingReport.setEffective(new DateTimeType(source.getNursingReportDate()));
		sendObservation(nursingReport, source.getPatient().getPatientId());
	}

	/**
	 * Sends the observation via FHIR to the SMIS Service
	 */
	public void sendObservation(Observation observation, int patientId) {
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
		outcome = client.create().resource(observation).execute();
		logger.info("Observation ID: " + outcome.getId().getIdPart());
		// Update the creation Date in the Database because of the Trigger
		// ObservationController.getInstance().updateCreationDate(patientId);
	}
}
