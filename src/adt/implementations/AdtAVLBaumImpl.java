package adt.implementations;

import adt.interfaces.AdtAVLBaum;

public class AdtAVLBaumImpl implements AdtAVLBaum {
	
	private AVLKnoten wurzel;
	
	private AdtAVLBaumImpl(){
		wurzel = null;
	}
	
	public AdtAVLBaum create(){
		return new AdtAVLBaumImpl();
	}
	
	public static void main(String[] args) {
		System.loadLibrary("gv_java");
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
		// TODO Auto-generated method stub
	}
	
	@Override
	public boolean print() {
		// TODO GraphViz .dot Export
		return false;
	}

	private boolean search(int elem) {
		return search(wurzel, elem);
	}
	
	private boolean search(AVLKnoten knoten, int elem) {
		boolean found = false;
		while(knoten != null && !found) {
			int wert = knoten.getWert();
			if(elem < wert) {
				knoten = knoten.getLinks();
			} else if(elem > wert) {
				knoten = knoten.getRechts();
			} else {
				found = true;
				break;
			}
			found = search(knoten, elem);
		}
		
		return found;	
	}
	
	private AVLKnoten insert(AVLKnoten knoten, int elem) {
		if(knoten == null) {
			knoten = new AVLKnoten(elem);
		} else if(elem < knoten.getWert()) {
			knoten.setLinks(insert(knoten.getLinks(), elem)); // rekursives "Entlanghangeln"
			if(knoten.getLinks().getHoehe() - knoten.getRechts().getHoehe() == 2) { // prueft ob Baum balanciert
				if(elem < knoten.getLinks().getWert()) {
					knoten = rotateWithLeftChild(knoten);
				} else {
					knoten = doubleWithLeftChild(knoten);
				}
			}
		} else if(elem > knoten.getWert()) {
			knoten.setRechts(insert(knoten.getRechts(), elem));	// rekursives "Entlanghangeln"
			if(knoten.getRechts().getHoehe() - knoten.getLinks().getHoehe() == 2) {	// prueft ob Baum balanciert
				if(elem > knoten.getRechts().getWert()) {
					knoten = rotateWithRightChild(knoten);
				} else {
					knoten = doubleWithRightChild(knoten);
				}
			}
		} else {
			// nichts, weil Element bereits enthalten
		}
		knoten.setHoehe(Math.max(knoten.getLinks().getHoehe(), knoten.getRechts().getHoehe()) + 1);
		
		return knoten;
	}
	
	private AVLKnoten doubleWithRightChild(AVLKnoten knoten) {
		knoten.setRechts(rotateWithLeftChild(knoten.getRechts()));
		
		return rotateWithRightChild(knoten);
	}

	private AVLKnoten rotateWithRightChild(AVLKnoten knoten) {
		AVLKnoten k = knoten.getRechts();
		k.setRechts(knoten.getLinks());
		k.setLinks(knoten);
		knoten.setHoehe(Math.max(knoten.getLinks().getHoehe(), knoten.getRechts().getHoehe()) + 1);
		k.setHoehe(Math.max(k.getHoehe(), k.getRechts().getHoehe()) + 1);
		
		return k;
	}

	private AVLKnoten doubleWithLeftChild(AVLKnoten knoten) {
		knoten.setLinks(rotateWithRightChild(knoten.getLinks()));
		
		return rotateWithLeftChild(knoten);
	}

	private AVLKnoten rotateWithLeftChild(AVLKnoten knoten) {
		AVLKnoten k = knoten.getLinks();
		k.setLinks(knoten.getRechts());
		k.setRechts(knoten);
		knoten.setHoehe(Math.max(knoten.getLinks().getHoehe(), knoten.getRechts().getHoehe()) + 1);
		k.setHoehe(Math.max(k.getLinks().getHoehe(), k.getHoehe()) + 1);
		
		return k;
	}
}
