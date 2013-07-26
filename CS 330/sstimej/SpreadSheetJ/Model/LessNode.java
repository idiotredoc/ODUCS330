package SpreadSheetJ.Model;




// Less than (<)
public
class LessNode extends BinaryExpression
{
  
  public LessNode (Expression leftOperand, Expression rightOperand)
  {
    super(leftOperand, rightOperand);
  }

  // Evaluate this expression
  public Value evaluate(SpreadSheet s)
  {
    Value leftValue = left.evaluate(s);
    Value rightValue = right.evaluate(s);
    Value result;

    if (leftValue.valueKind() == NumericValue.valueKindName()
	&& rightValue.valueKind() == NumericValue.valueKindName()) {
      double dleft = ((NumericValue)leftValue).getNumericValue();
      double dright = ((NumericValue)rightValue).getNumericValue();
      result = new NumericValue((dleft < dright) ? 1.0 : 0.0);
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
    return new LessNode(left.clone(colOffset, rowOffset),
			right.clone(colOffset, rowOffset));
  }

  
  // The following control how the expression gets printed by 
  // the default implementation of put(ostream&)

  public boolean isInline()      {return true;}
  // if false, print as functionName(comma-separated-list)
  // if true, print in inline form

  public int precedence()        {return 100;}
  // Parentheses are placed around an expression whenever its precedence
  // is lower than the precedence of an operator (expression) applied to it.
  // E.g., * has higher precedence than +, so we print 3*(a1+1) but not
  // (3*a1)+1

  public String getOperator()    {return "<";}
  // Returns the name of the operator for printing purposes.
  // For constants, this is the string version of the constant value.


}
