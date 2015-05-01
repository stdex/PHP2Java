package translator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Parser {

	private List<Token> tokens;
	private Iterator<Token> iterator;
	private Token token;

	private Map<String, Type> map = new HashMap<String, Type>();

	private List<String> codelist = new ArrayList<>();
	private List<String> functionlist = new ArrayList<>();
	
	private StringBuffer code;

	private boolean incdec = false;

	private String name;
	
	private int opened2 = 0;
	private boolean isFunc;
	private int  newVar = -1;
	private Type type;
	
	/**
	 * перечисление для типов переменных
	 */
	enum Type{
		INT(0, "int "), 
		STRING(2, "String "), 
		FLOAT(1, "float ");
		public final String value;
		public final int priority;
		
		private Type(int p, String s) {
			value = s;
			priority = p;
		}
	}

	/**
	 * главные метод для разбора строки
	 * @param tokens
	 * @throws ParseException
	 */
	public void expr(List<Token> tokens) throws ParseException {

		this.tokens = tokens;
		this.iterator = this.tokens.iterator();

		code = new StringBuffer(); // сгенерир. джава-код


		// разбор первого токена
		nextlexem();
		switch (token.name) {
		case ECHO:
		case PRINT:
			code.append("System.out.println(");
			print_expr();
			code.append(")");
			semicolon();
			break;
		case VAR:
			varExpression();
			semicolon();
			break;
		case WHILE:
			code.append("while ");
			unequationExpression(); // неравенство в скобках
			nextlexem();
			open2(); // открывающая {
			break;
		case FOR:
			code.append("for ");
			forExpression(); 
			nextlexem();
			open2();// открывающая {
			break;
		case IF:
			code.append("if ");
			unequationExpression();// неравенство в скобках
			nextlexem();
			open2();// открывающая {
			break;
		case ELSE:
			code.append("else ");
			nextlexem();
			if (token.name == TokenType.IF){
				code.append("if ");
				unequationExpression();
				nextlexem();
			}
			open2();// открывающая {
			break;
		case FUNAME:
			code.append(token.value);
			open();
			nextlexem();
			numvar();
			close();
			nextlexem();
			semicolon();
			break;
		case FUNCTION:
			isFunc = true;
			code = new StringBuffer(); 
			code.append("private static void ");
			nextlexem();
			if (token.name == TokenType.FUNAME){
				code.append(token.value);
				open();
				nextlexem();
				code.append("int ");
				var();
				while(isComma()){
					//TODO обработка параметров функции
				}
				close();
				nextlexem();
				open2();
			}
			break;
		case CLOSE2:
			code.append("\t}");
			opened2--;
			
			break;
		default:
			break;
		}
		if (isFunc){
			functionlist.add(code.toString());
			if (opened2 == 0) 
				isFunc = false;
		}else 
		codelist.add(code.toString());
	}


	private boolean isComma() {
		if (token.name == TokenType.COMMA)
			return true;
		return false;
	}


	// (int i=0; i<n; i++)
	private void forExpression() throws ParseException {
		open();
		nextlexem();
		var();
		assignExpression();
		semicolon();
		unequation();
		semicolon();
		nextlexem();
		varExpression();
		close();
	}


	private void unequationExpression() throws ParseException {
		open();
		unequation();
		close();
	}


	private void print_expr() throws ParseException {
		
		nextlexem();
		String val = token.value;
		switch (token.name) {
		case VAR:
			val = val.substring(1);
		case INT:
		case TEXT:
			code.append(val);
			nextlexem();
			while(token.name == TokenType.DOT){
				code.append(" + ");
				print_expr();
			}
			break;
		default:
			throw new ParseException("Can't print " + token);
		}
		
	}

   //либо a++, либо a = blabla;
	private void varExpression() throws ParseException {
		var();
		if (!incdec) {
			assignExpression();
		}
		incdec = false;
	}


    // = bla-bla;  
	// если в начале была новая переменная (newVar > -1) то надо вставить в код тип переменной
	//ДО ее объявления (для этого мы запомнили newVar = индекс первой буквы переменной) 
	private void assignExpression() throws ParseException {
		assign();
		expr_body();
		
		if (newVar>-1){
			code.insert(newVar, type.value);
			map.put(name, type);
			newVar = -1;
		}
	}

	//фигурная открывающая скобка {
	private void open2() throws ParseException {
		
		if(token.name == TokenType.OPEN2){
			code.append(token.value);
			opened2++;
		}else{
			throw new ParseException(" { expected: " + token);
		}
	}


	private void close() throws ParseException {
		if(token.name!= TokenType.CLOSE){
			throw new ParseException("\")\" expected: " + token);
		}
		code.append(token.value);
	}


	private void semicolon() throws ParseException {
		if (token.name != TokenType.SEMICOLON)
			throw new ParseException("\";\" expected" + token);
		code.append(";");
	}


	//неравенство
	private void unequation() throws ParseException {
		nextlexem();
		var();
		assign();
		expr_body();
		
	}

	private void open() throws ParseException {
		nextlexem();
		if (token.name != TokenType.OPEN){
			throw new ParseException("\"(\" expected: " + token);
		}
		code.append(token.value);
	}

	
	private void nextlexem() {
		if (iterator.hasNext()) {
			token = iterator.next();
			while (token.name == TokenType.WHITESPACE)
				nextlexem();
		} else
			token = null;
	}

	//выражение с операциями + - * / % ( ) 
	private void expr_body() throws ParseException {
		item();

		while (isOperatorPlus()) {
			code.append(token.value);
			item();
		}
		
	}

	//обрабатывает выражение-слагаемое
	private void item() throws ParseException {
		nextlexem();
		mult();
		while (isOperatorMult()) {
			code.append(token.value);
			item();
		}
	}
	
	/**
	 * обрабатывает множитель
	 * @throws ParseException
	 */
	private void mult() throws ParseException {
		if (token == null)
			throw new ParseException("unexpected end of a string");
		switch (token.name) {
		case TEXT:
			if (newVar>-1) 	type = Type.STRING;
			code.append(token.value);
			nextlexem();
			break;
		case INT:
		case FLOAT:
		case VAR:
			numvar();
			break;
		case OPEN:
			code.append(token.value);
			expr_body();
			if (token.name == TokenType.CLOSE) {
				code.append(token.value);
				nextlexem();
			} else
				throw new ParseException("closing bracket expected: " + token);
			break;
		case OPP:
			if (token.value.equals("-")){ //ОТРИЦ ЧИСЛА
				code.append("-");
				nextlexem();
				
				mult();
			} else 
				throw new ParseException("unexpected token: " + token);
			break;
		default:
			throw new ParseException("unexpected token: " + token);
		}
	}

	/**
	 * проверяет, что текущий токен *, / или %
	 * @return
	 * @throws ParseException
	 */
	private boolean isOperatorMult() throws ParseException {
		if (token != null) {
			if (token.name == TokenType.OPM) {
				return true;
			}
		}
		return false;
	}

	/**
	 * проверяет, что текущий токен + или -
	 * @return
	 * @throws ParseException
	 */
	private boolean isOperatorPlus() throws ParseException {
		if (token != null) {
			if (token.name == TokenType.OPP) {
				return true;
			} else if (token.name == TokenType.SEMICOLON) {
				return false;
			} else if (token.name == TokenType.DOT){
				return true;
			}
		}
		return false;
	}

	// int float variable
	private void numvar() throws ParseException {
		if (token == null)
			throw new ParseException("no token when should be int or var ");
		switch (token.name) {
		case VAR:
			if (!map.containsKey(token.value))
				throw new ParseException("No such variable in the map: " + token.value);
			if (newVar>-1) {
				if (type.priority < map.get(token.value).priority)
				type = map.get(token.value);
			}
			code.append(token.value.substring(1));
			nextlexem();
			if ((token.name == TokenType.INC)||(token.name == TokenType.DEC)) {
				code.append(token.value);
				nextlexem();
			} 
			break;
		case INT:
		case FLOAT:
			number();
			break;
		default:
			throw new ParseException("token is not a variable or an number: " + token);
		}
	}
	
	// int float
	private void number() {
		if (token.name == TokenType.INT) {
			if (newVar>-1) {
				if (type.priority < Type.INT.priority)
					type = Type.INT;
				}
		}else if (token.name == TokenType.FLOAT) {
			if (newVar>-1) {
				if (type.priority < Type.FLOAT.priority)
					type = Type.FLOAT;
			}
		}
		code.append(token.value);
		nextlexem();
	}

	private void var() throws ParseException {

		if (token == null)
			throw new ParseException("no lexem");
		if (token.name == TokenType.VAR) {
			name = token.value;
			if (!map.containsKey(name)) {
				newVar = code.length(); //запоминает индекс, куда вставляем тип
				type = Type.INT;
			}
			code.append(name.substring(1));
			nextlexem();
			if (token.name == TokenType.INC || token.name == TokenType.DEC ) {
				code.append(token.value);
				nextlexem();
				incdec= true;
			}
		} else {
			throw new ParseException("not a variable: " + token);
		}
	}

	// =, <, >
	private void assign() throws ParseException {
		if (token == null)
			throw new ParseException("no assign lexem");
		if (token.name == TokenType.ASSIGN) {
			code.append(" = ");
			return;
		} else if (token.name == TokenType.UNEQUAL){
			code.append(token.value);
			return;
		} else if (token.name == TokenType.EQUAL){
			code.append(token.value);
			return;
		}
		throw new ParseException("expected assign lexem:" + token);
	}

	public void printAllVariables() {
		for (Entry<String, Type> e : map.entrySet()) {
			System.out.println(e.getKey() + " = " + e.getValue());
		}
		System.out.println();
	}

	public Map<String, Type> getVarMap() {
		return map;
	}

	public List<String> getCodelist() {
		return codelist;
	}


	public List<String> getFunctionlist() {
		return functionlist;
	}

}
