package tests;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import translator.LexException;
import translator.Lexer;
import translator.Token;
import translator.TokenType;
import static translator.TokenType.*;

@RunWith(Parameterized.class)
public class LexerTest  extends Assert {
	

	private Lexer l;
	
	private String input;

	private boolean res;

	private List<TokenType> tokenList;
	
	@Parameterized.Parameters
	  public static List<Object[]> isEmptyData() {
	    return Arrays.asList(new Object[][] {
	      { "$aa=3;", true, Arrays.asList(VAR, ASSIGN, INT, SEMICOLON) },
	      { "$a=$a+3;", true, Arrays.asList(VAR, ASSIGN, VAR, OPP, INT, SEMICOLON) },
	      { "$a=$a-3;", true, Arrays.asList(VAR, ASSIGN, VAR, OPP, INT, SEMICOLON) },
	      { "$a=$a%3;", true, Arrays.asList(VAR, ASSIGN, VAR, OPM, INT, SEMICOLON) },
	      { "echo $a;", true, Arrays.asList(ECHO, WHITESPACE, VAR, SEMICOLON) },
	      { "print $a;", true, Arrays.asList(PRINT, WHITESPACE, VAR, SEMICOLON) },
	      { "echo \"Text1\";", true, Arrays.asList(ECHO, WHITESPACE, TEXT, SEMICOLON) },
	      { "f(2);", true, Arrays.asList(FUNAME, OPEN, INT, CLOSE, SEMICOLON)},
	      { "for($a = 0; $a < 5; $a++) {", true, 
	    	  Arrays.asList(FOR, OPEN, VAR, WHITESPACE, ASSIGN, WHITESPACE, INT, SEMICOLON,
	    			  WHITESPACE, VAR, WHITESPACE, UNEQUAL, WHITESPACE, INT, SEMICOLON,
	    			  WHITESPACE, VAR, INC, CLOSE, WHITESPACE, OPEN2)},
	    			  
	      { "}", true, Arrays.asList(CLOSE2)},
	      {"while($a < 5) {", true, 
	    	  Arrays.asList(WHILE, OPEN, VAR, WHITESPACE, UNEQUAL, WHITESPACE, INT,
	    			  CLOSE, WHITESPACE, OPEN2) },
	      {"$a++", true, Arrays.asList(VAR, INC)},
	      {"$k=5.123", true, Arrays.asList(VAR, ASSIGN, FLOAT)},
	      {"echo $string_var.\"\\n\";", true, 
	    	  Arrays.asList(ECHO, WHITESPACE, VAR, DOT, TEXT,SEMICOLON)},
	      {"echo \"String 1\".\" \".$string_var;", true, 
	    		  Arrays.asList(ECHO, WHITESPACE, TEXT, DOT, TEXT, DOT, VAR, SEMICOLON)}
	      
	    });
	  }
	
	@Before
	public void prepare(){
	}
	
	public LexerTest(final String s, final boolean result, final List<TokenType> tl) {
		l = new Lexer();
		input = s;
		res = result;
		tokenList = tl;
	}
	
	@Test
	public void testLexer() throws LexException{
		
		List<Token> tokens = l.read(input);
		
		for (int i = 0; i < tokens.size(); i++) {
			assertEquals(tokenList.get(i), tokens.get(i).name);
		}
	}
	
}
