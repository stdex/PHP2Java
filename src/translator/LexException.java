package translator;

public class LexException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7238374723833613987L;

	public LexException(String s) {
		super("Lexer error: " + s);
	}

}
