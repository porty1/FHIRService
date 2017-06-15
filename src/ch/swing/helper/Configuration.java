package ch.swing.helper;

/**
 * Configuration Class for all the credentials and URL's
 * 
 * @author Yannis
 *
 */
public class Configuration {

	// ------------- Database Settings -------------
	// ---------------------------------------------
	// URL of the Connection Database
	public static final String DBURL = "jdbc:sqlserver://vmbalios.bfh.ch:1433;DatabaseName=Swing";
	// Username for the Connection Database
	public static final String DBUSER = "Swing";
	// Password for the Connection Database
	public static final String DBPASSWORD = "GOLf1720";

	// ------------- Medication Settings -------------
	// -----------------------------------------------
	// From Date for the Medication
	public static final String MEDICATIONDATEFROM = "2017";
	// To Date for the Medication
	public static final String MEDICATIONDATETO = "2018";
	// URL for the Medication
	public static final String MEDICATIONURL = "https://smis-test.arpage.ch:443/smis2-core/orgs/";
	// Username to view the Medication (Administrator)
	public static final String MEDICATIONUSERNAME = "2064905472674";
	// Password to view the Medication
	public static final String MEDICATIONPASSWORD = "Test1234.";
	// Organisation ID in the SMIS System
	public static final String MEDICATIONORGID = "1457020138649054";

	// ------------- Masterdata Settings -------------
	// -----------------------------------------------
	// FHIR Server URL from the SMIS System
	public static final String FHIRSERVERURL = "https://smis-test.arpage.ch/smis2-importer/fhir";
	// Username for the FHIR Server
	public static final String FHIRUSERNAME = "2064905472674";
	// Password for the FHIR Server
	public static final String FHIRPASSWORD = "Test1234.";

	// ------------- Observation Settings -------------
	// ------------------------------------------------
	public static final String FHIRSERVEROBSERVATIONURL = "https://smis-test.arpage.ch/smis2-importer/fhir/Observation";

	// ------------- Scheduler Settings -------------
	// ---------------------------------------------
	// Configuration for the transfer time frame
	public static final Integer SCHEDULERMINUTES = 1;
	public static final Integer SCHEDULERMINUTESMEDICATION = 3;
}
