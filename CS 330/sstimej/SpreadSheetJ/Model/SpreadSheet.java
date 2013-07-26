package SpreadSheetJ.Model;

import SpreadSheetJ.Model.Cell;
import SpreadSheetJ.Model.CellName;

public class SpreadSheet {
    // Stub for testing purposes

  public SpreadSheet()
  {
  }


  public Cell getCell (CellName name)
    // get an existing cell
  {
    return new Cell(this, name);
  }


public Value getValue(CellName value) {
	// Stub for testing purposes
	return new NumericValue((double)value.getRowNumber());
}


  
};
