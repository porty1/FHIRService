package ch.swing.bc;

import java.util.List;

import org.apache.log4j.Logger;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Observation.ObservationStatus;
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
 * Converter Klasse um alle Vitaldaten und Pflegeberichtseinträge in eine FHIR
 * Ressource umzuwandeln
 * 
 * @author Yannis Portmann / Shpend Vladi
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
	 * Startpunkt für den Scheduler, welcher alle Pflegeberichtseinträge aus der
	 * Datenbank abruft und die Konvertierung startet
	 * 
	 * @throws FHIRServiceException
	 * @throws FHIRException
	 */
	public void startNursingReportConverter() throws FHIRServiceException, FHIRException {
		List<ch.swing.persistence.model.NursingReport> nursingReportList = NusingReportController.getInstance()
				.getNursingReportChanges();

		if (nursingReportList != null) {
			for (int i = 0; i < nursingReportList.size(); i++) {
				convertNursingReport(nursingReportList.get(i));
			}
		} else {
			logger.info("Database List empty!");
		}
	}

	/**
	 * Startpunkt für den Scheduler, welcher alle Vitaldaten aus der Datenbank
	 * abruft und die Konvertierung startet
	 * 
	 * @throws FHIRServiceException
	 * @throws FHIRException
	 */
	public void startObservationConverter() throws FHIRServiceException, FHIRException {
		List<ch.swing.persistence.model.Observation> observationList = ObservationController.getInstance()
				.getObservationChanges();

		if (observationList != null) {
			for (int i = 0; i < observationList.size(); i++) {
				convertObservation(observationList.get(i));
			}
		} else {
			logger.info("Database List empty!");
		}
	}

	/**
	 * Konvertiert alle Vitaldaten in eine FHIR Ressource
	 */
	public void convertObservation(ch.swing.persistence.model.Observation source) {
		Observation observation = new Observation();
		if (source.getSmisObservationId() != 0) {
			observation.addIdentifier().setSystem(CodingSystems.SYSTEM_PATIENT_INTERNAL_ID)
					.setValue(Long.toString(source.getSmisObservationId()));
		}
		observation.setStatus(ObservationStatus.FINAL);
		observation.setSubject(new Reference("Patient/" + Long.toString(source.getPatient().getSmisPatientId())));
		final Coding observationCode = observation.getCode().addCoding();
		observationCode.setCode("vital-signs") //
				.setSystem(CodingSystems.VITALDATAURL) //
				.setDisplay("Vital-Signs");
		// Blutdruck
		if (source.getCode().equals("85354-9")) {
			String[] parts = source.getValue().split("#");
			String systol = parts[0];
			String diastol = parts[1];
			String temp = "Blutdruck systolisch: " + systol + "mmHg" + "\n Blutdruck diastolisch: " + diastol + "mmHg";
			observation.setValue(new StringType(temp));
			// Puls
		} else if (source.getCode().equals("8867-4")) {
			String puls = "Puls: ";
			observation.setValue(new StringType(puls + source.getValue() + "S/min"));
			// Temperatur
		} else if (source.getCode().equals("8310-5")) {
			String temp = "Temperatur: ";
			observation.setValue(new StringType(temp + source.getValue() + "°C"));
			// Gewicht
		} else if (source.getCode().equals("29463-7")) {
			String gw = "Gewicht: ";
			observation.setValue(new StringType(gw + source.getValue() + "kg"));
			// Blutzucker
		} else if (source.getCode().equals("15074-8")) {
			String bz = "Blutzucker: ";
			observation.setValue(new StringType(bz + source.getValue() + "mmol/l"));
			// Atmung
		} else if (source.getCode().equals("9279-1")) {
			String atmg = "Atmung: ";
			observation.setValue(new StringType(atmg + source.getValue() + "AZ/min"));
		} else if (source.getCode().equals("8302-2")) {
			String gr = "Grösse: ";
			observation.setValue(new StringType(gr + source.getValue() + "cm"));
		} else {
			observation.setValue(new StringType(source.getValue()));
		}
		observation.setEffective(new DateTimeType(source.getEffectiveDate()));
		sendObservation(observation, source.getPatient().getPatientId(), source.getSmisObservationId());
	}

	/**
	 * Konvertiert die Pflegeberichtseinträge in eine FHIR Ressource
	 * 
	 * @param source
	 */
	public void convertNursingReport(ch.swing.persistence.model.NursingReport source) {
		Observation nursingReport = new Observation();
		if (source.getSwingNursingReportId() != 0) {
			nursingReport.addIdentifier().setSystem(CodingSystems.SYSTEM_PATIENT_EXTERNAL_ID)
					.setValue(Integer.toString(source.getSwingNursingReportId()));
		}

		nursingReport.setStatus(ObservationStatus.FINAL);
		nursingReport.setSubject(new Reference("Patient/" + Long.toString(source.getPatient().getSmisPatientId())));
		final Coding code = nursingReport.getCode().addCoding();
		code.setCode("nursing-report") //
				.setSystem(CodingSystems.NURSINGREPORTURL) //
				.setDisplay("Pflegebericht");
		nursingReport.setValue(new StringType(source.getValue()));
		nursingReport.setEffective(new DateTimeType(source.getNursingReportDate()));
		sendObservation(nursingReport, source.getPatient().getPatientId(), source.getSmisObservationId());
	}

	/**
	 * Sendet die Observation Ressource an den FHIR Service
	 */
	public void sendObservation(Observation observation, int patientId, Long smisObservationId) {
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
		String smisID = outcome.getId().getIdPart();
		logger.info("Observation ID: " + smisID);

		if (smisObservationId == 0) {

		}
		// Observation beinhaltet Vitaldaten
		if (observation.getCode().getCoding().get(0).getCode().equals("vital-signs")) {
			ObservationController.getInstance().updateSendDateObservation(patientId);
			if (smisObservationId == 0) {
				ObservationController.getInstance().updateSMISIDObservation(patientId, Long.parseLong(smisID));
			}
			// Observation beinhaltet Pflegeberichte
		} else {
			if (smisObservationId == 0) {
				ObservationController.getInstance().updateSMISIDNursingReport(patientId, Long.parseLong(smisID));
			}
			ObservationController.getInstance().updateSendDateNursingReport(patientId);
		}
	}
}
