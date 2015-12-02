package adt.interfaces;

public interface AdtArray
{
    public static AdtArray initA() {
        return null;
    }
    
    public void setA(int pos, int elem);
    
    public int getA(int pos);
    
    public int lengthA();
}
