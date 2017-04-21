package ch.swing.bc;

import org.apache.log4j.Logger;
import org.hl7.fhir.dstu3.model.Observation;

/**
 * 
 * @author Yannis Portmann
 *
 */
public class ObservationConverter {
	final static Logger logger = Logger.getLogger(ObservationConverter.class);

	/**
	 * Converts the observation to a FHIR observation object
	 */
	public void convertObservation() {
		Observation observation = new Observation();
		
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
		
	}

	/**
	 * Retrieves all the Nursing Reports from the System
	 */
	public void getObservationNursingReportList() {
		
	}
}
