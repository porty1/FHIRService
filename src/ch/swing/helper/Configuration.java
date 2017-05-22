package ch.swing.helper;

/**
 * Configuration Class for all the credentials and URL's
 * 
 * @author Yannis
 *
 */
public class Configuration {

	// Database
	public static final String DBURL = "jdbc:sqlserver://vmbalios.bfh.ch:1433;DatabaseName=Swing";
	public static final String DBUSER = "Swing";
	public static final String DBPASSWORD = "GOLf1720";

	// Medication
	public static final String MEDICATIONDATEFROM = "20170101";
	public static final String MEDICATIONDATETO = "20180101";
	public static final String MEDICATIONURL = "https://smis-test.arpage.ch:443/smis2-core/orgs/";
	public static final String MEDICATIONUSERNAME = "2064905440277";
	public static final String MEDICATIONPASSWORD = "Test1234.";
	public static final String MEDICATIONORGID = "1457020138649054";
	
	//Masterdata
	public static final String FHIRSERVERURL = "https://smis-test.arpage.ch/smis2-importer/fhir";
	public static final String FHIRUSERNAME = "2064905424321";
	public static final String FHIRPASSWORD = "Test1234.";

}
