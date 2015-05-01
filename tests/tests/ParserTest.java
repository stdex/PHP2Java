package tests;

//import static translator.TokenType.ASSIGN;
//import static translator.TokenType.ECHO;
//import static translator.TokenType.INT;
//import static translator.TokenType.OPM;
//import static translator.TokenType.OPP;
//import static translator.TokenType.PRINT;
//import static translator.TokenType.SEMICOLON;
//import static translator.TokenType.TEXT;
//import static translator.TokenType.VAR;
//import static translator.TokenType.WHITESPACE;
import static translator.TokenType.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import translator.ParseException;
import translator.Parser;
import translator.Token;

@RunWith(Parameterized.class)
public class ParserTest extends Assert {

	private String input;

	private boolean res;

	private List<Token> tokenList;

	private String expected;

	private static Parser p = new Parser();
	private static int counter = -1;

	@Parameterized.Parameters
	public static List<Object[]> isEmptyData() {
		Token ass = new Token(ASSIGN, "=");
		Token ws = new Token(WHITESPACE, " ");
		Token echo = new Token(ECHO, "echo");
		Token print = new Token(PRINT, "print");
		Token sc = new Token(SEMICOLON, ";");
		Token plus = new Token(OPP, "+");
		Token minus = new Token(OPP, "-");
		Token mod = new Token(OPM, "%");
		Token varA = new Token(VAR, "$a");
		Token varF = new Token(VAR, "$f");
		Token varS = new Token(VAR, "$string_var");
		Token varS1 = new Token(VAR, "$s");
		Token int3 = new Token(INT, "3");
//		Token int_3 = new Token(INT, "3");
		Token int0 = new Token(INT, "0");
		Token float5 = new Token(FLOAT, "5.345");
		Token txt = new Token(TEXT, "\"Text1\"");
		Token txt2 = new Token(TEXT, "\"String 1\\n\"");
		Token inc= new Token(INC, "++");
		Token whilee = new Token(WHILE, "while");
		Token forr = new Token(FOR, "for");
		Token iff = new Token(IF, "if");
		Token els = new Token(ELSE, "else");
		Token open = new Token(OPEN, "(");
		Token close = new Token(CLOSE, ")");
		Token open2 = new Token(OPEN2, "{");
		Token uneq = new Token(UNEQUAL, "<");
		Token eq = new Token(EQUAL, "==");
		Token func = new Token(FUNAME, "f");
		Token function = new Token(FUNCTION, "function");
		Token dot = new Token(DOT, ".");
		return Arrays.asList(new Object[][] {

		{"$a=-3;", true, Arrays.asList(varA, ass, minus, int3, sc), "int a = -3;" }, 
		{ "$a=$a+3;", true, Arrays.asList(varA, ass, varA, plus, int3, sc), "a = a+3;" },
		{ "$a=$a-3;", true, Arrays.asList(varA, ass, varA, minus, int3, sc), "a = a-3;" },
		{ "$a=$a%3;", true, Arrays.asList(varA, ass, varA, mod, int3, sc), "a = a%3;" },
		{ "$a++;", true, Arrays.asList(varA, inc, sc), "a++;"},
		{ "$a=$a++;", true, Arrays.asList(varA, ass, varA, inc, sc), "a = a++;"},
		{ "echo $a;", true, Arrays.asList(echo, ws, varA, sc), "System.out.println(a);" }, 
		{ "print $a;", true, Arrays.asList(print, ws, varA, sc), "System.out.println(a);" },
		{ "echo \"Text1\";", true, Arrays.asList(echo, ws, txt, sc), "System.out.println(\"Text1\");" }, 
		{ "while ($a < 3) {", true, Arrays.asList(whilee , open, varA, uneq, int3, close, open2), "while (a<3){" }, 
		{ "for ($a = 0; $a < 3; a++) {", true, Arrays.asList(forr,open,varA,ass,int0,sc,varA, uneq, int3, sc, varA, inc, close, open2), 
			"for (a = 0;a<3;a++){" },
		{ "if ($a == 0) {", true, Arrays.asList(iff,open,varA,eq,int0,close, open2), 
			"if (a==0){" },
		{ "else if ($a == 3) {", true, Arrays.asList(els, iff,open,varA,eq,int3,close, open2), 
			"else if (a==3){" },	
		{ "else {", true, Arrays.asList(els, open2), 
			"else {" },
		{ "f(3);", true, Arrays.asList(func , open, int3, close, sc), "f(3);" }, 
		{"$s=$a+\"Text1\";",true, Arrays.asList(varS1, ass, varA, plus, txt, sc),
			"String s = a+\"Text1\";"},
		{"$f=5.345;", true,  Arrays.asList(varF, ass, float5, sc), 
				"float f = 5.345;"},
		{"echo \"String 1\\n\";", true, Arrays.asList(echo, ws, txt2, sc), 
				"System.out.println(\"String 1\\n\");"},
		{"$string_var = \"String 2\";", true, Arrays.asList(varS, ws, ass, ws, new Token(TEXT, "\"String 2\""), sc),
					"String string_var = \"String 2\";"},
		{"echo $string_var.\"\\n\";", true, Arrays.asList(echo, ws, varS, dot , new Token(TEXT, "\"\\n\""), sc),
					"System.out.println(string_var + \"\\n\");"},
					
					{ "function f($a) {", true, Arrays.asList(function, func, open, varA, close, open2), 
					"private static void f(int a){" }
	});
	}

	@Before
	public void prepare() {
	}

	public ParserTest(final String s, final boolean result, final List<Token> tl, final String expected) {
		
		input = s;
		res = result;
		tokenList = tl;
		this.expected = expected;
		counter++;
	}

	@Test
	public void testParser() throws ParseException {

		p.expr(tokenList);
		List<String> code = p.getCodelist();
		try{
			assertEquals(expected, code.get(counter));
		}catch(IndexOutOfBoundsException e){
			List<String> fff = p.getFunctionlist();
			assertEquals(expected, fff.get(0));
		}
	}
}
