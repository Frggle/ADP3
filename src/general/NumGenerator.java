package general;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import adt.implementations.AdtArrayImpl;
import adt.interfaces.AdtArray;

/**
 * Der NumGenerator kann beliebig grosse Dateien erzeugen, welche mit zufaellig generierten Integer-Werten gefuellt sind.
 * Der NumGenerator kann diese Dateien einlesen und wandelt sie in ein ADTArray (optional kann das ADT direkt sortiert erstellt werden).
 */
public class NumGenerator {
	/**
	 * Erzeugt eine Datei mit der angegebenen Anzahl an zufaelligen Zahlen. Die Zahlen werden hintereinander weg
	 * geschrieben und durch ein Leerzeichen getrennt. Die Range der zufaelligen Zahlen ist 1..(quantity * 3)
	 * @param filename, Dateiname (Endung *.dat wird automatisch ergaenzt)
	 * @param quantity, die Anzahl der zufaellig generierten Zahlen
	 * @param yesToDuplicates, true = es duerfen Zahlen doppelt vorkommen; false = jede Zahl darf max. einmal vorkommen
	 */
	public static void sortNum(String filename, int quantity, Boolean yesToDuplicates) {
		
		File file = new File(filename + ".dat");
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
				

				ThreadLocalRandom random = ThreadLocalRandom.current();
				List<Integer> list = null;
				if(yesToDuplicates) {
					list = new ArrayList<Integer>();
					while(list.size() < quantity) {
						list.add(random.nextInt(1, quantity * 3));
					}
				} else {
					list = noDuplicates(quantity);
				}

				for(int i = 0; i < list.size() - 1; i++) {
					bw.write(String.valueOf(list.get(i)) + " ");
				}
				bw.write(String.valueOf(list.get(list.size() - 1)));	// nach der letzten Zahl folgt kein Space
				System.err.println("Die Datei " + filename + ".dat wurde erfolgreich erstellt");
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
	}
	
	/**
	 * Erzeugt eine Datei mit der angegebenen Anzahl in sortierter Reihenfolge. Die Zahlen werden hintereinander weg
	 * geschrieben und durch ein Leerzeichen getrennt. Die Range der zufaellig generierten Zahlen ist 1..(quantity * 3)
	 * @param filename, Dateiname (Endung *.dat wird automatisch ergaenzt)
	 * @param quantity, die Anzahl der zufaellig generierten Zahlen
	 * @param desc, true = absteigend sortiert; false = aufsteigend sortiert
	 * @param duplicates, true = es duerfen Zahlen doppelt vorkommen; false = jede Zahl darf max. einmal vorkommen
	 */
	public static void sortNum(String filename, int quantity, boolean desc, boolean yesToDuplicates) {
		
		File file = new File(filename + ".dat");
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
				
				ThreadLocalRandom random = ThreadLocalRandom.current();
				List<Integer> list = null;
				if(yesToDuplicates) {
					list = new ArrayList<Integer>();
					while(list.size() < quantity) {
						list.add(random.nextInt(1, quantity * 3));
					}
				} else {
					list = noDuplicates(quantity);
				}

				Collections.sort(list);
				
				if(desc) {
					Collections.reverse(list);
					for(int i = 0; i < list.size() - 1; i++) {
						bw.write(String.valueOf(list.get(i)) + " ");
					}
				} else {
					for(int i = 0; i < list.size() - 1; i++) {
						bw.write(String.valueOf(list.get(i)) + " ");
					}
				}
				bw.write(String.valueOf(list.get(list.size() - 1)));
				
				System.err.println("Die Datei " + filename + ".dat wurde erfolgreich erstellt");
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
	}
	
	/**
	 * Erzeugt eine Liste mit zufaelligen Zahlen, ohne Duplikate
	 * @param quantity, die Anzahl der Elemente
	 * @return List<Integer>, die Liste 
	 */
	private static List<Integer> noDuplicates(int quantity) {
		Set<Integer> resultSet = new HashSet<Integer>();
		List<Integer> resultList = new ArrayList<Integer>();
		
		ThreadLocalRandom random = ThreadLocalRandom.current();
		
		while(resultSet.size() < quantity) {
			resultSet.add(random.nextInt(1, quantity * 3));
		}
		resultList.addAll(resultSet);
		
		return resultList;
	}
	
	/**
	 * Liest die Datei ein und fuegt die Zahlen dem ADTArray Objekt hinzu. Die Datei muss mit WhiteSpaces geschrieben
	 * sein -> zwischen zwei Integer.
	 * @param filename, der Dateiname (die Endung *.dat wurde automatisch ergaenzt)
	 * @return AdtArray, ein ADTArray Objekt
	 */
	public static AdtArray readNum(String filename) {
		
		AdtArray array = AdtArrayImpl.initA();
		Scanner scanner = null;
		try {
			scanner = new Scanner(new FileReader(filename + ".dat"));
			while(scanner.hasNextInt()) {
				array.setA(array.lengthA(), scanner.nextInt());
			}
		} catch(FileNotFoundException e) {
			System.err.println("Die Datei " + filename + " wurde nicht gefunden!");
		} finally {
			scanner.close();
		}
		return array;
	}
	
	public static List<Integer> readNumAsList(String filename) {
        List<Integer> array = new ArrayList<>();       
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileReader(filename + ".dat"));
            while(scanner.hasNextInt()) {
                array.add(scanner.nextInt());
            }
        } catch(FileNotFoundException e) {
            System.err.println("Die Datei " + filename + " wurde nicht gefunden!");
        } finally {
            scanner.close();
        }
        return array;
    }
}
