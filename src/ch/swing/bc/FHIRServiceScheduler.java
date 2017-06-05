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

		MasterdataConverter.getInstance().startMasterdataConverter();
//		ObservationConverter.getInstance().startObservationConverter();
		ObservationConverter.getInstance().startNursingReportConverter();

		Runnable runnableMasterData = new Runnable() {
			@Override
			public void run() {
				try {
					MasterdataConverter.getInstance().startMasterdataConverter();
				} catch (FHIRException e) {
					e.printStackTrace();
				} catch (FHIRServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		Runnable runnableObservation = new Runnable() {
			@Override
			public void run() {

			}
		};
		Runnable runnableMedication = new Runnable() {
			public void run() {
				MedicationConverter.getInstance().startMedicationConvertion();
			}
		};
		ScheduledExecutorService service = Executors.newScheduledThreadPool(3);
		service.scheduleAtFixedRate(runnableMasterData, 0, Configuration.SCHEDULERMINUTES, TimeUnit.MINUTES);
		service.scheduleAtFixedRate(runnableObservation, 0, Configuration.SCHEDULERMINUTES, TimeUnit.MINUTES);
		service.scheduleAtFixedRate(runnableMedication, 0, Configuration.SCHEDULERMINUTES, TimeUnit.MINUTES);
	}
}