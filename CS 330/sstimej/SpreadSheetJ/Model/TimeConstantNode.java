package SpreadSheetJ.Model;




public
class TimeConstantNode extends Expression
{
  private String value;
  
  public TimeConstantNode ()
  {
    value = "0:0";
  }

  public TimeConstantNode (String d)
  {
    value = d;
  }

  // How many operands does this expression node have?
  public int arity()                    {return 0;}

  // Get the k_th operand
  public Expression operand(int k)      {return null;}
    //pre: k < arity()


  // Evaluate this expression
  public Value evaluate(SpreadSheet s)  {return new TimeValue(value);}



  // Copy this expression (deep copy), altering any cell references
  // by the indicated offsets except where the row or column is "fixed"
  // by a preceding $. E.g., if e is  2*D4+C$2/$A$1, then
  // e.copy(1,2) is 2*E6+D$2/$A$1, e.copy(-1,4) is 2*C8+B$2/$A$1
  public Expression clone (int colOffset, int rowOffset)
  {
    return new TimeConstantNode(value);
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

  public String getOperator()    {return "" + value;}
  // Returns the name of the operator for printing purposes.
  // For constants, this is the string version of the constant value.


}
