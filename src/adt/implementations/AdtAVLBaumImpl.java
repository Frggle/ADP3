package adt.implementations;

import general.AVLKnoten;
import general.Count;
import general.Helper;
import java.util.HashSet;
import java.util.Set;
import adt.interfaces.AdtAVLBaum;

public class AdtAVLBaumImpl implements AdtAVLBaum {
	
	private AVLKnoten wurzel;	/* Wurzel des AVL Baums */
	private Set<Integer> rotateConditionSet;	/* Haelt die Werte, welche anzeigen ob rotiert werden muss */
	
	/* Zaehlvariablen fuer Zugriffe und Rotationen */
	private int countRead;
	private int countWrite;
	private int rightRotCounter;
	private int leftRotCounter;
	
	private Helper helper = new Helper();	/* kuemmert sich um den *.png-Druck */
	
	/* privater Konstruktor */
	private AdtAVLBaumImpl() {
		wurzel = null;

		rotateConditionSet = new HashSet<>();
		rotateConditionSet.add(-2);
		rotateConditionSet.add(2);
		
		initCounter();
	}
	
	/**
	 * Initialisiert die Zaehlvariablen
	 */
	private void initCounter() {
		countRead = 0;
		countWrite = 0;
		rightRotCounter = 0;
		leftRotCounter = 0;
	}
	
	public static AdtAVLBaum create() {
		return new AdtAVLBaumImpl();
	}
	
	@Override
	public boolean isEmpty() {
		return wurzel == null;
	}
	
	@Override
	public int high() {
		return wurzel.getHoehe();
	}
	
	@Override
	public void insert(int elem) {
		wurzel = insert(wurzel, elem);
	}
	
	@Override
	public void delete(int elem) {
		wurzel = delete(wurzel, elem);
	}
	
	@Override
	public boolean print() {
		
		return helper.print(wurzel);
	}
	
	@Override
	public long insertRunTime(int elem) {
		long startTime = System.currentTimeMillis();
		this.insert(elem);
		return System.currentTimeMillis() - startTime;
	}
	
	@Override
	public Count insertCount(int elem) {
		initCounter();
		wurzel = insertCounter(wurzel, elem);
		return new Count(countRead, countWrite, leftRotCounter, rightRotCounter);
	}
	
	@Override
	public Count deleteCount(int elem) {
		initCounter();
		wurzel = deleteCounter(wurzel, elem);
		return new Count(countRead, countWrite, leftRotCounter, rightRotCounter);
	}
	
	/**
	 * Fuegt ein Element in den AVL Baum ein und rotiert ggf.
	 * @param knoten, fuer den rekurisiven Aufruf
	 * @param elem, welches eingefuegt werden soll
	 * @return knoten
	 */
	private AVLKnoten insert(AVLKnoten knoten, int elem) {
		if(knoten == null) {
			knoten = new AVLKnoten(elem);
		} else if(elem < knoten.getWert()) {
			knoten.setLinks(insert(knoten.getLinks(), elem)); // rekursives "Entlanghangeln"
			
			knoten = rebalanceInsert(knoten, elem);	// prueft ob AVL-Bedingung verletzt und korrigiert ggf.
		} else if(elem >= knoten.getWert()) {
			knoten.setRechts(insert(knoten.getRechts(), elem));	// rekursives "Entlanghangeln"
			
			knoten = rebalanceInsert(knoten, elem);
		} else {
			// nichts, (weil Element bereits enthalten -> spielt keine Rolle, weil Duplikate zugelassen sind)
		}
		knoten.setHoehe(Math.max(high(knoten.getLinks()), high(knoten.getRechts())) + 1);
		
		return knoten;
	}
	
