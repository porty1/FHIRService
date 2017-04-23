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

	public static void main(String[] args) {
		Runnable runnable = new Runnable() {

			public void run() {
				// task to run goes here
				System.out.println("Hello !!");

				// TODO Tasks korrekt?
				MasterdataConverter.getInstance().getPatientList();
				ObservationConverter.getInstance().getObservationList();
				ObservationConverter.getInstance().getObservationNursingReportList();
				MedicationConverter.getInstance().getMedication();
			}
		};
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(runnable, 0, 10, TimeUnit.MINUTES);
	}
}