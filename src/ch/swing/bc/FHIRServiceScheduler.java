package ch.swing.bc;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/**
 * 
 * @author Yannis Portmann
 *
 */
public final class FHIRServiceScheduler {
	final static Logger logger = Logger.getLogger(FHIRServiceScheduler.class);

	// TODO Korrekt?
	public static void main(String[] args) {
		Runnable runnableMasterData = new Runnable() {
			@Override
			public void run() {
				MasterdataConverter.getInstance().getPatientList();
			}
		};
		Runnable runnableObservation = new Runnable() {
			@Override
			public void run() {
				ObservationConverter.getInstance().getObservationNursingReportList();
				ObservationConverter.getInstance().getObservationList();
			}
		};
		Runnable runnableMedication = new Runnable() {
			public void run() {
				MedicationConverter.getInstance().getMedication();
			}
		};
		ScheduledExecutorService service = Executors.newScheduledThreadPool(3);
		service.scheduleAtFixedRate(runnableMasterData, 0, 10, TimeUnit.MINUTES);
		service.scheduleAtFixedRate(runnableObservation, 0, 10, TimeUnit.MINUTES);
		service.scheduleAtFixedRate(runnableMedication, 0, 10, TimeUnit.MINUTES);
	}
}