	/**
	 * Loescht ein Element aus dem AVL Baum und rotiert ggf.
	 * @param knoten, fuer den rekursiven Aufruf
	 * @param elem, welches geloescht werden soll
	 * @return knoten
	 */
	private AVLKnoten delete(AVLKnoten knoten, int elem) {
		if(knoten == null) {
			return knoten;
		}
		int knotenWert = knoten.getWert();
		if(elem < knotenWert) {
			knoten.setLinks(delete(knoten.getLinks(), elem));
			
			knoten = rebalanceDelete(knoten, elem);	// prueft ob AVL-Bedingung verletzt und korrigiert ggf.
		} else if(elem > knotenWert) {
			knoten.setRechts(delete(knoten.getRechts(), elem));
			
			knoten = rebalanceDelete(knoten, elem);	// prueft ob AVL-Bedingung verletzt und korrigiert ggf.
		} else {	// zu loeschendes Element gefunden
			if(knoten.getHoehe() == 1) {	// keine Kinder
				knoten = null;
			} else if((knoten.getLinks() != null) ^ (knoten.getRechts() != null)) {	// genau EIN Kind
				if(knoten.getLinks() != null) {	// setze Kind auf zu loeschenden Knoten
					knoten = knoten.getLinks();
				} else {
					knoten = knoten.getRechts();
				}
			} else if((knoten.getLinks() != null) && (knoten.getRechts() != null)) {// zwei Kinder
				AVLKnoten minKnoten = knoten.getRechts();	// minimalster Knoten im rechten Teilbaum (ausgehend vom zu
														  // loeschenden Knoten)
				while(minKnoten.getLinks() != null) {
					minKnoten = minKnoten.getLinks();
				}
				delete(knoten, minKnoten.getWert());	// loescht in dem Teilbaum den Knoten, dessen Wert kopiert wird
				knoten.setWert(minKnoten.getWert());	// kopiert den Wert vom geloeschten Knoten
				knoten.setHoehe(Math.max(high(knoten.getLinks()), high(knoten.getRechts())) + 1);
			}
		}
		
		return knoten;
	}
	
	/**
	 * Gibt die Hoehe des Knoten zurueck. <br> 
	 * 0, wenn Knoten null <br>
	 * 1, wenn keine Kinder <br>
	 * @param knoten
	 * @return hoehe; wenn null -> 0, ansonsten die Hoehe
	 */
	private int high(AVLKnoten knoten) {
		return knoten == null ? 0 : knoten.getHoehe();
	}
	
	/**
	 * Prueft ob die AVL Bedingung verletzt ist und rotiert ggf.
	 * @param knoten am dem die Bedingung geprueft wird
	 * @param elem
	 * @return
	 */
	private AVLKnoten rebalanceInsert(AVLKnoten knoten, int elem) {
		if(elem < knoten.getWert()) {
			if(rotateConditionSet.contains(high(knoten.getRechts()) - high(knoten.getLinks()))) {	// prueft ob Baum balanciert
				if(elem < knoten.getLinks().getWert()) {
					knoten = rechtsRotationUmLinkesKind(knoten);
				} else { // wenn das neue Element ein ZickZack (grafisch) verursacht hat -> Links-Rechts (bspw. 4, 2, 3)
					knoten = linksRechtsRotation(knoten);
				}
			}
		} else {
			if(rotateConditionSet.contains(high(knoten.getRechts()) - high(knoten.getLinks()))) {	// prueft ob Baum balanciert
				if(elem >= knoten.getRechts().getWert()) {
					knoten = linksRotationUmRechtesKind(knoten);
				} else { // wenn das neue Element ein ZickZack (grafisch) verursacht hat -> Rechts-Links (bspw. 4, 6, 5)
					knoten = rechtsLinksRotation(knoten);
				}
			}
		}
		
		return knoten;
	}
	
	/**
	 * Prueft ob die AVL Bedingung verletzt ist und rotiert ggf.
	 * @param knoten am dem die Bedingung geprueft wird
	 * @param elem
	 * @return
	 */
	private AVLKnoten rebalanceDelete(AVLKnoten knoten, int elem) {
		int knotenWert = knoten.getWert();
		if(knotenWert > elem) {
			if(rotateConditionSet.contains(high(knoten.getRechts()) - high(knoten.getLinks()))) {	// prueft ob Baum balanciert
				if(knotenWert < knoten.getRechts().getWert()) {
					knoten = linksRotationUmRechtesKind(knoten);
				} else { // wenn das neue Element ein ZickZack (grafisch) verursacht hat -> Rechts-Links (bspw. 4, 6, 5)
					knoten = rechtsLinksRotation(knoten);
				}
			}
		} else {
			if(rotateConditionSet.contains(high(knoten.getRechts()) - high(knoten.getLinks()))) { // prueft ob Baum balanciert
				if(knotenWert > knoten.getLinks().getWert()) {
					knoten = rechtsRotationUmLinkesKind(knoten);
				} else { // wenn das neue Element ein ZickZack (grafisch) verursacht hat -> Links-Rechts (bspw. 4, 2, 3)
					knoten = linksRechtsRotation(knoten);
				}
			}
		}
		
		return knoten;
	}

