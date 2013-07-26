

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import SpreadSheetJ.Model.ExprParser;
import SpreadSheetJ.Model.Expression;
import SpreadSheetJ.Model.SpreadSheet;

public
class TestExpressionEval extends TestCase {

	public TestExpressionEval(String testName) {
		super(testName);
	}

	public static void main( String[] args ) throws Exception {
		TestRunner.run( suite() );
	}

	public static Test suite() {
		return new TestSuite(TestExpressionEval.class);
	}


	private static String eval (String expression)
	{
		SpreadSheet ssheet = new SpreadSheet();
		ExprParser parser = new ExprParser();
		Expression e = parser.doParse(expression);

		if (e == null) {
			return "Syntax error reported";
		}

		String rendered = e.evaluate(ssheet).render(0);
		return rendered;
	}




	public static void testNum1(){
		assertEquals("1", eval("1"));
	}

	public static void testCell1(){
		assertEquals("0", eval("A1"));
	}

	public static void testString1(){
		assertEquals("hello", eval("\"hello\""));
	}

	public static void testNegate()
	{
		assertEquals("-1", eval("-A2"));
	}

	public static void testExpr()
	{
		assertEquals("5", eval("A2 + 2 * B3"));
	}

	public static void testConcat()
	{
		assertEquals("hello world", eval("\"hello\" + \" \" + \"world\""));
	}

	public static void testTime1()
	{
		assertEquals("4:12:30:30", eval( "4:12:30:30" ));
	}
	public static void testb()
	{
		assertEquals("****", new TimeValue(0,1,2,12).render(4));
	}
	public static void testbb()
	{
		assertEquals("*******", new TimeValue(0,1,2,12,3,2,4,5).render(7));
	}
	
}

