package SpreadSheetJ.Model;

/*
import SpreadSheetJ.Model.Value;
*/


//
// Erroneous/invalid values in the spreadsheet.
//
public
class ErrorValue extends Value
{
    private static final String theValueKindName = new String("**error**");
    
    public ErrorValue()
    {
    }


    // The string used to identify numeric value kinds.
    public static String valueKindName()
    {
	return theValueKindName;
    }

    // Indicates what kind of value this is. For any two values, v1 and v2,
    // v1.valueKind() == v2.valueKind() if and only if they are of the
    // same kind (e.g., two numeric values). The actual character string
    // pointed to by valueKind() may be anything, but should be set to
    // something descriptive as an aid in identification and debugging.
    public String valueKind()
    {
	return theValueKindName;
    }


    // Produce a string denoting this value such that the
    // string's length() <= maxWidth (assuming maxWidth > 0)
    // If maxWidth==0, then the output string may be arbitrarily long.
    // This function is intended to supply the text for display in the
    // cells of a spreadsheet.
    public String render (int maxWidth)
    {
	String s = theValueKindName;
	if (maxWidth == 0 || maxWidth > s.length())
	    return s;
	else
	    return s.substring(0, maxWidth);
    }


    public Object clone()
    {
	return new ErrorValue();
    }


    boolean isEqual (Value v)
	//pre: valueKind() == v.valueKind()
	//  Returns true iff this value is equal to v, using a comparison
	//  appropriate to the kind of value.
    {
	return false;
    }

}

