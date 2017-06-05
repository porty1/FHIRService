package ch.swing.helper;

import java.util.HashMap;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;

public final class CodingSystems {

	/**
	 * Blocked constructor.
	 */
	private CodingSystems() {
	}

	public static final String GLN_OID = "urn:oid:2.51.1.3";

	public static final String ZSR_OID = "urn:oid:2.16.756.5.30.1.123.1";

	public static final String SYSTEM_PATIENT_INTERNAL_ID = "http://smis.ch/fhir/patient/internal_id";

	public static final String SYSTEM_PATIENT_EXTERNAL_ID = "http://smis.ch/fhir/patient/external_id";

	// TODO Patrick muss Link noch liefern
	public static final String NURSINGREPORTURL = "http://smis.ch/fhir/types/open";

	public static final String VITALDATAURL = "http://smis.ch/fhir/types/open";

	// TODO Funktioniert so noch nicht
	@SuppressWarnings("serial")
	public static final Map<Short, AdministrativeGender> GENDERMAP = new HashMap<Short, AdministrativeGender>() {
		{
			put((short) 0, AdministrativeGender.UNKNOWN);
			put((short) 99, AdministrativeGender.FEMALE);
			put((short) 100, AdministrativeGender.MALE);
		}
	};
}
