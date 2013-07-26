package SpreadSheetJ.Model;

// Represents a rectangular region in the spreadsheet.

public class CellRange {

    public CellRange (CellName corner1, CellName corner2)
    {
	int ulrow = corner1.getRowNumber();
	int lrrow = corner2.getRowNumber();
	if (ulrow > lrrow)
	    {
		int temp = ulrow;
		ulrow = lrrow;
		lrrow = temp;
	    }

	int ulcol = corner1.getColumnNumber();
	int lrcol = corner2.getColumnNumber();
	if (ulcol > lrcol)
	    {
		int temp = ulcol;
		ulcol = lrcol;
		lrcol = temp;
	    }

	upperLeft = new CellName(ulcol, ulrow);
	lowerRight = new CellName(lrcol, lrrow);
    }



    public CellName upperLeftCorner() 
    {
	return upperLeft;
    }


    public CellName lowerRightCorner() 
    {
	return lowerRight;
    }



    public boolean contains (CellName cn) 
    {
	return
	    cn.getColumnNumber() >= upperLeft.getColumnNumber()
	    && cn.getColumnNumber() <= lowerRight.getColumnNumber()
	    && cn.getRowNumber() >= upperLeft.getRowNumber()
	    && cn.getRowNumber() <= lowerRight.getRowNumber();
    }



    // Functions to aid in iterating over all cells in a range
    // e.g., for (CellName name = range.first(); range.more(name); 
    //            name = range.next(name)) { ... loop body ...}
    public CellName first() 
    {
	return upperLeft;
    }

    public boolean more(CellName cname) 
    {
	return contains(cname);
    }

    public CellName next(CellName cname) 
    {
	if (cname.getColumnNumber() >= lowerRight.getColumnNumber())
	    return new CellName(upperLeft.getColumnNumber(),
				cname.getRowNumber()+1);
	else
	    return new CellName(cname.getColumnNumber()+1,
				cname.getRowNumber());
    }


    public String toString() 
    {
	return upperLeft.toString() + ":" + lowerRight;
    }


    private CellName upperLeft;
    private CellName lowerRight;
}
