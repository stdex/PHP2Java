package translator;

public class ParseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2161947544417639476L;

	public ParseException(String string) {
		super("Parse error:" + string);
	}

}
