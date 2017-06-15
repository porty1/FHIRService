package ch.swing.bc;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.hl7.fhir.exceptions.FHIRException;

import ch.swing.helper.Configuration;
import ch.swing.helper.FHIRServiceException;

/**
 * Dies ist der Scheduler, welcher gestartet wird, damit der Prozess alle X
 * Minuten durchläuft Die Konfiguration erfolgt über das Configuration file
 * 
 * @author Yannis Portmann
 *
 */
public final class FHIRServiceScheduler {
	final static Logger logger = Logger.getLogger(FHIRServiceScheduler.class);

	public static void main(String[] args) throws FHIRException, FHIRServiceException {

		// MasterdataConverter.getInstance().startMasterdataConverter();
		// ObservationConverter.getInstance().startNursingReportConverter();
		// ObservationConverter.getInstance().startObservationConverter();
		// MedicationConverter.getInstance().startMedicationConverter();

		Runnable runnableMasterData = new Runnable() {
			@Override
			public void run() {
				try {
					MasterdataConverter.getInstance().startMasterdataConverter();
				} catch (FHIRServiceException | FHIRException e) {
					logger.error(e.getMessage());
				}
			}
		};
		Runnable runnableObservation = new Runnable() {
			@Override
			public void run() {
				try {
					ObservationConverter.getInstance().startObservationConverter();
				} catch (FHIRServiceException | FHIRException e) {
					logger.error(e.getMessage());
				}
			}
		};
		Runnable runnableNursingReport = new Runnable() {
			@Override
			public void run() {
				try {
					ObservationConverter.getInstance().startNursingReportConverter();
				} catch (FHIRServiceException | FHIRException e) {
					logger.error(e.getMessage());
				}
			}
		};
		Runnable runnableMedication = new Runnable() {
			public void run() {
				MedicationConverter.getInstance().startMedicationConverter();
			}
		};
		ScheduledExecutorService service = Executors.newScheduledThreadPool(4);
		// ScheduledExecutorService service =
		// Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(runnableMasterData, 0, Configuration.SCHEDULERMINUTES, TimeUnit.MINUTES);
		service.scheduleAtFixedRate(runnableNursingReport, 0, Configuration.SCHEDULERMINUTES, TimeUnit.MINUTES);
		service.scheduleAtFixedRate(runnableObservation, 0, Configuration.SCHEDULERMINUTES, TimeUnit.MINUTES);
		service.scheduleAtFixedRate(runnableMedication, 0, Configuration.SCHEDULERMINUTESMEDICATION, TimeUnit.MINUTES);
	}
}