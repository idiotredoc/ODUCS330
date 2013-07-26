package SpreadSheetJ.Model;




public abstract
class BinaryExpression extends Expression
{
  protected Expression left;
  protected Expression right;
  
  BinaryExpression (Expression leftOperand, Expression rightOperand)
  {
    left = leftOperand;
    right = rightOperand;
  }

  // How many operands does this expression node have?
  public int arity()                    {return 2;}

  // Get the k_th operand
  public Expression operand(int k)      {return (k == 0) ? left : right;}
    //pre: k < arity()


}