	/**
	 * Doppelrotation - erst Rechtsrotation, dann Linksrotation
	 * @param knoten
	 * @return
	 */
	private AVLKnoten rechtsLinksRotation(AVLKnoten knoten) {
		knoten.setRechts(rechtsRotationUmLinkesKind(knoten.getRechts()));
		
		return linksRotationUmRechtesKind(knoten);
	}
	
	/**
	 * Einfache Linksrotation <br>
	 * Rotiert um das rechte Kind vom uebergebenen Knoten
	 * @param alteTeilbaumWurzel
	 * @return neueTeilbaumwurzel
	 */
	private AVLKnoten linksRotationUmRechtesKind(AVLKnoten alteTeilbaumWurzel) {
		AVLKnoten neueTeilbaumWurzel = alteTeilbaumWurzel.getRechts();
		alteTeilbaumWurzel.setRechts(neueTeilbaumWurzel.getLinks());
		neueTeilbaumWurzel.setLinks(alteTeilbaumWurzel);
		alteTeilbaumWurzel.setHoehe(Math.max(high(alteTeilbaumWurzel.getLinks()), high(alteTeilbaumWurzel.getRechts())) + 1);
		neueTeilbaumWurzel.setHoehe(Math.max(high(alteTeilbaumWurzel), high(neueTeilbaumWurzel.getRechts())) + 1);
		
		return neueTeilbaumWurzel;
	}
	
	/**
	 * Doppelrotation - erst Linksrotation, dann Rechtsrotation
	 * @param knoten
	 * @return
	 */
	private AVLKnoten linksRechtsRotation(AVLKnoten knoten) {
		knoten.setLinks(linksRotationUmRechtesKind(knoten.getLinks()));
		
		return rechtsRotationUmLinkesKind(knoten);
	}
	
	/**
	 * Einfache Rechtsrotation <br>
	 * Rotiert um das linke Kind vom uebergebenen Knoten
	 * @param alteTeilbaumWurzel
	 * @return neueTeilbaumwurzel
	 */
	private AVLKnoten rechtsRotationUmLinkesKind(AVLKnoten alteTeilbaumWurzel) {
		AVLKnoten neueTeilbaumWurzel = alteTeilbaumWurzel.getLinks();
		alteTeilbaumWurzel.setLinks(neueTeilbaumWurzel.getRechts());
		neueTeilbaumWurzel.setRechts(alteTeilbaumWurzel);
		alteTeilbaumWurzel.setHoehe(Math.max(high(alteTeilbaumWurzel.getLinks()), high(alteTeilbaumWurzel.getRechts())) + 1);
		neueTeilbaumWurzel.setHoehe(Math.max(high(neueTeilbaumWurzel.getLinks()), high(alteTeilbaumWurzel)) + 1);
		
		return neueTeilbaumWurzel;
	}
	
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------- jetzt folgenden die Messmethoden -------------------------------
	// --------------------------------------------------------------------------------------------------------
	
	/**
	 * Loescht ein Element aus dem AVL Baum und rotiert ggf. <br>
	 * Zusaetzlich werden die Zugriffe und Rotationen gezaehlt
	 * @param knoten, fuer den rekursiven Aufruf
	 * @param elem, welches geloescht werden soll
	 * @return knoten
	 */
	private AVLKnoten deleteCounter(AVLKnoten knoten, int elem) {
		if(knoten == null) {
			return knoten;
		}
		countRead++;
		int knotenWert = knoten.getWert();
		if(knotenWert > elem) {
			countRead++;
			countWrite++;
			knoten.setLinks(deleteCounter(knoten.getLinks(), elem));
			
			knoten = rebalanceDeleteCounter(knoten, elem);
		} else if(knotenWert < elem) {
			countRead++;
			countWrite++;
			knoten.setRechts(deleteCounter(knoten.getRechts(), elem));
			
			knoten = rebalanceDeleteCounter(knoten, elem);
		} else {    // zu loeschendes Element gefunden
			countRead += 5;
			if(knoten.getHoehe() == 1) {    // keine Kinder
				knoten = null;
			} else if((knoten.getLinks() != null) ^ (knoten.getRechts() != null)) { // genau EIN Kind
				countRead++;
				if(knoten.getLinks() != null) { // setze Kind auf zu loeschenden Knoten
					countRead++;
					knoten = knoten.getLinks();
				} else {
					countRead++;
					knoten = knoten.getRechts();
				}
			} else if((knoten.getLinks() != null) && (knoten.getRechts() != null)) {// zwei Kinder
				countRead++;
				AVLKnoten minKnoten = knoten.getRechts();   // minimalster Knoten im rechten Teilbaum (ausgehend vom zu
														  // loeschenden Knoten)
				countRead++;
				while(minKnoten.getLinks() != null) {
					countRead++;
					minKnoten = minKnoten.getLinks();
					countRead++;
				}
				countRead++;
				deleteCounter(knoten, minKnoten.getWert());    // loescht in dem Teilbaum den Knoten, dessen Wert kopiert
															// wird
				countRead++;
				countWrite++;
				knoten.setWert(minKnoten.getWert());    // kopiert den Wert vom geloeschten Knoten
				countRead += 2;
				countWrite++;
				knoten.setHoehe(Math.max(high(knoten.getLinks()), high(knoten.getRechts())) + 1);
			}
		}
		
		return knoten;
	}
	
