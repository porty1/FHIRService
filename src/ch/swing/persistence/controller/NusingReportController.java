package ch.swing.persistence.controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ch.swing.persistence.model.old.NursingReport;

public class NusingReportController extends MasterDataController{

	private static NusingReportController nc = null;
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
	public static NusingReportController getInstance() {
		
		if (nc == null) {
			nc = new NusingReportController();
		}
		return nc;
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
	public NusingReportController() {
		try {
			connection = dc.getConnection();
		} catch (SQLException e) {
			System.err.println("There was an error getting the connection: "
					+ e.getMessage());
		}

	}
	
	public  List<NursingReport> getNursingReportChanges ()
	   {
		NursingReport nursingReport = new NursingReport();
		List<NursingReport> nursingReportList = new ArrayList<NursingReport>();
		
	    Statement stmt = null;
	    String query = "select * from NursingReport where CreationDate="+"NULL";
	    try {
	    	
	        stmt = connection.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	        while (rs.next()) {
	        	
	            int 	nursingReportId = rs.getInt("nursingReportId");
	            Date	nursingReportDate = rs.getDate("nursingReportDate");
	            String 	value = rs.getString("value");
	            int		idPatient = rs.getInt("idPatient");
	            Date 	creationDate = rs.getDate("creationDate");
	            Date 	lastUpdate = rs.getDate("lastUpdate");
	            Date 	deletionDate = rs.getDate("deletionDate");     
	            
	            nursingReport.setNursingReportId(nursingReportId);
	            nursingReport.setNursingReportDate(nursingReportDate);
	            nursingReport.setValue(value);
	            nursingReport.setPatient(getPatient(idPatient));
	            nursingReport.setCreationDate(creationDate);
	            nursingReport.setLastUpdate(lastUpdate);
	            nursingReport.setDeletionDate(deletionDate);
	            
	            nursingReportList.add(nursingReport);
	            
	            return nursingReportList;
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
