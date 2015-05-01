# Translator PHP to Java

## Nonterminals:
START -> S | SFUNC  
S -> ECHOPRINT PRINT_EXPR );  
S -> VAR_EXPR ;  
S -> while ( UNEQ ) { S }  
S -> for FOR_EXPR { S }  
S -> INPHP  
SFUNC -> FUNC FTYPE FUNAME ( PARAMETERS ) { S RETURN}  
FUNC -> private static  
FTYPE -> void | TYPE  
PARAMETERS -> e | PARAM  
PARAM -> TYPE VAR | TYPE VAR , PARAM  
RETURN -> e | return NUMVAR ;  
S -> S_IF | S_ELSE  
S_IF -> if ( UNEQ ) { S }  
S_ELSE -> else { S } | else S_IF  
INPHP -> e  
ECHOPRINT -> System.out.println(  
PRINT_EXPR -> TEXT | VAR | NUM  
VAR_EXPR -> VAR0 ASSIGN_EXPR | VAR INCDEC  
VAR0 -> VAR | TYPE VAR  
TYPE -> int | float | string  
ASSIGN_EXPR -> ASSIGN EXPR_BODY  
INCDEC -> ++ | --  
ASSIGN -> = | < | >  
UNEQ ->  VAR ASSIGN EXPR_BODY  
FOR_EXPR -> ( VAR0 ASSIGN_EXPR ; UNEQ ; VAR_EXPR )  
EXPR_BODY ->  ITEM | ITEM ADDICT_EXPR  
ADDICT_EXPR -> OPP ITEM | OPP ITEM ADDICT_EXPR  
OPP -> + | -  
ITEM -> MULT | MULT MULT_EXPR  
MULT_EXPR -> OPM MULT | OPM MULT MULT_EXPR  
OPM -> * | / | %  
MULT -> TEXT | NUMVAR | ( EXPR_BODY)  
NUMVAR -> VAR | NUMBER  
NUMBER -> INT | FLOAT  

## Tokens:
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
INPHP("<\\?"),  
FUNCTION("function"),  
ECHO("echo"),  
PRINT("print");  
