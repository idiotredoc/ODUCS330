package SpreadSheetJ.Model;




public abstract
class UnaryExpression extends Expression
{
  protected Expression opnd;
  
  UnaryExpression (Expression operand)
  {
    opnd = operand;
  }

  // How many operands does this expression node have?
  public int arity()                    {return 1;}

  // Get the k_th operand
  public Expression operand(int k)      {return opnd;}
    //pre: k < arity()


}
