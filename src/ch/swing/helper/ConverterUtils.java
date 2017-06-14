package ch.swing.helper;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.exceptions.FHIRException;

import ch.swing.persistence.model.Telecom;

/**
 * Hilfsklasse f�r die Konvertierung
 * 
 * @author Yannis Portmann
 *
 */
public final class ConverterUtils {

	/**
	 * Konvertiert ein Telecom Objekt in eine ContactPoint Objekt, welches f�r
	 * die FHIR Ressource verwendet wird
	 * 
	 * @param communications
	 * @return
	 * @throws FHIRException
	 */
	public static List<ContactPoint> convertTelecom(final List<Telecom> communications) throws FHIRException {
		if (communications == null) {
			return null;
		}

		final List<ContactPoint> telecom = new ArrayList<ContactPoint>();
		for (Telecom comm : communications) {
			telecom.add(new ContactPoint() //
					.setSystem(ContactPointSystem.fromCode(comm.getSystem()))
					.setUse(ContactPointUse.fromCode(comm.getTelecomUse())).setValue(comm.getValue()));
		}
		return telecom;
	}

}
