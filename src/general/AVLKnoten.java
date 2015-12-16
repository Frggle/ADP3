package general;

public class AVLKnoten {
	
	/* Instanzvariablen */
	private AVLKnoten links, rechts;	// Kinder
	private int hoehe;
	private int wert;
	
	/**
	 * Initialisiert einen Knoten, ohne Angabe eines Kindes.
	 * @param daten, der Integer-Wert des Knoten
	 */
	public AVLKnoten(int wert) {
		this(wert, null, null);
	}
	
	/**
	 * Initialisiert einen Knoten, mit Anbgabe der Kinder.
	 * @param daten, der Integer-Wert des Knoten
	 * @param l, das linke Kind
	 * @param r, das rechte Kind
	 */
	public AVLKnoten(int wert, AVLKnoten l, AVLKnoten r) {
		this.wert = wert;
		this.links = l;
		this.rechts = r;
		this.hoehe = 0;
	}

	/**
	 * Gibt das linke Kind zurueck
	 * @return linkes Kind
	 */
	public AVLKnoten getLinks() {
		return links;
	}

	/**
	 * Setzt das linke Kind neu
	 * @param links, neues linkes Kind
	 */
	public void setLinks(AVLKnoten links) {
		this.links = links;
	}

	/**
	 * Gibt das rechte Kind zurueck
	 * @return rechtes Kind
	 */
	public AVLKnoten getRechts() {
		return rechts;
	}

	/**
	 * Setzt das rechte Kind neu
	 * @param rechts, neues rechtes Kind
	 */
	public void setRechts(AVLKnoten rechts) {
		this.rechts = rechts;
	}

	/**
	 * Gibt die Hoehe des Knotens zurueck
	 * @return die Hoehe
	 */
	public int getHoehe() {
		return hoehe;
	}
	
	/**
	 * Setzt die Hoehe des Knoten
	 * @param h die neue Hoehe
	 */
	public void setHoehe(int h) {
		this.hoehe = h;
	}

	/**
	 * Gibt den Integer-Wert zurueck
	 * @return den Wert des Knoten
	 */
	public int getWert() {
		return wert;
	}
	
	/**
	 * Setzt den Wert des Knoten
	 */
	public void setWert(int elem) {
		this.wert = elem;
	}
}
