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
	
	private AdtAVLBaumImpl() {
		wurzel = null;
		alleKnotenVerbindungen = new HashSet<>();
	}
	
	public static AdtAVLBaum create() {
		return new AdtAVLBaumImpl();
	}
	
	public static void main(String[] args) {
		AdtAVLBaum baum = AdtAVLBaumImpl.create();
		
		baum.insert(4);
		baum.insert(2);
		baum.insert(3);
		baum.insert(3);

		baum.print();
		System.err.println(baum.getSet());
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
		treeToSet();
		
		String time = new SimpleDateFormat("MMdd_HHmmss").format(Calendar.getInstance().getTime());
		
		File file = new File("avl_" + time + ".dot");

		int i = 1;
		while(file.exists()) {
			file = new File("avl_" + time + "_" + i + ".dot");
			i++;
		}
		
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
			System.err.println("Datei wurde erstellt");
		} catch(IOException e) {
			return false;
		} finally {
			try {
				bw.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	// TODO: wieder löschen, nur zum Testen
	@Override
	public Set<String> getSet() {
		return alleKnotenVerbindungen;
	}
	
	private void treeToSet() {
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
			
			// Fuegt wenn Kind == null einen leeren String ein -> wird in GraphViz ignoriert
			alleKnotenVerbindungen.add(knoten.getWert() + (links == null ? "" : " -- " + links.getWert()));
			alleKnotenVerbindungen.add(knoten.getWert() + (rechts == null ? "" : " -- " + rechts.getWert()));
			
			treeToSet(links);
			treeToSet(rechts);
		}
	}
	
	/**
	 * Gibt die Hoehe des Knoten zurueck. 0, wenn Knoten null 1, wenn keine Kinder
	 * @param knoten
	 * @return hoehe, wenn null -> 0, ansonsten die Hoehe
	 */
	private int high(AVLKnoten knoten) {
		return knoten == null ? 0 : knoten.getHoehe();
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
			// links - rechts, weil wir uns im linken Teilbaum befinden
			if(high(knoten.getLinks()) - high(knoten.getRechts()) == 2) { // prueft ob Baum balanciert
				if(elem < knoten.getLinks().getWert()) {
					knoten = rechtsRotationUmLinkesKind(knoten);
				} else { // wenn das neue Element ein ZickZack (grafisch) verursacht hat -> Links-Rechts (bspw. 4, 2, 3)
					knoten = rechtsLinksRotation(knoten);
				}
			}
		} else if(elem >= knoten.getWert()) {
			knoten.setRechts(insert(knoten.getRechts(), elem));	// rekursives "Entlanghangeln"
			if(high(knoten.getRechts()) - high(knoten.getLinks()) == 2) {	// prueft ob Baum balanciert
				if(elem >= knoten.getRechts().getWert()) {
					knoten = linksRotationUmRechtesKind(knoten);
				} else { // wenn das neue Element ein ZickZack (grafisch) verursacht hat -> Rechts-Links (bspw. 4, 6, 5)
					knoten = linksRechtsRotation(knoten);
				}
			}
		} else {
			// nichts, weil Element bereits enthalten
		}
		knoten.setHoehe(Math.max(high(knoten.getLinks()), high(knoten.getRechts())) + 1);
		
		return knoten;
	}
	
	private AVLKnoten linksRechtsRotation(AVLKnoten knoten) {
		knoten.setRechts(rechtsRotationUmLinkesKind(knoten.getRechts()));
		
		return linksRotationUmRechtesKind(knoten);
	}
	
	private AVLKnoten linksRotationUmRechtesKind(AVLKnoten alteTeilbaumWurzel) {
		AVLKnoten neueTeilbaumWurzel = alteTeilbaumWurzel.getRechts();
		alteTeilbaumWurzel.setRechts(alteTeilbaumWurzel.getLinks());
		neueTeilbaumWurzel.setLinks(alteTeilbaumWurzel);
		alteTeilbaumWurzel.setHoehe(Math.max(high(alteTeilbaumWurzel.getLinks()), high(alteTeilbaumWurzel.getRechts())) + 1);
		// TODO die Hoehe wird in der aufrufenden Methode #insert() wieder ueberschrieben, warum hier also berechnen?
//		neueTeilbaumWurzel.setHoehe(Math.max(high(neueTeilbaumWurzel), high(neueTeilbaumWurzel.getRechts())) + 1);
		
		return neueTeilbaumWurzel;
	}
	
	private AVLKnoten rechtsLinksRotation(AVLKnoten knoten) {
		knoten.setLinks(linksRotationUmRechtesKind(knoten.getLinks()));
		
		return rechtsRotationUmLinkesKind(knoten);
	}
	
	private AVLKnoten rechtsRotationUmLinkesKind(AVLKnoten alteTeilbaumWurzel) {
		AVLKnoten neueTeilbaumWurzel = alteTeilbaumWurzel.getLinks();
		alteTeilbaumWurzel.setLinks(alteTeilbaumWurzel.getRechts());
		neueTeilbaumWurzel.setRechts(alteTeilbaumWurzel);
		alteTeilbaumWurzel.setHoehe(Math.max(high(alteTeilbaumWurzel.getLinks()), high(alteTeilbaumWurzel.getRechts())) + 1);
		// TODO die Hoehe wird in der aufrufenden Methode #insert() wieder ueberschrieben, warum hier also berechnen?
//		neueTeilbaumWurzel.setHoehe(Math.max(high(neueTeilbaumWurzel.getLinks()), high(neueTeilbaumWurzel)) + 1);
		
		return neueTeilbaumWurzel;
	}
}
