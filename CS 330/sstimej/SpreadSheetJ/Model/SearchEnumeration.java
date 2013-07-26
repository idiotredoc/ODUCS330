package SpreadSheetJ.Model;

import java.util.Enumeration;

abstract public interface SearchEnumeration
    extends Enumeration,  Cloneable 
{

    // The standard Enumeration operations
    boolean hasMoreElements();
    Object nextElement();
    
    // Extensions : look at the current object without moving
    // (useful when returning a position where something was found
    //  after a search operation).
    boolean hasElement();
    Object at(); 

    // Move backwards as well as forwards
    boolean hasPriorElements();
    Object priorElement();
}


