package SpreadSheetJ.Model;

import java.util.Enumeration;


// Expressions can be thought of as trees.  Each non-leaf node of the tree
// contains an operator, and the children of that node are the subexpressions
// (operands) that the operator operates upon.  Constants, cell references,
// and the like form the leaves of the tree.
// 
// For example, the expression (a2 + 2) * c26 is equivalent to the tree:
// 
//                *
//               / \
//              +   c26
//             / \
//           a2   2

public abstract class Expression implements Cloneable
{
  // How many operands does this expression node have?
  public abstract int arity();
  
  // Get the k_th operand
  public abstract Expression operand(int k);
  //pre: k < arity()




  // Evaluate this expression
  public abstract Value evaluate(SpreadSheet usingSheet);



  // Copy this expression (deep copy), altering any cell references
  // by the indicated offsets except where the row or column is "fixed"
  // by a preceding $. E.g., if e is  2*D4+C$2/$A$1, then
  // e.copy(1,2) is 2*E6+D$2/$A$1, e.copy(-1,4) is 2*C8+B$2/$A$1
  public abstract Expression clone (int colOffset, int rowOffset);

  public Object clone ()
  {
    return clone(0,0);
  }



  public Sequence collectReferences()
  {
    Sequence refs = new Sequence();
    for (int i = 0; i < arity(); ++i)
      {
	Sequence refsi = operand(i).collectReferences();
	for (Enumeration p = refsi.front(); p.hasMoreElements(); ) {
	  CellName cn = (CellName)p.nextElement();
	  refs.addToBack (cn);
	}
      }
    return refs;
  }


  public static Expression get (String in)
  {
    return new ExprParser().doParse(in);
  }


  public String toString ()
  {
    String out = "";
    if (isInline() && arity() == 0) {
      out = getOperator();
    }
    else if (isInline() && arity() == 1) {
      out = getOperator();
      Expression opnd = operand(0);
      if (precedence() > opnd.precedence()) {
	out = out + '(' + opnd + ')';
      }
      else
	out = out + opnd;
    }
    else if (isInline() && arity() == 2) {
      Expression left = operand(0);
      Expression right = operand(1);

      if (precedence() > left.precedence()) {
	out = "(" + left + ')';
      }
      else
	out = left.toString();

      out = out + getOperator();

      if (precedence() > right.precedence()) {
	  out = out + '(' + right + ')';
      }
      else
	out = out + right;
    }
    else {
      // write in prefix function-call form
      out = getOperator() + '(';
      for (int k = 0; k < arity(); ++k)	{
	if (k > 0)
	  out = out + ", ";
	out = out + operand(k);
      }
      out = out + ')';
    }
    return out;
  }



  // The following control how the expression gets printed by 
  // the default implementation of put(ostream&)
  
  public abstract boolean isInline();
  // if false, print as functionName(comma-separated-list)
  // if true, print in inline form

  public abstract int precedence();
  // Parentheses are placed around an expression whenever its precedence
  // is lower than the precedence of an operator (expression) applied to it.
  // E.g., * has higher precedence than +, so we print 3*(a1+1) but not
  // (3*a1)+1

  public abstract String getOperator();
  // Returns the name of the operator for printing purposes.
  // For constants, this is the string version of the constant value.



}

