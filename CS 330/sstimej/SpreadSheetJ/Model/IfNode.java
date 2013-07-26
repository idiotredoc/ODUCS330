package SpreadSheetJ.Model;




// Conditional expressions
public
class IfNode extends Expression
{
  private Expression condition;
  private Expression thenpart;
  private Expression elsepart;
  
  public IfNode (Expression conditionExp,
		 Expression thenExp,
		 Expression elseExp)
  {
    condition = conditionExp;
    thenpart = thenExp;
    elsepart = elseExp;
  }


  // How many operands does this expression node have?
  public int arity()              {return 3;}
  
  // Get the k_th operand
  public Expression operand(int k)
  //pre: k < arity()
  {
    return (k == 0) ? condition :
      ((k == 1) ? thenpart : elsepart);
  }

  // Evaluate this expression
  public Value evaluate(SpreadSheet s)
  {
    Value condValue = condition.evaluate(s);
    Value result;

    if (condValue.valueKind() == NumericValue.valueKindName()) {
      double dcond = ((NumericValue)condValue).getNumericValue();
      if (dcond > 1.0e-5)
	result = thenpart.evaluate(s);
      else
	result = elsepart.evaluate(s);
    }
    else
      result = new ErrorValue();

    return result;
  }



  // Copy this expression (deep copy), altering any cell references
  // by the indicated offsets except where the row or column is "fixed"
  // by a preceding $. E.g., if e is  2*D4+C$2/$A$1, then
  // e.copy(1,2) is 2*E6+D$2/$A$1, e.copy(-1,4) is 2*C8+B$2/$A$1
  public Expression clone (int colOffset, int rowOffset)
  {
  return new IfNode(condition.clone(colOffset, rowOffset),
		    thenpart.clone(colOffset, rowOffset),
		    elsepart.clone(colOffset, rowOffset));
  }


  // The following control how the expression gets printed by 
  // the default implementation of put(ostream&)

  public boolean isInline()      {return false;}
  // if false, print as functionName(comma-separated-list)
  // if true, print in inline form

  public int precedence()        {return 900;}
  // Parentheses are placed around an expression whenever its precedence
  // is lower than the precedence of an operator (expression) applied to it.
  // E.g., * has higher precedence than +, so we print 3*(a1+1) but not
  // (3*a1)+1

  public String getOperator()    {return "if";}
  // Returns the name of the operator for printing purposes.
  // For constants, this is the string version of the constant value.


}
