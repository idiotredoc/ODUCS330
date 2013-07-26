package SpreadSheetJ.Model;

import java.util.Vector;


public class ExprParser
{
  // The expression grammar:
  // <expr>    ::= <subexpr> [ <relop> <subexpr> ]
  // <subexpr> ::= <term> {<addop> <term>}  
  // <term> ::= <factor> {<multop> <factor>}
  // <factor> ::= - <item> 
  //           |  <item>
  // <item> ::= NumericConstant | StringConstant
  //         |  CellReference
  //         |  ( <expr> )
  //         |  <functionName> ( <paramList> )
  // <paramList> ::= <expr> {, <expr> }
  //
  // <relop>  ::= < | > | <= | >= | == | !=
  // <addop>  ::= + | -
  // <multop> ::= * | /
  // <functionName> ::= abs | if | sqrt | sum



  public ExprParser () {}

  public Expression doParse(String input)
  {
    Tokenizer scanner = new Tokenizer (input);
    scanner.setCharType ('$', Tokenizer.WordChars);
    scanner.setCharType ('\"', Tokenizer.Quoting);
    scanner.setCharType ('\'', Tokenizer.Quoting);
    scanner.setReserved("<=");
    scanner.setReserved(">=");
    scanner.setReserved("!=");
    scanner.setReserved("==");
    scanner.setReserved("if");
    scanner.setReserved("sqrt");
    scanner.setReserved("abs");
    scanner.setReserved("sum");
    scanner.setNumbersAreUnsigned(true);

    scanner.getNextToken();
    Expression e = expr(scanner);
    if (scanner.getTokenType() == Tokenizer.EndOfInput)
      return e;
    else
      return null;
  }

  /************ end of public area ************/


  private Expression expr (Tokenizer scanner)
  {
    // <expr>    ::= <subexpr> [ <relop> <subexpr> ]
    Expression left = subexpr(scanner);
    if (left == null)
      return null;
    
    String relationalOp = relop(scanner);
    if (relationalOp.equals(""))
      return left;
    
    Expression right = subexpr(scanner);
    if (right == null)
      return null;
    
    return ExpressionFactory.buildBinaryExpression(relationalOp, left, right);
  }
  
  
  Expression subexpr (Tokenizer scanner)
  {
    // <subexpr> ::= <term> {<addop> <term>}  
    Expression left = term(scanner);
    if (left == null)
      return null;
    
    String addOp = addop(scanner);
    while (!addOp.equals("")) {
      Expression right = term(scanner);
      if (right == null)
	return null;
      
      left = ExpressionFactory.buildBinaryExpression(addOp, left, right);
      addOp = addop(scanner);
    }
    return left;
  }
  

  Expression term (Tokenizer scanner)
  {
    // <term> ::= <factor> {<multop> <factor>}  
    Expression left = factor(scanner);
    if (left == null)
      return null;
    
    String multOp = multop(scanner);
    while (!multOp.equals("")) {
      Expression right = factor(scanner);
      if (right == null)
	return null;
      
      left = ExpressionFactory.buildBinaryExpression(multOp, left, right);
      multOp = multop(scanner);
    }
    return left;
  }
  



  Expression factor (Tokenizer scanner)
  {
  // <factor> ::= - <item> 
  //           |  <item>

  if (scanner.getTokenType() == Tokenizer.Operator
      && scanner.getLexeme().equals("-")) {
    scanner.getNextToken();
    Expression opnd = item(scanner);
    if (opnd == null)
      return null;
    
    return ExpressionFactory.buildUnaryExpression("-", opnd);
  }
  else
    return item(scanner);
  }



