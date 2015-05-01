package translator;

public class Token {

	final public TokenType name;
	final public String value;
	
	public Token(TokenType n, String v) {
		name = n;
		value = v;
	}
	
	@Override
	public String toString() {
		return "<" + name + ", "+ value + ">";
	}
	
}
