package adt.implementations;

import java.util.Set;
import adt.interfaces.AdtAVLBaum;

public class AdtAVLBaumImpl implements AdtAVLBaum {

	private AVLKnoten wurzel;
	
	private AdtAVLBaumImpl() {
		wurzel = null;
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void insert(int elem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(int elem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean print() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void treeToSet() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<String> getSet() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