  Expression item (Tokenizer scanner)
  {
    // <item> ::= NumericConstant | StringConstant
    //         |  CellReference
    //         |  ( <expr> )
    //         |  <functionName> ( <paramList> )
    
    Expression theItem = null;
    if (scanner.getTokenType() == Tokenizer.IntegerConstant) {
      theItem = ExpressionFactory.buildConstantExpression
	("Numeric", (double)scanner.getIntValue());
      scanner.getNextToken();
    }
    else if (scanner.getTokenType() == Tokenizer.RealConstant) {
      theItem = ExpressionFactory.buildConstantExpression
	("Numeric", scanner.getRealValue());
      scanner.getNextToken();
    }
    else if (scanner.getTokenType() == Tokenizer.StringLiteral)
      {
	theItem = ExpressionFactory.buildConstantExpression
	  ("String", scanner.getLexeme());
	scanner.getNextToken();
      }
    else if (scanner.getTokenType() == Tokenizer.TimeConstant)
    {
	theItem = ExpressionFactory.buildConstantExpression
	  ("Time", scanner.getLexeme());
	scanner.getNextToken();
    }
  else if (scanner.getTokenType() == Tokenizer.Operator
	     && scanner.getLexeme().equals("("))
      {
	scanner.getNextToken();
	theItem = expr(scanner);
	if (scanner.getTokenType() == Tokenizer.Operator
	    && scanner.getLexeme().equals(")"))
	  scanner.getNextToken();
	else
	  return null;
      }
    else if (scanner.getTokenType() == Tokenizer.Reserved) {
      // Must be the start of a  function call.
      
      String theWord = scanner.getLexeme();

      String theLowerWord = "";
      for (int i = 0; i < theWord.length(); ++i) {
	  if (theWord.charAt(i) >= 'A' && theWord.charAt(i) <= 'Z')
	      theLowerWord += (char)(theWord.charAt(i) + ('a' - 'A'));
	  else
	      theLowerWord += theWord.charAt(i);
      }
      theWord = theLowerWord;
      scanner.getNextToken();
      if (scanner.getTokenType() != Tokenizer.Operator
	  || !scanner.getLexeme().equals("("))
	return null;
      scanner.getNextToken();

      Vector params = paramlist(scanner);
      if (params.size() == 0)
	return null;
      
      if (scanner.getTokenType() != Tokenizer.Operator
	  || !scanner.getLexeme().equals(")"))
	return null;
      scanner.getNextToken();
      
      theItem = ExpressionFactory.buildExpression
	(theWord, params);
      
    }
    else if (scanner.getTokenType() == Tokenizer.Word) {
      // Does this word look like a cell reference?
      String theWord = scanner.getLexeme();
      if (theWord.length() == 0)
	return null;
      int i = 0;
      if (theWord.charAt(i) == '$')
	++i;
      boolean lettersEncountered = false;
      while (i < theWord.length() &&
	     ((theWord.charAt(i) >= 'A' && theWord.charAt(i) <= 'Z')
	      || (theWord.charAt(i) >= 'a' && theWord.charAt(i) <= 'z'))) {
	++i;
	lettersEncountered = true;
      }
      if (i < theWord.length() && theWord.charAt(i) == '$')
	++i;
      boolean numbersEncountered = false;
      while (i < theWord.length()
	     && (theWord.charAt(i) >= '0' && theWord.charAt(i)
		 <= '9')) {
	++i;
	numbersEncountered = true;
      }
      if (lettersEncountered && numbersEncountered
	  && i == theWord.length()) {
	theItem = ExpressionFactory.buildConstantExpression
	  ("CellReference", scanner.getLexeme());
	scanner.getNextToken();
      }
    }
    return theItem;
  }  



  String relop (Tokenizer scanner)
  {
  // <relop>  ::= < | > | <= | >= | == | !=
    String theOp = scanner.getLexeme();
    if (scanner.getTokenType() == Tokenizer.Reserved &&
	(theOp.equals("<=") || theOp.equals(">=")
	 || theOp.equals("==") || theOp.equals("!="))) {
      scanner.getNextToken();
      return theOp;
    }
    else if (scanner.getTokenType() == Tokenizer.Operator &&
	     (theOp.equals("<") || theOp.equals(">")
	      || theOp.equals("="))) {
      scanner.getNextToken();
      return theOp;
    }
    else
      return "";
  }

  String addop (Tokenizer scanner)
  {
    // <addop>  ::= + | -
    String theOp = scanner.getLexeme();
    if (scanner.getTokenType() == Tokenizer.Operator &&
	(theOp.equals("+") || theOp.equals("-"))) {
      scanner.getNextToken();
      return theOp;
    }
    else
      return "";
  }


  String multop (Tokenizer scanner)
  {
    // <multop> ::= * | /
    String theOp = scanner.getLexeme();
    if (scanner.getTokenType() == Tokenizer.Operator &&
	(theOp.equals("*") || theOp.equals("/"))) {
      scanner.getNextToken();
      return theOp;
    }
    else
      return "";
  }


  Vector paramlist (Tokenizer scanner)
  {
    Vector params = new Vector();

    Expression param = expr(scanner);
    if (param == null)
      return new Vector();

    params.add(param);
    while (scanner.getTokenType() == Tokenizer.Operator
	   && scanner.getLexeme().equals(",")) {
      scanner.getNextToken();
      
      param = expr(scanner);
      if (param == null)
	return new Vector();
      params.add(param);
    }

    return params;
  }





}