	/**
	 * Fuegt ein Element in den AVL Baum ein und rotiert ggf.<br>
	 * Zusaetzlich werden die Zugriffe gezaehlt
	 * @param knoten, fuer den rekurisiven Aufruf
	 * @param elem, welches eingefuegt werden soll
	 * @return knoten
	 */
	private AVLKnoten insertCounter(AVLKnoten knoten, int elem) {
		countRead += 2; // fuer die else Abfragen
		if(knoten == null) {
			knoten = new AVLKnoten(elem);
		} else if(elem < knoten.getWert()) {
			knoten.setLinks(insertCounter(knoten.getLinks(), elem)); // rekursives "Entlanghangeln"
			countRead++;
			countWrite++;
			
			knoten = rebalanceInsertCounter(knoten, elem);
		} else if(elem >= knoten.getWert()) {
			knoten.setRechts(insertCounter(knoten.getRechts(), elem)); // rekursives "Entlanghangeln"
			countRead++;
			countWrite++;
			
			knoten = rebalanceInsertCounter(knoten, elem);
		} else {
			// nichts, (weil Element bereits enthalten -> spielt keine Rolle, weil Duplikate zugelassen sind)
		}
		countRead += 2;
		countWrite++;
		knoten.setHoehe(Math.max(high(knoten.getLinks()), high(knoten.getRechts())) + 1);
		
		return knoten;
	}
	
	/**
	 * Einfache Rechtsrotation <br>
	 * Rotiert um das linke Kind vom uebergebenen Knoten <br>
	 * Zusaetzlich werden die Zugriffe gezaehlt
	 * @param alteTeilbaumWurzel
	 * @return neueTeilbaumwurzel
	 */
	private AVLKnoten rechtsRotationUmLinkesKindCounter(AVLKnoten alteTeilbaumWurzel) {
		AVLKnoten neueTeilbaumWurzel = alteTeilbaumWurzel.getLinks();
		countRead++;
		alteTeilbaumWurzel.setLinks(alteTeilbaumWurzel.getRechts());
		countRead++;
		countWrite++;
		neueTeilbaumWurzel.setRechts(alteTeilbaumWurzel);
		countWrite++;
		alteTeilbaumWurzel.setHoehe(Math.max(high(alteTeilbaumWurzel.getLinks()), high(alteTeilbaumWurzel.getRechts())) + 1);
		countWrite++;
		countRead += 2;
		neueTeilbaumWurzel.setHoehe(Math.max(high(neueTeilbaumWurzel.getLinks()), high(alteTeilbaumWurzel)) + 1);
		countRead++;
		countWrite++;
		
		rightRotCounter++;
		return neueTeilbaumWurzel;
	}
	
	/**
	 * Einfache Linksrotation <br>
	 * Rotiert um das rechte Kind vom uebergebenen Knoten <br>
	 * Zusaetzlich werden die Zugriffe gezaehlt
	 * @param alteTeilbaumWurzel
	 * @return neueTeilbaumwurzel
	 */
	private AVLKnoten linksRotationUmRechtesKindCounter(AVLKnoten alteTeilbaumWurzel) {
		AVLKnoten neueTeilbaumWurzel = alteTeilbaumWurzel.getRechts();
		countRead++;
		alteTeilbaumWurzel.setRechts(neueTeilbaumWurzel.getLinks());
		countRead++;
		countWrite++;
		neueTeilbaumWurzel.setLinks(alteTeilbaumWurzel);
		countWrite++;
		alteTeilbaumWurzel.setHoehe(Math.max(high(alteTeilbaumWurzel.getLinks()), high(alteTeilbaumWurzel.getRechts())) + 1);
		countWrite++;
		countRead += 2;
		neueTeilbaumWurzel.setHoehe(Math.max(high(alteTeilbaumWurzel), high(neueTeilbaumWurzel.getRechts())) + 1);
		countRead++;
		countWrite++;
		
		leftRotCounter++;
		return neueTeilbaumWurzel;
	}
	
