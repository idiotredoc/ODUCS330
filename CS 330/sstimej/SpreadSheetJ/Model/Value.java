package SpreadSheetJ.Model;


//
// Represents a value that might be obtained for some spreadsheet cell
// when its formula was evaluated.
// 
// Values may come in many forms. At the very least, we can expect that
// our spreadsheet will support numeric and string values, and will
// probably need an "error" or "invalid" value type as well. Later we may 
// want to add addiitonal value kinds, such as currency or dates.
//
public abstract
class Value implements Cloneable
{
    public abstract String valueKind();
    // Indicates what kind of value this is. For any two values, v1 and v2,
    // v1.valueKind() == v2.valueKind() if and only if they are of the
    // same kind (e.g., two numeric values). The actual character string
    // pointed to by valueKind() may be anything, but should be set to
    // something descriptive as an aid in identification and debugging.
    
    
    public abstract String render (int maxWidth);
    // Produce a string denoting this value such that the
    // string's length() <= maxWidth (assuming maxWidth > 0)
    // If maxWidth==0, then the output string may be arbitrarily long.
    // This function is intended to supply the text for display in the
    // cells of a spreadsheet.

    public String toString()
    {
	return render(0);
    }
    
    public boolean equals (Object value)
    {
	Value v = (Value)value;
	return (valueKind() == v.valueKind()) && isEqual(v);
    }
	

    abstract boolean isEqual (Value v);
    //pre: valueKind() == v.valueKind()
    //  Returns true iff this value is equal to v, using a comparison
    //  appropriate to the kind of value.

}

