package SpreadSheetJ.Model;

import java.util.Vector;

//
// A "Factory" is a common pattern in which a factory class is
//  used to create new instances of other classes. A factory
//  can help decouple an application from a large set of related
//  classes that it must build.
// 



public class ExpressionFactory
{

  public static Expression buildConstantExpression (String op, int i)
  {
    return new NumericConstantNode ((double)i);
  }


  public static Expression buildConstantExpression (String op, double d)
  {
    return new NumericConstantNode (d);
  }


  public static Expression buildConstantExpression (String op, String s)
  {
    if (op.equals("String"))
      return new StringLiteralNode (s);
    else if (op.equals("CellReference"))
      return new CellRefNode (new CellName(s));
    else if (op.equals("Time"))
        return new TimeConstantNode (s);
    else
      return null;
  }

  public static Expression buildUnaryExpression (String op, Expression opnd)
  {
    if (op.equals("-"))
      return new NegateNode(opnd);
    else
      return null;
  }


  
  public static Expression buildBinaryExpression (String op,
						  Expression left,
						  Expression right)
  {
    if (op.equals("<"))
      return new LessNode (left, right);
    else if (op.equals("<="))
      return new LessEqNode (left, right);
    else if (op.equals(">"))
      return new GreaterNode (left, right);
    else if (op.equals(">="))
      return new GreaterEqNode (left, right);
    else if (op.equals("=="))
      return new EqualNode (left, right);
    else if (op.equals("!="))
      return new NotEqualNode (left, right);
    else if (op.equals("+"))
      return new PlusNode (left, right);
    else if (op.equals("-"))
      return new SubtractNode (left, right);
    else if (op.equals("*"))
      return new TimesNode (left, right);
    else if (op.equals("/"))
      return new DividesNode (left, right);
    else
      return null;
  }
  
  
  public static Expression buildExpression (String op, Vector opnds)
  {
    if (op.equals("if") && opnds.size() == 3)
      return new IfNode((Expression)opnds.elementAt(0),
			(Expression)opnds.elementAt(1),
			(Expression)opnds.elementAt(2));
    else if (op.equals("abs") && opnds.size() == 1)
      return new AbsNode((Expression)opnds.elementAt(0));
    else if (op.equals("sqrt") && opnds.size() == 1)
      return new SqrtNode((Expression)opnds.elementAt(0));
    else if (op.equals("sum") && opnds.size() == 1)
      return new SumNode((Expression)opnds.elementAt(0));
    else
      return null;
  }

}
