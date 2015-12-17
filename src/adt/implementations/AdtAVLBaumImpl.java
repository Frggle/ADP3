package adt.implementations;

import general.AVLKnoten;
import general.Count;
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
	
	private int countRead;
	private int countWrite;
	private int rightRotCounter;
	private int leftRotCounter;
	
	private AdtAVLBaumImpl() {
		wurzel = null;
		alleKnotenVerbindungen = new HashSet<>();
		initCounter();
	}
	
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
		delete(wurzel, elem);
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
			
			bw.write("digraph avlbaum {\n");
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
		try {
			Runtime r = Runtime.getRuntime();
			String s = "";
			String outfileName = file.getName() + ".png";
			if(System.getProperty("os.name").equals("Mac OS X")) {
				s = "/usr/local/bin/dot " + file.getName() + " -Tpng -o " + outfileName;	
			} else {
				s = "dot " + file.getName() + " -Tpng -o" + outfileName;
			}
			Process p = r.exec(s);
			p.waitFor();
		} catch(IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return true;
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
		insertCounter(wurzel, elem);
		return new Count(countRead, countWrite, leftRotCounter, rightRotCounter);
	}

	@Override
	public Count deleteCount(int elem) {
		initCounter();
		deleteCounter(wurzel, elem);
		return new Count(countRead, countWrite, leftRotCounter, rightRotCounter);
	}

	private AVLKnoten delete(AVLKnoten knoten, int elem) {
		if(knoten == null) {
			return knoten;
		}
		int knotenWert = knoten.getWert();
		if(knotenWert > elem) {
			AVLKnoten linkerKnoten = knoten.getLinks();
			knoten.setLinks(delete(linkerKnoten, elem));
			
			if(high(knoten.getLinks()) - high(knoten.getRechts()) == 2) { // prueft ob Baum balanciert
                if(elem < knoten.getLinks().getWert()) {
                    knoten = rechtsRotationUmLinkesKind(knoten);
                } else { // wenn das neue Element ein ZickZack (grafisch) verursacht hat -> Links-Rechts (bspw. 4, 2, 3)
                    knoten = rechtsLinksRotation(knoten);
                }
            }
		} else if(knotenWert < elem) {
			AVLKnoten rechterKnoten = knoten.getRechts();
			knoten.setRechts(delete(rechterKnoten, elem));
			
			if(high(knoten.getRechts()) - high(knoten.getLinks()) == 2) {    // prueft ob Baum balanciert
                if(elem >= knoten.getRechts().getWert()) {
                    knoten = linksRotationUmRechtesKind(knoten);
                } else { // wenn das neue Element ein ZickZack (grafisch) verursacht hat -> Rechts-Links (bspw. 4, 6, 5)
                    knoten = linksRechtsRotation(knoten);
                }
            }
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
				AVLKnoten minKnoten = knoten.getRechts();	// minimalster Knoten im rechten Teilbaum (ausgehend vom zu loeschenden Knoten)
				while(minKnoten.getLinks() != null) {
					minKnoten = minKnoten.getLinks();
				}
				delete(knoten, minKnoten.getWert());	// loescht in dem Teilbaum den Knoten, dessen Wert kopiert wird
				knoten.setWert(minKnoten.getWert());	// kopiert den Wert vom geloeschten Knoten
			}
		}
		knoten.setHoehe(Math.max(high(knoten.getLinks()), high(knoten.getRechts())) + 1);
		
		return knoten;
	}
	
	private AVLKnoten deleteCounter(AVLKnoten knoten, int elem) {
        if(knoten == null) {
            return knoten;
        }
        countRead++;
        int knotenWert = knoten.getWert();
        if(knotenWert > elem) {
            countRead++;
            AVLKnoten linkerKnoten = knoten.getLinks();
            countWrite++;
            knoten.setLinks(deleteCounter(linkerKnoten, elem));
            
            countRead+=2;
            if(high(knoten.getLinks()) - high(knoten.getRechts()) == 2) { // prueft ob Baum balanciert
                countRead+=2;
                if(elem < knoten.getLinks().getWert()) {
                    knoten = rechtsRotationUmLinkesKindCounter(knoten);
                } else { // wenn das neue Element ein ZickZack (grafisch) verursacht hat -> Links-Rechts (bspw. 4, 2, 3)
                    knoten = rechtsLinksRotationCounter(knoten);
                }
            }
        } else if(knotenWert < elem) {
            countRead++;
            AVLKnoten rechterKnoten = knoten.getRechts();
            countWrite++;
            knoten.setRechts(deleteCounter(rechterKnoten, elem));
            
            countRead+=2;
            if(high(knoten.getRechts()) - high(knoten.getLinks()) == 2) {    // prueft ob Baum balanciert
                countRead+=2;
                if(elem >= knoten.getRechts().getWert()) {
                    knoten = linksRotationUmRechtesKindCounter(knoten);
                } else { // wenn das neue Element ein ZickZack (grafisch) verursacht hat -> Rechts-Links (bspw. 4, 6, 5)
                    knoten = linksRechtsRotationCounter(knoten);
                }
            }
        } else {    // zu loeschendes Element gefunden
            countRead+=5;
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
                AVLKnoten minKnoten = knoten.getRechts();   // minimalster Knoten im rechten Teilbaum (ausgehend vom zu loeschenden Knoten)
                countRead++;
                while(minKnoten.getLinks() != null) {
                    countRead++;
                    minKnoten = minKnoten.getLinks();
                    countRead++;
                }
                countRead++;
                deleteCounter(knoten, minKnoten.getWert());    // loescht in dem Teilbaum den Knoten, dessen Wert kopiert wird
                countRead++;
                countWrite++;
                knoten.setWert(minKnoten.getWert());    // kopiert den Wert vom geloeschten Knoten
            }
        }
        countRead+=2;
        countWrite++;
        knoten.setHoehe(Math.max(high(knoten.getLinks()), high(knoten.getRechts())) + 1);
        
        return knoten;
    }
	
	/**
	 * Gibt die Hoehe des Knoten zurueck. 0, wenn Knoten null 1, wenn keine Kinder
	 * @param knoten
	 * @return hoehe, wenn null -> 0, ansonsten die Hoehe
	 */
	private int high(AVLKnoten knoten) {
		return knoten == null ? 0 : knoten.getHoehe();
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
	
	private AVLKnoten insertCounter(AVLKnoten knoten, int elem) {
	    countRead+=2; // fuer die else Abfragen 
	    if(knoten == null) {
            knoten = new AVLKnoten(elem);
        } else if(elem < knoten.getWert()) {
            knoten.setLinks(insertCounter(knoten.getLinks(), elem)); // rekursives "Entlanghangeln"
            countRead+=3;
            countWrite++;
            // links - rechts, weil wir uns im linken Teilbaum befinden
            if(high(knoten.getLinks()) - high(knoten.getRechts()) == 2) { // prueft ob Baum balanciert
                countRead+=2;
                if(elem < knoten.getLinks().getWert()) {
                    knoten = rechtsRotationUmLinkesKindCounter(knoten);
                } else { // wenn das neue Element ein ZickZack (grafisch) verursacht hat -> Links-Rechts (bspw. 4, 2, 3)
                    knoten = rechtsLinksRotationCounter(knoten);
                }
            }
        } else if(elem >= knoten.getWert()) {
            knoten.setRechts(insertCounter(knoten.getRechts(), elem)); // rekursives "Entlanghangeln"
            countRead+=3;
            countWrite++;
            if(high(knoten.getRechts()) - high(knoten.getLinks()) == 2) {   // prueft ob Baum balanciert
                countRead+=2;
                if(elem >= knoten.getRechts().getWert()) {
                    knoten = linksRotationUmRechtesKindCounter(knoten);
                } else { // wenn das neue Element ein ZickZack (grafisch) verursacht hat -> Rechts-Links (bspw. 4, 6, 5)
                    knoten = linksRechtsRotationCounter(knoten);
                }
            }
        } else {
            // nichts, weil Element bereits enthalten
        }
	    countRead+=2;
	    countWrite++;
        knoten.setHoehe(Math.max(high(knoten.getLinks()), high(knoten.getRechts())) + 1);
        
        return knoten;
    }
	
	private AVLKnoten linksRechtsRotation(AVLKnoten knoten) {
		knoten.setRechts(rechtsRotationUmLinkesKind(knoten.getRechts()));
		
		return linksRotationUmRechtesKind(knoten);
	}
	
	private AVLKnoten linksRechtsRotationCounter(AVLKnoten knoten) {
        knoten.setRechts(rechtsRotationUmLinkesKindCounter(knoten.getRechts()));
        
        return linksRotationUmRechtesKindCounter(knoten);
    }
	
	private AVLKnoten linksRotationUmRechtesKind(AVLKnoten alteTeilbaumWurzel) {
		AVLKnoten neueTeilbaumWurzel = alteTeilbaumWurzel.getRechts();
		alteTeilbaumWurzel.setRechts(alteTeilbaumWurzel.getLinks());
		neueTeilbaumWurzel.setLinks(alteTeilbaumWurzel);
		alteTeilbaumWurzel.setHoehe(Math.max(high(alteTeilbaumWurzel.getLinks()), high(alteTeilbaumWurzel.getRechts())) + 1);
		neueTeilbaumWurzel.setHoehe(Math.max(high(neueTeilbaumWurzel), high(neueTeilbaumWurzel.getRechts())) + 1);
		
		return neueTeilbaumWurzel;
	}
	
	private AVLKnoten linksRotationUmRechtesKindCounter(AVLKnoten alteTeilbaumWurzel) {
        AVLKnoten neueTeilbaumWurzel = alteTeilbaumWurzel.getRechts();
        countRead++;
        alteTeilbaumWurzel.setRechts(alteTeilbaumWurzel.getLinks());
        countRead++;
        countWrite++;
        neueTeilbaumWurzel.setLinks(alteTeilbaumWurzel);
        countWrite++;
        alteTeilbaumWurzel.setHoehe(Math.max(high(alteTeilbaumWurzel.getLinks()), high(alteTeilbaumWurzel.getRechts())) + 1);
        countWrite++;
        countRead+=2;
        neueTeilbaumWurzel.setHoehe(Math.max(high(neueTeilbaumWurzel), high(neueTeilbaumWurzel.getRechts())) + 1);
        countRead++;
        countWrite++;
        
        leftRotCounter++;
        return neueTeilbaumWurzel;
    }
	
	private AVLKnoten rechtsLinksRotation(AVLKnoten knoten) {
		knoten.setLinks(linksRotationUmRechtesKind(knoten.getLinks()));
		
		return rechtsRotationUmLinkesKind(knoten);
	}
	
	private AVLKnoten rechtsLinksRotationCounter(AVLKnoten knoten) {
        knoten.setLinks(linksRotationUmRechtesKindCounter(knoten.getLinks()));
        
        return rechtsRotationUmLinkesKindCounter(knoten);
    }
	
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
        countRead+=2;
		neueTeilbaumWurzel.setHoehe(Math.max(high(neueTeilbaumWurzel.getLinks()), high(neueTeilbaumWurzel)) + 1);
		countRead++;
        countWrite++;
        
        rightRotCounter++;
		return neueTeilbaumWurzel;
	}
	
	private AVLKnoten rechtsRotationUmLinkesKind(AVLKnoten alteTeilbaumWurzel) {
        AVLKnoten neueTeilbaumWurzel = alteTeilbaumWurzel.getLinks();
        alteTeilbaumWurzel.setLinks(alteTeilbaumWurzel.getRechts());
        neueTeilbaumWurzel.setRechts(alteTeilbaumWurzel);
        alteTeilbaumWurzel.setHoehe(Math.max(high(alteTeilbaumWurzel.getLinks()), high(alteTeilbaumWurzel.getRechts())) + 1);
        neueTeilbaumWurzel.setHoehe(Math.max(high(neueTeilbaumWurzel.getLinks()), high(neueTeilbaumWurzel)) + 1);
        
        return neueTeilbaumWurzel;
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
			alleKnotenVerbindungen.add(knoten.getWert() + (links == null ? "" : " -> " + links.getWert()));
			alleKnotenVerbindungen.add(knoten.getWert() + (rechts == null ? "" : " -> " + rechts.getWert()));
			// -> == digraph (directed)
			// -- == graph (undirected)
			
			treeToSet(links);
			treeToSet(rechts);
		}
	}
}
