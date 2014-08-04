package de.unibremen.swp.stundenplan.data;

import java.io.Serializable;


import javax.persistence.Column;
import javax.persistence.Id;

import de.unibremen.swp.stundenplan.persistence.Data;

/**
 * Repr�sentiert ein Fach 
 * 
 * @author Belavic, Oliver
 *
 */

public class Subject extends Activity {
    
	/**
	 * Die dem Fach zugeordnete Klasse.
	 */
	private Schoolclass klasse;

	/**
	 * �berschreibt die Methode addClass aus Activity. Da jedem Fach nur eine Klasse
	 * hinzugef�gt werden soll, f�gt addClass die Klasse aus dem Parameter nun nicht mehr der Liste der beteiligten
	 * Klassen, sondern verwendet das interne Attribut 'klasse'.
	 * 
	 * @param s
	 * 	 	Die Schulklasse, die hinzugef�gt werden soll.
	 */
	@Override
	public void addClass(final Schoolclass s){
		if(s != null){
			klasse = s;
		}
	}
}
