package tests;
import static org.junit.Assert.*;
import general.NumGenerator;
import org.junit.Before;
import org.junit.Test;
import adt.implementations.AdtAVLBaumImpl;
import adt.interfaces.AdtAVLBaum;
import adt.interfaces.AdtArray;

public class AdtAVLBaumTests {
	
	private AdtAVLBaum avlbaum = null;
	
	@Before
	public void executedBeforeEach() {
		avlbaum = AdtAVLBaumImpl.create();
	}
	
	/**
	 * Lasttest mit 10000 Zahlen
	 */
	@Test
	public void test10000RandomNoDup(){
		String file = "1000RandomNoDup";
		NumGenerator.sortNum(file, 10000, false);
		AdtArray array = NumGenerator.readNum(file);
		for(int i = 0; i < array.lengthA(); i++) {
			avlbaum.insert(array.getA(i));
		}
		assertTrue(true);
	}
	
	/**
	 * Lasttest mit 10000 Zahlen
	 * Nach dem alle eingefuegt wird, werden alle wieder geloescht
	 */
	@Test
	public void test10000RandomNoDupInsertDelete(){
		String file = "10000RandomNoDupInsertDelete";
		NumGenerator.sortNum(file, 10000, false);
		AdtArray array = NumGenerator.readNum(file);
		for(int i = 0; i < array.lengthA(); i++) {
			avlbaum.insert(array.getA(i));
		}
		for(int i = 0; i < array.lengthA(); i++) {
			avlbaum.delete(array.getA(i));
		}
		assertTrue(avlbaum.isEmpty());
	}
	
	/**
	 * Prueft ob der AVL Baum "gedruckt" wurde
	 */
	@Test
	public void testPrintSuccess() {
		avlbaum.insert(4);
		avlbaum.insert(2);
		avlbaum.insert(5);
		assertTrue(avlbaum.print());
	}
	
	/**
	 * Entfernt die Wurzel -> Hoehe bleibt in diesem Fall gleich
	 */
	@Test
	public void testDeleteRoot() {
		avlbaum.insert(4);
		avlbaum.insert(2);
		avlbaum.insert(5);
		avlbaum.delete(4);
		assertEquals(2, avlbaum.high());
	}

	/**
	 * TODO
	 */
	@Test
	public void testDeleteDups() {
		avlbaum.insert(2);
		avlbaum.insert(1);
		avlbaum.insert(3);
		avlbaum.insert(1);
		avlbaum.insert(2);
		avlbaum.delete(2);
	}
	
}
