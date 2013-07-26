package SpreadSheetJ.Model;




public
class CellRefNode extends Expression
{
  private CellName value;
  
  public CellRefNode ()
  {
    value = new CellName(0,0);
  }

  public CellRefNode (CellName cn)
  {
    value = cn;
  }

  // How many operands does this expression node have?
  public int arity()                    {return 0;}

  // Get the k_th operand
  public Expression operand(int k)      {return null;}
    //pre: k < arity()


  // Evaluate this expression
  public Value evaluate(SpreadSheet s)
  {
    Value v = s.getValue(value);
    if (v == null)
      return new ErrorValue();
    else
      return v;
  }



  // Copy this expression (deep copy), altering any cell references
  // by the indicated offsets except where the row or column is "fixed"
  // by a preceding $. E.g., if e is  2*D4+C$2/$A$1, then
  // e.copy(1,2) is 2*E6+D$2/$A$1, e.copy(-1,4) is 2*C8+B$2/$A$1
  public Expression clone (int colOffset, int rowOffset)
  {
    int col = value.getColumnNumber();
    int row = value.getRowNumber();

    boolean colFixed = value.isColumnFixed();
    boolean rowFixed = value.isRowFixed();

    if (!colFixed) {
      col += colOffset;
      if (colOffset < 0)
	colOffset = 0;
    }

    if (!rowFixed) {
      row += rowOffset;
      if (rowOffset < 0)
	rowOffset = 0;
    }
    return new CellRefNode(new CellName(col, row, colFixed, rowFixed));
  }


  // The following control how the expression gets printed by 
  // the default implementation of put(ostream&)

  public boolean isInline()      {return true;}
  // if false, print as functionName(comma-separated-list)
  // if true, print in inline form

  public int precedence()        {return 1000;}
  // Parentheses are placed around an expression whenever its precedence
  // is lower than the precedence of an operator (expression) applied to it.
  // E.g., * has higher precedence than +, so we print 3*(a1+1) but not
  // (3*a1)+1

  public String getOperator()    {return value.toString();}
  // Returns the name of the operator for printing purposes.
  // For constants, this is the string version of the constant value.

  public Sequence collectReferences()
  {
    Sequence refs = new Sequence();
    refs.addToBack (value);
    return refs;
  }


}
