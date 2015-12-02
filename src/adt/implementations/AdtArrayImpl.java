package adt.implementations;

import java.util.ArrayList;
import java.util.List;

import adt.implementations.AdtListImpl;
import adt.interfaces.AdtArray;
import adt.interfaces.AdtList;

public class AdtArrayImpl implements AdtArray
{
    private AdtList _array;
    
    /**
     * privater Konstruktor
     */
    private AdtArrayImpl()
    {
        _array = AdtListImpl.create();
    }
    
    /**
     * Initialisiert das Objekt
     * 
     * @return AdtArray
     */
    public static AdtArray initA() {
        return new AdtArrayImpl();
    }
    
    /**
     * Fuegt an dem aufrufenden Array ein Element an einer bestimmten Position hinzu
     * Wenn die Position kleiner als 0 ist, wird das Element nicht hinzugefuegt
     * Wenn die Position groesser als die aktuelle Laenge ist, 
     *                 wird das Array erweitert und die neuen Felder mit 0 beschrieben
     * 
     * @param int pos
     * @param int elem
     */
    @Override
    public void setA(int pos, int elem)
    {
        if(pos >= 0) {
            while(_array.laenge() <= (pos - 1)) {
                _array.insert(_array.laenge() + 1, 0);
            }
//            _array.delete(pos + 1);
            _array.insert(pos + 1, elem);
            _array.delete(pos + 1);
        }
    }

    /**
     * Gibt das Element an der Position zurueck
     * Wenn die Position ungueltig ist (kleiner als 0 oder groesser als die Laenge) wird 0 zurueckgegeben
     * 
     * @param int pos
     * @return int, das Element
     */
    @Override
    public int getA(int pos)
    {
        if(pos >= 0 && pos <= _array.laenge()) {
            return _array.retrieve(pos + 1); 
        }
        return 0;
    }

    /**
     * Gibt die Laenge des Arrays zurueck
     * 
     * @return int, die Laenge
     */
    @Override
    public int lengthA()
    {
       return _array.laenge();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_array == null) ? 0 : _array.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof AdtArrayImpl))
            return false;
        AdtArrayImpl other = (AdtArrayImpl) obj;
        
        if(other.lengthA() != this.lengthA()) {
            return false;
        }
        List<Integer> otherAsList = new ArrayList<Integer>();
        List<Integer> thisAsList = new ArrayList<Integer>();
        for(int i = 0; i < other.lengthA(); i++) {
            otherAsList.add(other.getA(i));
        }
        for(int i = 0; i < this.lengthA(); i++) {
            thisAsList.add(this.getA(i));
        }
        if(!otherAsList.equals(thisAsList)) {
            return false;
        }
        
        if (_array == null) {
            if (other._array != null)
                return false;
        } else if (!_array.equals(other._array))
            return false;
        return true;
    }
}
