package ch.swing.bc;

import java.util.List;

import org.apache.log4j.Logger;
import org.hl7.fhir.dstu3.model.Observation;

import ch.swing.helper.CodingSystems;
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
				.setValue(Integer.toString(source.getPatient().getSmisPatientId()));
		observation.addIdentifier().setSystem(CodingSystems.SYSTEM_PATIENT_EXTERNAL_ID)
				.setValue(Integer.toString(source.getPatient().getSwingPatientId()));
		observation.addIdentifier().setSystem(CodingSystems.ZSR_OID)
				.setValue(Integer.toString(source.getPatient().getInsuranceCard().getCardNumber()));

		// observation.setValue();

		// private Date effectiveDate;
		// private int value;
		// private String code;
		// private Patient patient;
	}

	/**
	 * Converts the nursing report to a FHIR observation object
	 * 
	 * @param source
	 */
	public void convertNursingReport(ch.swing.persistence.model.NursingReport source) {
		Observation observation = new Observation();

		// Add the all the identifiers of the Patient
		observation.addIdentifier().setSystem(CodingSystems.SYSTEM_PATIENT_INTERNAL_ID)
				.setValue(Integer.toString(source.getPatient().getSmisPatientId()));
		observation.addIdentifier().setSystem(CodingSystems.SYSTEM_PATIENT_EXTERNAL_ID)
				.setValue(Integer.toString(source.getPatient().getSwingPatientId()));
		observation.addIdentifier().setSystem(CodingSystems.ZSR_OID)
				.setValue(Integer.toString(source.getPatient().getInsuranceCard().getCardNumber()));

	}

	/**
	 * Sends the observation via FHIR to the SMIS Service
	 */
	public void sendObservation() {

	}

	/**
	 * Retrieves the observation List from the Database
	 */
	public void getObservationList() {
		List<ch.swing.persistence.model.Observation> observationList = ObservationController.getInstance()
				.getObservationChanges();

		for (int i = 0; i < observationList.size(); i++) {
			convertObservation(observationList.get(i));
		}
	}

	/**
	 * Retrieves all the Nursing Reports from the System
	 */
	public void getObservationNursingReportList() {
		List<ch.swing.persistence.model.NursingReport> nursingReportList = NusingReportController.getInstance()
				.getNursingReportChanges();

		for (int i = 0; i < nursingReportList.size(); i++) {
			convertNursingReport(nursingReportList.get(i));
		}
	}
}
