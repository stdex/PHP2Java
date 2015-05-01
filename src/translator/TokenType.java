package translator;

public enum TokenType {
        // Token types cannot have underscores
		WHITESPACE("\\s"),
        VAR("[$]{1}[A-Za-z]{1}[A-Za-z_0-9]*"), 
        ASSIGN("="),
        OPEN("\\("),
        CLOSE("\\)"),
        SEMICOLON(";"),
        COMMA(","),
        OPP("[+-]"),
        OPM("[*/%]"),
        
        INT("0|[1-9]{1}[0-9]*"),
        FLOAT("\\d+\\.\\d+"),
        TEXT("\"(\\.|[^\"])*\""),
        FUNAME("[a-zA-Z]{1}\\w*"), 
        
        FOR("for"),
        WHILE("while"),
        IF("if"),
        ELSE("else"),
        
        OPEN2("\\{"),
        CLOSE2("\\}"),
        
        DOT("\\."),
        UNEQUAL("[<>]"),
        
        EQUAL("[=]{2}"),
        INC("[+]{2}"),
        DEC("[-]{2}"),
        
        FUNCTION("function"),
        ECHO("echo"),
        PRINT("print");

        public final String pattern;

        private TokenType(String pattern) {
            this.pattern = pattern;
        }
}
