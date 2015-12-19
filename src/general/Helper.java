package general;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Helper {
	
	private List<AVLKnoten> alleKnoten;
	private Set<String> alleKnotenVerbindungen;
	private AVLKnoten wurzel;

	/* Hilfsmethode - Rekursionsaufruf um alle Knoten in ein Set zu stecken */
	private void treeToSet() {
		if(wurzel == null) {
			System.err.println("AVL Baum ist leer!");
		} else {
			treeToSet(wurzel);
		}
	}
	
	/* Rekursionsmethode */
	private void treeToSet(AVLKnoten knoten) {
		if(knoten != null) {
			alleKnoten.add(knoten);
			AVLKnoten links = knoten.getLinks();
			AVLKnoten rechts = knoten.getRechts();
			
			alleKnotenVerbindungen.add(knoten.getWert() + " [label = \"" + knoten.getWert() + "  (" + knoten.getHoehe() + ")" + "\"]");
			
			// Fuegt wenn Kind == null einen leeren String ein -> wird in GraphViz ignoriert
			alleKnotenVerbindungen.add(knoten.getWert() + (links == null ? "" : " -> " + links.getWert()));
			alleKnotenVerbindungen.add(knoten.getWert() + (rechts == null ? "" : " -> " + rechts.getWert()));
			// -> == digraph (directed)
			// -- == graph (undirected)
			
			treeToSet(links);
			treeToSet(rechts);
		}
	}
	
	/**
	 * Druckt den AVLBaum als *.dot und *.png aus
	 * @param wurzel, die Wurzel von dem AVLBaum
	 * @return boolean, ob erfolgreich gedruckt
	 */
	public boolean print(AVLKnoten wurzel) {
		this.wurzel = wurzel;
		alleKnoten = new ArrayList<>();
		alleKnotenVerbindungen = new HashSet<>();
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
			bw.write("legende [label = \"Syntax:\\n <val> (<high>)\", color = \"red\"]\n");
			for(String string : alleKnotenVerbindungen) {
				bw.write(string + "\n");
			}
			bw.write("}\n");
			System.err.println("dot-Datei wurde erstellt");
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
			return false;
		}
		System.err.println("png-Datei wurde erstellt");
		
		return true;
	}
}
