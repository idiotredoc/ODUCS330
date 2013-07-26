package SpreadSheetJ.Model;


// Spreadsheet cells are addressed by column and row. The row indicators
// are pretty straightforward, consisting of integer values starting at 1.
// The column indicators are case-insensitive strings of alphabetic
// characters as follows: A, B, ..., Z, AA, AB, AC, ..., AZ, BA, BB, ... ZZ, 
// AAA, AAB, ... and so on.
//
// This class converts cell names to column and row indices and vice versa.
//
// When copying references to spreadsheet cells from one column to another
// and/or from one row to another, it is customary to alter the column 
// and row by the difference between the location copied from and 
// the location copied to. For example, if the cell B2 contains the formula
// 1+A1, and we copy B2 to C5, the new formula in C5 would be 1+B4.
// The alteration can be prevented by "fixing" the column and or row 
// indicator, which is done by placing a $ in front of the indicator.
// For example, if the cell B2 contains the formula
// 1+A1+$A1+A$1+$A$1, and we copy B2 to C5, the new formula in C5 would 
// be 1+B4+$A4+B$1+$A$1.

public
class CellName
{
    public CellName (String col, int row)
	//pre: column.size() > 0 && all characters in column are alphabetic
	//     row > 0
    {
	columnNumber = alphaToInt(col);
	rowNumber = row-1;
	theColIsFixed = false;
	theRowIsFixed = false;
    }


    public CellName (String col, int row,
		     boolean fixTheColumn,
		     boolean fixTheRow)
	//pre: column.size() > 0 && all characters in column are alphabetic
	//     row > 0
    {
	columnNumber = alphaToInt(col);
	rowNumber = row-1;
	theColIsFixed = fixTheColumn;
	theRowIsFixed = fixTheRow;
    }
    
    
    public CellName (String cellname)
	//pre: exists j, 0<=j<cellname.size()-1, 
	//        cellname.substr(0,j) is all alphabetic (except for a
	//             possible cellname[0]=='$')
	//        && cellname.substr(j) is all numeric (except for a
	//             possible cellname[j]=='$') with at least one non-zero
	//             digit
    {
	theRowIsFixed = false;
	theColIsFixed = false;
	int k = 0;
	while (k < cellname.length()
	       && (cellname.charAt(k) < '0'
		   || cellname.charAt(k) > '9'))
	    ++k;
	if (cellname.charAt(k-1) == '$') {
	    theRowIsFixed = true;
	    cellname = cellname.substring(0,k-1) + cellname.substring(k);
	    --k;
	}

	String theColumn;
	if (cellname.charAt(0) == '$') {
	    theColIsFixed = true;
	    theColumn = cellname.substring(1,k);
	}
	else
	    theColumn = cellname.substring(0, k);
	columnNumber = alphaToInt(theColumn);
	
	String rowChars = cellname.substring(k);
	int row = 0;
	try {
	    row = new Integer(rowChars).intValue();
	} catch (Exception e) {}
	rowNumber = row - 1;
    }


    public CellName (int columnNum,
		     int rowNum)
    {
	columnNumber = columnNum;
	rowNumber = rowNum;
	theColIsFixed = false;
	theRowIsFixed = false;
    }

    public CellName (int columnNum,
		     int rowNum,
		     boolean fixTheColumn,
		     boolean fixTheRow)
    {
	columnNumber = columnNum;
	rowNumber = rowNum;
	theColIsFixed = fixTheColumn;
	theRowIsFixed = fixTheRow;
    }
    
    
    // render the entire CellName as a string
    public String toString()
    {
	String s = new String();
	if (theColIsFixed)
	    s = "$";
	s = s + getColumn();
	if (theRowIsFixed)
	    s = s + '$';
	s = s + (rowNumber+1);
	return s;
    }
    
    
    
    // Get components in spreadsheet notation
    public String getColumn()
    {
	return intToAlpha(columnNumber);
    }
    
    
    public int getRow()
    {
	return rowNumber + 1;
    }
    
    
    public boolean isRowFixed()
    {
	return theRowIsFixed;
    }
    
    public boolean isColumnFixed()
    {
	return theColIsFixed;
    }
    
    
    // Get components as integer indices in range 0..
    public int getColumnNumber()
    {
	return columnNumber;
    }
    
    public int getRowNumber()
    {
	return rowNumber;
    }
    


    // Compute a new cell name at a given offset from this one
    public CellName offset(int nCols, int nRows)
    {
	int newCol = columnNumber + nCols;
	int newRow = rowNumber + nRows;

	if (newCol < 0)
	    newCol = 0;
	if (newRow < 0)
	    newRow = 0;

	return new CellName(newCol, newRow, theColIsFixed, theRowIsFixed);
    }
    

    public  boolean equals (Object cellname)
    {
      CellName r = (CellName)cellname;
      return (columnNumber == r.columnNumber &&
	      rowNumber == r.rowNumber &&
	      theColIsFixed == r.theColIsFixed &&
	      theRowIsFixed == r.theRowIsFixed);
    }
    

    public int hashCode()
    {
	return 1027*columnNumber + 3*rowNumber;
    }

private int columnNumber;
private int rowNumber;
private boolean theRowIsFixed;
private boolean theColIsFixed;

// Convert spreadsheet style indicators (e.g., BA) to integer indices.
// 
// It's tempting to assume the spreadsheet indicators are just a base-26
// number with A..Z as the digits, but it's not that simple. If we 
// assume that A=0, B=1, ...,Z=25, then AA would be 00 instead of 26.
// In fact, the leading 'digit' in a multi-digit indicator is base-27 with
// A=1, B=2, ..., Z=26, and all subsequent digits are base 26 with A=0.
private int alphaToInt (String columnIndicator)
    {
	int index = 0;
	for (int i = 0; i < columnIndicator.length(); ++i) {
	    char c = columnIndicator.charAt(i);
	    if (c >= 'a' && c <= 'z')
		c += ('A' - 'a');  // convert lowercase to uppercase
	    index = (26 * index + 1) + c - 'A';
	}
	return index-1;
    }


// Convert integer indices to spreadsheet style indicators (e.g., BA)
// 
// It's tempting to assume the spreadsheet indicators are just a base-26
// number with A..Z as the digits, but it's not that simple. If we 
// assume that A=0, B=1, ...,Z=25, then AA would be 00 instead of 26.
// In fact, the leading 'digit' in a multi-digit indicator is base-27 with
// A=1, B=2, ..., Z=26, and all subsequent digits are base 26 with A=0.
private String intToAlpha (int columnIndex) 
    {
	String column = new String();
	while (columnIndex >= 0) {
	    char c = (char)((columnIndex % 26) + 'A');
	    column = "" + c + column;
	    columnIndex = columnIndex / 26 - 1;
	}
	return column;
    }


}

