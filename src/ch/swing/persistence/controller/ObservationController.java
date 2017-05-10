package ch.swing.persistence.controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ch.swing.persistence.model.Observation;

public class ObservationController extends MasterDataController {
	
	private static ObservationController oc = null;
	private static DB_Connection dc = DB_Connection.getInstance();
	private static Connection connection = null;
	
	/**
	 * With the getInstance method we make sure that only one object of the PatientCaseController will be used<br>
	 * <br>
	 *
	 * @param none
	 * @return pc
	 * 
	 * <br>
	 */
	public static ObservationController getInstance() {
		
		if (oc == null) {
			oc = new ObservationController();
		}
		return oc;
	}
	
	/**
	 * PatientCaseController constructor<br>
	 * gets the connection with the database which is used for the methods<br>
	 * <br>
	 *
	 * @param none
	 * @exception the SQL Exception is catched and shows if the connection failed
	 * 
	 * <br>
	 */
	// Static block for initialization
	public ObservationController() {
		try {
			connection = dc.getConnection();
		} catch (SQLException e) {
			System.err.println("There was an error getting the connection: "
					+ e.getMessage());
		}

	}
	
	public  List<Observation> getObservationChanges ()
	   {
		Observation observation = new Observation();
		List<Observation> observationList = new ArrayList<Observation>();
		
	    Statement stmt = null;
	    String query = "select * from Observation where CreationDate IS NULL";
	    try {
	    	
	        stmt = connection.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	        while (rs.next()) {
	        	
	            int 	observationId = rs.getInt("observationId");
	            Date	effectiveDate = rs.getDate("effectiveDate");
	            String	value = rs.getString("value");
	            String 	code = rs.getString("code");
	            int		idPatient = rs.getInt("idPatient");
	            Date 	creationDate = rs.getDate("creationDate");
	            Date 	lastUpdate = rs.getDate("lastUpdate");
	            Date 	deletionDate = rs.getDate("deletionDate");
	            
	            observation.setObservationId(observationId);
	            observation.setEffectiveDate(effectiveDate);
	            observation.setValue(value);
	            observation.setCode(code);
	            observation.setPatient(getPatient(idPatient));
	            observation.setCreationDate(creationDate);
	            observation.setLastUpdate(lastUpdate);
	            observation.setDeletionDate(deletionDate);         
	            
	            observationList.add(observation);
	            
	            return observationList;
	        }
	    } catch (SQLException err ) {
	    	System.out.println(err.getMessage());
	    } finally {
	        if (stmt != null) { try {
				stmt.close();
			} catch (SQLException err) {
				System.out.println(err.getMessage());
			} }
	    }
		return null;
		
	}
	
}
