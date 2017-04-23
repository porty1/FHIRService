package ch.swing.bc;

import org.apache.log4j.Logger;

public class MedicationConverter {
	final static Logger logger = Logger.getLogger(MedicationConverter.class);
	private static MedicationConverter mc = null;

	public static MedicationConverter getInstance() {

		if (mc == null) {
			mc = new MedicationConverter();
		}
		return mc;
	}

	/**
	 * Update the medication
	 */
	public void getMedication() {

	}
}