	/**
	 * Doppelrotation - erst Linksrotation, dann Rechtsrotation
	 * @param knoten
	 * @return
	 */
	private AVLKnoten linksRechtsRotationCounter(AVLKnoten knoten) {
		knoten.setLinks(linksRotationUmRechtesKindCounter(knoten.getLinks()));
		countRead++;
		countWrite++;
		leftRotCounter++;
		rightRotCounter++;
		return rechtsRotationUmLinkesKindCounter(knoten);
	}
	
	/**
	 * Doppelrotation - erst Rechtsrotation, dann Linksrotation
	 * @param knoten
	 * @return
	 */
	private AVLKnoten rechtsLinksRotationCounter(AVLKnoten knoten) {
		knoten.setRechts(rechtsRotationUmLinkesKindCounter(knoten.getRechts()));
		countRead++;
		countWrite++;
		leftRotCounter++;
		rightRotCounter++;
		return linksRotationUmRechtesKindCounter(knoten);
	}
	
	/**
	 * Prueft ob die AVL Bedingung verletzt ist und rotiert ggf. <br>
	 * Zusaetzlich werden die Zugriffe und Rotationen gezaehlt
	 * @param knoten am dem die Bedingung geprueft wird
	 * @param elem
	 * @return
	 */
	private AVLKnoten rebalanceInsertCounter(AVLKnoten knoten, int elem) {
		countRead++;
		if(elem < knoten.getWert()) {
			countRead+=2;
			if(rotateConditionSet.contains(high(knoten.getRechts()) - high(knoten.getLinks()))) {	// prueft ob Baum balanciert
				countRead+=2;
				if(elem < knoten.getLinks().getWert()) {
					rightRotCounter++;
					knoten = rechtsRotationUmLinkesKindCounter(knoten);
				} else { // wenn das neue Element ein ZickZack (grafisch) verursacht hat -> Links-Rechts (bspw. 4, 2, 3)
					knoten = linksRechtsRotationCounter(knoten);
				}
			}
		} else {
			countRead+=2;
			if(rotateConditionSet.contains(high(knoten.getRechts()) - high(knoten.getLinks()))) {	// prueft ob Baum balanciert
				countRead+=2;
				if(elem >= knoten.getRechts().getWert()) {
					leftRotCounter++;
					knoten = linksRotationUmRechtesKindCounter(knoten);
				} else { // wenn das neue Element ein ZickZack (grafisch) verursacht hat -> Rechts-Links (bspw. 4, 6, 5)
					knoten = rechtsLinksRotationCounter(knoten);
				}
			}
		}
		
		return knoten;
	}
	
	/**
	 * Prueft ob die AVL Bedingung verletzt ist und rotiert ggf. <br>
	 * Zusaetzlich werden die Zugriffe und Rotationen gezaehlt
	 * @param knoten am dem die Bedingung geprueft wird
	 * @param elem
	 * @return
	 */
	private AVLKnoten rebalanceDeleteCounter(AVLKnoten knoten, int elem) {
		countRead++;
		int knotenWert = knoten.getWert();
		if(knotenWert > elem) {
			countRead+=2;
			if(rotateConditionSet.contains(high(knoten.getRechts()) - high(knoten.getLinks()))) {	// prueft ob Baum balanciert
				countRead+=2;
				if(knotenWert < knoten.getRechts().getWert()) {
					leftRotCounter++;
					knoten = linksRotationUmRechtesKindCounter(knoten);
				} else { // wenn das neue Element ein ZickZack (grafisch) verursacht hat -> Rechts-Links (bspw. 4, 6, 5)
					knoten = rechtsLinksRotationCounter(knoten);
				}
			}
		} else {
			countRead+=2;
			if(rotateConditionSet.contains(high(knoten.getRechts()) - high(knoten.getLinks()))) { // prueft ob Baum balanciert
				countRead+=2;
				if(knotenWert > knoten.getLinks().getWert()) {
					rightRotCounter++;
					knoten = rechtsRotationUmLinkesKindCounter(knoten);
				} else { // wenn das neue Element ein ZickZack (grafisch) verursacht hat -> Links-Rechts (bspw. 4, 2, 3)
					knoten = linksRechtsRotationCounter(knoten);
				}
			}
		}
		
		return knoten;
	}
}
