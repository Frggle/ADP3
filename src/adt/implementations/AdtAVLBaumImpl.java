package adt.implementations;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import adt.interfaces.AdtAVLBaum;

public class AdtAVLBaumImpl implements AdtAVLBaum {
	
	private AVLKnoten wurzel;
	private Set<String> alleKnotenVerbindungen;
	
	private AdtAVLBaumImpl(){
		wurzel = null;
		alleKnotenVerbindungen = new HashSet<>();
	}
	
	public static AdtAVLBaum create(){
		return new AdtAVLBaumImpl();
	}
	
	public static void main(String[] args) {
		AdtAVLBaum baum = AdtAVLBaumImpl.create();
		baum.insert(2);
		baum.insert(4);
//		baum.insert(6);
		baum.insert(1);
		
		baum.treeToSet();
		System.err.println(baum.getSet());
		baum.print();
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
		String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		
		File file = new File("avl_" + time + ".dot");
		if(file.exists()) {
			System.err.println("Die Datei existiert bereits");
		} else {
			try {
				file.createNewFile();
			} catch(IOException e) {
				e.printStackTrace();
			}
			BufferedWriter bw = null;
			try {
				FileWriter fw = new FileWriter(file);
				bw = new BufferedWriter(fw);

				bw.write("graph avlbaum {\n");
				for(String string : alleKnotenVerbindungen) {
					bw.write(string + "\n");
				}
				bw.write("}\n");
				
			} catch(IOException e) {
				e.printStackTrace();
			} finally {
				try {
					bw.close();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	// TODO: wieder löschen, nur zum Testen
	@Override
	public Set<String> getSet() {
		return alleKnotenVerbindungen;
	}
	
	// TODO: wieder auf private setzen, nur zum Testen auf public
	@Override
	public void treeToSet() {
		if(wurzel == null) {
			System.err.println("AVL Baum ist leer!");
		} else {
			treeToSet(wurzel);
		}
	}
	
	private void treeToSet(AVLKnoten knoten) {
		if(knoten != null) {
			AVLKnoten links = knoten.getLinks();
			AVLKnoten rechts = knoten.getRechts();
//			String wertL = links == null ? "null_" : "" + links.getWert();
//			String wertR = rechts == null ? "_null" : "" + rechts.getWert();
			
			if(links != null) {
				alleKnotenVerbindungen.add(knoten.getWert() + " -- " + links.getWert());	
			} else {
				alleKnotenVerbindungen.add(knoten.getWert() + "");
			}
			if(rechts != null) {
				alleKnotenVerbindungen.add(knoten.getWert() + " -- " + rechts.getWert());	
			} else {
				alleKnotenVerbindungen.add(knoten.getWert() + "");
			}
			if(links != null) {
				treeToSet(links);
			}
			if(rechts != null) {
				treeToSet(rechts);	
			}
			
		}
	}
	
	private int high(AVLKnoten knoten) {
		return knoten == null ? -1 : knoten.getHoehe();
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
	
	private int max(int h1, int h2) {
		return h1 > h2 ? h1 : h2;
	}
	
	private AVLKnoten insert(AVLKnoten knoten, int elem) {
		if(knoten == null) {
			knoten = new AVLKnoten(elem);
		} else if(elem < knoten.getWert()) {
			knoten.setLinks(insert(knoten.getLinks(), elem)); // rekursives "Entlanghangeln"
			if(high(knoten.getLinks()) - high(knoten.getRechts()) == 2) { // prueft ob Baum balanciert
				if(elem < knoten.getLinks().getWert()) {
					knoten = rotateWithLeftChild(knoten);
				} else {
					knoten = doubleWithLeftChild(knoten);
				}
			}
		} else if(elem > knoten.getWert()) {
			knoten.setRechts(insert(knoten.getRechts(), elem));	// rekursives "Entlanghangeln"
			if(high(knoten.getRechts()) - high(knoten.getLinks()) == 2) {	// prueft ob Baum balanciert
				if(elem > knoten.getRechts().getWert()) {
					knoten = rotateWithRightChild(knoten);
				} else {
					knoten = doubleWithRightChild(knoten);
				}
			}
		} else {
			// nichts, weil Element bereits enthalten
		}
		knoten.setHoehe(max(high(knoten.getLinks()), high(knoten.getRechts())) + 1);
//		knoten.setHoehe(Math.max(high(knoten.getLinks()), high(knoten.getRechts())) + 1);
		
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
		knoten.setHoehe(max(high(knoten.getLinks()), high(knoten.getRechts())) + 1);
//		knoten.setHoehe(Math.max(high(knoten.getLinks()), high(knoten.getRechts())) + 1);
		k.setHoehe(max(high(k), high(k.getRechts())) + 1);
//		k.setHoehe(Math.max(high(k), high(k.getRechts())) + 1);
		
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
		knoten.setHoehe(max(high(knoten.getLinks()), high(knoten.getRechts())) + 1);
//		knoten.setHoehe(Math.max(high(knoten.getLinks()), high(knoten.getRechts())) + 1);
		k.setHoehe(max(high(k.getLinks()), high(k)) + 1);
//		k.setHoehe(Math.max(high(k.getLinks()), high(k)) + 1);
		
		return k;
	}
}
