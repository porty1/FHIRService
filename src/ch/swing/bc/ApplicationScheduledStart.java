package ch.swing.bc;

import java.util.concurrent.ScheduledFuture;

import org.apache.log4j.Logger;

public final class ApplicationScheduledStart {
	final static Logger logger = Logger.getLogger(ApplicationScheduledStart.class);

	private final ApplicationScheduledStart fScheduler;
	private final long fInitialDelay;
	private final long fDelayBetweenRuns;
	private final long fShutdownAfter;

	public void scheduledTask() {
		
	}

	private static final class SoundAlarmTask implements Runnable {
		@Override
		public void run() {
			++fCount;
			logger.info("beep " + fCount);
		}

		private int fCount;
	}

	private final class StopAlarmTask implements Runnable {
		StopAlarmTask(ScheduledFuture<?> aSchedFuture) {
			fSchedFuture = aSchedFuture;
		}

		@Override
		public void run() {
			logger.info("Stopping alarm.");
			fSchedFuture.cancel(false);
			/*
			 * Note that this Task also performs cleanup, by asking the
			 * scheduler to shutdown gracefully.
			 */
			fScheduler.shutdown();
		}

		private ScheduledFuture<?> fSchedFuture;
	}
}
