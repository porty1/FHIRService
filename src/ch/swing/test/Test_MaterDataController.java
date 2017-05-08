package ch.swing.test;

import org.apache.log4j.Logger;

import ch.swing.persistence.controller.MasterDataController;
import ch.swing.persistence.model.old.Patient;
import junit.framework.TestCase;

public class Test_MaterDataController extends TestCase {
	final static Logger logger = Logger.getLogger(Test_MaterDataController.class);
	
	protected Patient patient;
	protected MasterDataController mdController;
	
	protected void setUp(){
		patient = mdController.getPatient(2);
	}
	
	public void testAdd(){
		logger.info(patient.toString());
	}
}
