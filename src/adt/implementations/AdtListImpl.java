package adt.implementations;

import java.util.Arrays;

import adt.interfaces.AdtList;

/**
 * 
 * @author Marc & Constantin
 *
 */
public class AdtListImpl implements AdtList
{

    private int[] _array; 
    
    private int _laenge;
    
    /**
     * privater Konstruktor
     * Initialisiert das Array mit der Groesse 100 und die Laenge mit 0
     * 
     */
    private AdtListImpl()
    {
        _array = new int[100];
        _laenge = 0;
    }

    /**
     * Initialisiert das Objekt
     * 
     * @return AdtList
     */
    public static AdtList create()
    {
        return new AdtListImpl();
    }

    /**
     * Prueft ob die Laenge des aufrufenden Objektes 0 ist
     * 
     * @return boolean, ob Liste leer
     */
    @Override
    public boolean isEmpty()
    {
        return (_laenge == 0 ? true : false);
    }

    /**
     * Gibt die Anzahl der Elemente aus der Liste zurueck
     * 
     * @return int, laenge der Liste
     */
    @Override
    public int laenge()
    {
        return _laenge;
    }

    /**
     * Fuegt ein neues Element an einer bestimmten Position (0 < pos <= Laenge + 1) ein
     * Wenn die Position ungueltig ist, wird das Element nicht hinzugefuegt
     * 
     * @param int pos
     * @param int elem
     */
    @Override
    public void insert(int pos, int elem)
    {
        if (pos <= _laenge + 1)
        {
            // pruefen, ob max. Groesse von Array erreicht -> wenn ja
            // vergroessern und kopieren
            if (_laenge == _array.length - 1)
            {                
                int[] _tmpArray = new int[_array.length + 100];
                System.arraycopy(_array, 0, _tmpArray, 0, _array.length - 1);
                _array = _tmpArray;
            }
            if(_laenge != 0) {
                for(int i = _laenge; i >= pos; i--) {
                    _array[i] = _array[i - 1];
                } 
            }
            _array[pos - 1] = elem;
            _laenge++;
        }
    }
    
    /**
     * Entfernt das Element an einer bestimmten Postion
     * Wenn die Position ungueltig ist, wird das Element nicht entfernt
     * 
     * @param int pos
     */
    @Override
    public void delete(int pos)
    {
        if (pos <= _laenge && pos > 0)
        {
            for (int i = pos - 1; i < _laenge; i++)
            {
                _array[i] = _array[i + 1];
            }
            _laenge--;
        } else if(pos == 1 && _laenge == 1){
            _array[0] = 0;
            _laenge--;
        }
    }

    /**
     * Liefert die Position des ersten Vorkommens des Elements
     * Wenn das Element nicht enthalten ist, wird -1 als Fehler zurueckgegeben
     * 
     * @param int elem
     * @return int, Position des Elements
     */
    @Override
    public int find(int elem)
    {
        for(int i = 0; i <= _laenge; i++) 
        {
            if (_array[i] == elem)
            {
                return i + 1;
            }
        }
        return -1;
    }

    /**
     * Liefert das Element an der gegebenen Position (1..Laenge)
     * Wenn die Position ungueltig ist, wird -99999999 zurueckgegeben
     * 
     * @param int pos
     * @return int, das Element
     */
    @Override
    public int retrieve(int pos)
    {
        if(pos > _laenge || pos <= 0) {
            return -99999999;
        }
        return _array[pos - 1];
    }

    /**
     * Konkateniert die aufrufende Liste mit der 2. Parameter-Liste
     * 
     * @param AdtList list2
     * @return AdtList, die erste Liste
     */
    @Override
    public AdtList concat(AdtList list)
    {
        for(int i = 1; i <= list.laenge(); i++)
        {
            this.insert(_laenge + 1, list.retrieve(i));
        }
        return this;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(_array);
        result = prime * result + _laenge;
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AdtListImpl other = (AdtListImpl) obj;
        if (!Arrays.equals(_array, other._array))
            return false;
        if (_laenge != other._laenge)
            return false;
        return true;
    }
}
