package ch.swing.bc;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.hl7.fhir.exceptions.FHIRException;

import ch.swing.helper.Configuration;
import ch.swing.helper.FHIRServiceException;

/**
 * 
 * @author Yannis Portmann
 *
 */
public final class FHIRServiceScheduler {
	final static Logger logger = Logger.getLogger(FHIRServiceScheduler.class);

	public static void main(String[] args) throws FHIRException, FHIRServiceException {
		//MedicationConverter.getInstance().startMedicationConverter();
		MasterdataConverter.getInstance().startMasterdataConverter();
		//ObservationConverter.getInstance().startNursingReportConverter();
		

		// Runnable runnableMasterData = new Runnable() {
		// @Override
		// public void run() {
		// try {
		// MasterdataConverter.getInstance().startMasterdataConverter();
		// } catch (FHIRServiceException | FHIRException e) {
		// logger.error(e.getMessage());
		// }
		// }
		// };
		// Runnable runnableObservation = new Runnable() {
		// @Override
		// public void run() {
		// try {
		// ObservationConverter.getInstance().startObservationConverter();
		// } catch (FHIRServiceException | FHIRException e) {
		// logger.error(e.getMessage());
		// }
		// }
		// };
		// Runnable runnableNursingReport = new Runnable() {
		// @Override
		// public void run() {
		// try {
		// ObservationConverter.getInstance().startNursingReportConverter();
		// } catch (FHIRServiceException | FHIRException e) {
		// logger.error(e.getMessage());
		// }
		// }
		// };
		// Runnable runnableMedication = new Runnable() {
		// public void run() {
		// MedicationConverter.getInstance().startMedicationConverter();
		// }
		// };
		// ScheduledExecutorService service =
		// Executors.newSingleThreadScheduledExecutor();
		// ScheduledExecutorService service2 =
		// Executors.newSingleThreadScheduledExecutor();
		// ScheduledExecutorService service3 =
		// Executors.newSingleThreadScheduledExecutor();
		// ScheduledExecutorService service4 =
		// Executors.newSingleThreadScheduledExecutor();
		// service.scheduleAtFixedRate(runnableMasterData, 0,
		// Configuration.SCHEDULERMINUTES, TimeUnit.MINUTES);
		// service2.scheduleAtFixedRate(runnableNursingReport, 0,
		// Configuration.SCHEDULERMINUTES, TimeUnit.MINUTES);
		// service3.scheduleAtFixedRate(runnableObservation, 0,
		// Configuration.SCHEDULERMINUTES, TimeUnit.MINUTES);
		// service4.scheduleAtFixedRate(runnableMedication, 0,
		// Configuration.SCHEDULERMINUTES, TimeUnit.MINUTES);
	}
}