

import SpreadSheetJ.Model.ExprParser;
import SpreadSheetJ.Model.Expression;
import SpreadSheetJ.Model.SpreadSheet;

public
class TestExpressions  {

    /**
     * Very basic test driver - a command line argument is treated
     * as an expression, parsed, evaluated, and printed.
     */
    public static void main( String[] args ) throws Exception {
	
	String expression = args[0];

	SpreadSheet ssheet = new SpreadSheet();
	ExprParser parser = new ExprParser();
	Expression e = parser.doParse(expression);
	
	if (e == null) {
	    System.out.println("Syntax error reported");
	    return;
	}
	
	String rendered = e.evaluate(ssheet).render(0);
	System.out.println (expression + " evaluated to " + rendered);
    }
}

