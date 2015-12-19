package adt.interfaces;

import general.Count;

public interface AdtAVLBaum {
	
	/**
	 * Erzeugt einen leeren AVL Baum
	 * @return AVLBaum-Objekt
	 */
	public static AdtAVLBaum create() {
		return null;
	}
	
	/**
	 * Prueft ob der AVL Baum leer ist
	 * @return boolean
	 */
	public boolean isEmpty();
	
	/**
	 * Gibt die Hoehe des AVL Baums zurueck. Ein Knoten ohne Kinder hat die Hoehe 1
	 * @return hoehe des Baums
	 */
	public int high();
	
	/**
	 * Fuegt das Element in den AVL Baum ein und balanciert diesen ggf. aus
	 * @param elem
	 */
	public void insert(int elem);
	
	/**
	 * Fuegt das Element in den AVL Baum ein und balanciert diesen ggf. aus. Zusaetzlich wird die Laufzeit in ms gemesen
	 * @param elem
	 * @return Lauzeit in ms
	 */
	public long insertRunTime(int elem);
	
	/**
	 * Fuegt das Element in den AVL Baum ein und balanciert diesen ggf. aus. 
	 * Zusaetzlich werden Zugriffe (Links- & Rechtsrotationen, Lese- & Schreibzugriffe) gezaehlt
	 * @param elem
	 * @return Count-Objekt welches die Zugriffszahlen haelt
	 */
	public Count insertCount(int elem);
	
	/**
	 * Loescht das Element aus dem AVL Baum und balanciert diesen ggf. aus.
	 * Es lassen sich Elemente mehrfach einfuegen, diese werden im rechten Teilbaum eingefuegt.
	 * Hinweis: Duplikate erzeugen einen Fehler beim Loeschen
	 * @param elem
	 */
	public void delete(int elem);
	
	/**
	 * Loescht das Element aus dem AVL Baum und balanciert diesen ggf. aus.
	 * Es lassen sich Elemente mehrfach einfuegen, diese werden im rechten Teilbaum eingefuegt.
	 * Zusaetzlich werden Zugriffe (Links- & Rechtsrotationen, Lese- & Schreibzugriffe) gezaehlt
	 * Hinweis: Duplikate erzeugen einen Fehler beim Loeschen
	 * @param elem
	 * @return Count-Objekt welches die Zugriffszahlen haelt
	 */
	public Count deleteCount(int elem);
	
	/**
	 * Druckt den AVL Baum aus -> erzeugt eine *.dot und *.png Datei
	 * @return boolean ob erfolgreich
	 */
	public boolean print();
}
