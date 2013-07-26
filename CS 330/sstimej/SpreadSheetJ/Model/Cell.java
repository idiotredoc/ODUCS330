package SpreadSheetJ.Model;

import SpreadSheetJ.Model.CellName;
import SpreadSheetJ.Model.SpreadSheet;
import SpreadSheetJ.Model.NumericValue;
import SpreadSheetJ.Model.Value;

import java.util.Observable;

// A single cell within a Spreadsheet
//
//  stubs for testing purposes
public class Cell {

    public Cell (SpreadSheet sheet, CellName name)
    {
	theName = name;
    }
    
    
    public CellName getName()
    {
	return new CellName(0,0);
    }
    
    public Expression getFormula()
    {
	return null;
    }
    
    public void putFormula(Expression e) {}
    
    public Value getValue()
    {
	return new NumericValue(theName.getRow());
    }
    
    public Value evaluateFormula()
    {
	return getValue();
    }
    
    public boolean getValueIsCurrent()  {return true;}
    public void putValueIsCurrent(boolean b) {}
    
    
    public void notify (Observable changedCell) {}
    
    private CellName theName;
}
