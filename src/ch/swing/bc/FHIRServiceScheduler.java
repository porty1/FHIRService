package ch.swing.bc;

import org.apache.log4j.Logger;
import org.hl7.fhir.exceptions.FHIRException;

import ch.swing.helper.FHIRServiceException;

/**
 * 
 * @author Yannis Portmann
 *
 */
public final class FHIRServiceScheduler {
	final static Logger logger = Logger.getLogger(FHIRServiceScheduler.class);

	public static void main(String[] args) throws FHIRException, FHIRServiceException {
//		MasterdataConverter.getInstance().getPatientList();
//		ObservationConverter.getInstance().getObservationNursingReportList();
//		ObservationConverter.getInstance().getObservationList();
		MedicationConverter.getInstance().getMedication();
		
//		Runnable runnableMasterData = new Runnable() {
//			@Override
//			public void run() {
//				try {
//					MasterdataConverter.getInstance().getPatientList();
//				} catch (FHIRException e) {
//					e.printStackTrace();
//				}
//			}
//		};
//		Runnable runnableObservation = new Runnable() {
//			@Override
//			public void run() {
//				ObservationConverter.getInstance().getObservationNursingReportList();
//				ObservationConverter.getInstance().getObservationList();
//			}
//		};
//		Runnable runnableMedication = new Runnable() {
//			public void run() {
//				MedicationConverter.getInstance().getMedication();
//			}
//		};
//		ScheduledExecutorService service = Executors.newScheduledThreadPool(3);
//		service.scheduleAtFixedRate(runnableMasterData, 0, 10, TimeUnit.MINUTES);
//		service.scheduleAtFixedRate(runnableObservation, 0, 10, TimeUnit.MINUTES);
//		service.scheduleAtFixedRate(runnableMedication, 0, 10, TimeUnit.MINUTES);
	}
}