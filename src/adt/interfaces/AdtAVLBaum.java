package adt.interfaces;

public interface AdtAVLBaum {
	
	public static AdtAVLBaum create() {
		return null;
	}
	
	public boolean isEmpty();
	
	public int high();
	
	public AdtAVLBaum insert(int elem);
	
	public AdtAVLBaum delete(int elem);
	
	public void print();
}
