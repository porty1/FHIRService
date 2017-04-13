package ch.swing.test;

import java.util.ArrayList;

import com.microsoft.azure.keyvault.models.GetKeyResponseMessage;

import ch.swing.persistence.controller.*;
import ch.swing.persistence.model.Patient;
import junit.framework.TestCase;

public class Test_MaterDataController extends TestCase {

	protected Patient patient;
	protected MasterDataController mdController;
	
	protected void setUp(){
		patient = mdController.getPatient(1);
	}
	
	public void testAdd(){
		assertTrue(patient != null);
	}
}
