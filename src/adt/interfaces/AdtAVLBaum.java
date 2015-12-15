package adt.interfaces;

import java.util.Set;

public interface AdtAVLBaum {
	
	public static AdtAVLBaum create() {
		return null;
	}
	
	public boolean isEmpty();
	
	public int high();
	
	public void insert(int elem);
	
	public void delete(int elem);
	
	public boolean print();
	
	public Set<String> getSet();
}
