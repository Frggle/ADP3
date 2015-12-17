package adt.interfaces;

import general.Count;

public interface AdtAVLBaum {
	
	public static AdtAVLBaum create() {
		return null;
	}
	
	public boolean isEmpty();
	
	public int high();
	
	public void insert(int elem);
	
	public long insertRunTime(int elem);
	
	public Count insertCount(int elem);
	
	public void delete(int elem);
	
	public Count deleteCount(int elem);
	
	public boolean print();
}
