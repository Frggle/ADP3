package adt.implementations;

class AVLKnoten {
	
	/* Instanzvariablen */
	AVLKnoten links, rechts;	// Kinder
	int balance;
	int wert;
	
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
		this.balance = 0;
	}
}
