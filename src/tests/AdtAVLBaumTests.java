package tests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.List;
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
	 * Lasttest mit 100000 Zahlen
	 */
	@Test
	public void test100000RandomNoDup(){
		String file = "100000RandomNoDup2";
		NumGenerator.sortNum(file, 10000, false);
		AdtArray array = NumGenerator.readNum(file);
		for(int i = 0; i < array.lengthA(); i++) {
			avlbaum.insert(array.getA(i));
		}
		assertTrue(true);
	}
	
	/**
	 * Lasttest mit 1000 Zahlen
	 * Nach dem alle eingefuegt wird, werden alle wieder geloescht
	 */
	@Test
	public void test1000RandomNoDupInsertDelete(){
		String file = "1000RandomNoDupInsertDelete2";
		NumGenerator.sortNum(file, 1000, false);
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
	
		
	@Test
	public void test2() {
		String file = "bla2";
		NumGenerator.sortNum(file, 100, false);
		List<Integer> array = NumGenerator.readNumAsList(file);
		for(Integer integer : array) {
			avlbaum.insert(integer);
		}
		avlbaum.print();
	}
	
	@Test
	public void test() {
		for(int i = 1; i <= 80; i++) {
			avlbaum.insert(i);
		}
		for(int i = 1; i <= 40; i++) {
//			avlbaum.delete(i);
		}
		avlbaum.print();
	}
	
	/**
	 * Einfuegen von 1.000.000.000 Zahlen -> DAUERT!!
	 */
//	@Test
//	public void test10000000() {
//        long runtime = 0;
//        for(int i = 0; i < 100000000; i++) {
//            long l = avlbaum.insertRunTime(i);
//            runtime += l;
//            System.err.println((i / 100000000) * 100 + "%");
//        }
//        
//        System.err.println("Laufzeit (100.000.000): " + runtime + "ms");
//		assertTrue(true);
//	}
}
