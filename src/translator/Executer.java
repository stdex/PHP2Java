package translator;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import translator.Parser.Type;

public class Executer {

	private BufferedReader br;
	private List<List<Token>> listOfLexems = new ArrayList<>();

	private List<String> strings = new ArrayList<>();
	private Map<String, Type> varMap;
	private List<String> codelist;
	private List<String> functionlist;

	public static void main(String[] args) {

		if (args.length < 1)
			throw new IllegalArgumentException("No arguments error: need a filename");

		File f = new File(args[0]);
		if (!f.exists())
			throw new IllegalArgumentException("File '" + args[0] + "' doesn't exist");

		try {
			Executer e = new Executer();
			e.br = new BufferedReader(new FileReader(f));
			e.execute();
		} catch (FileNotFoundException e1) {
			System.err.println("File not found");
		}
	}

	public void execute() {

		try {
			lex(); // запуск лексического анализа
			parse(); // запуск синтаксического анализа
		} catch (ParseException e) {
			System.err.println(e.getMessage());
		} catch (LexException e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Для каждого списка токенов (по строчкам) запускает синтакс. анализатор
	 * 
	 * После берет из анализатора:
	 * 	- карту всех переменных
	 * 	- код метода мэйн
	 * 	- список методов генерируемого класса
	 * @throws ParseException
	 */
	public void parse() throws ParseException {
		Parser p = new Parser();
		for (List<Token> li : listOfLexems) {
			if (!li.isEmpty()){
				p.expr(li);
			}
		}
		varMap = p.getVarMap();
		codelist = p.getCodelist();
		functionlist = p.getFunctionlist();
	}

	/**
	 * Лекс. анализ по строкам, для каждой строки возвр. список токенов и 
	 * добавляет его в общий список
	 * @throws LexException
	 */
	public void lex() throws LexException { 
		Lexer l = new Lexer();
		try {
			String str;
			while ((str = br.readLine()) != null) {
				try {
					List<Token> tokens = l.read(str);
					strings.add(str);
					listOfLexems.add(tokens);
				} catch (LexException e) {
					throw new LexException(e.getMessage() + "\nin " + str);
				}
			}
			br.close();
		} catch (IOException e) {
			System.err.println("Can't read input stream");
		}
	}

	public void setText(String str) {
		clearAll();
		InputStream is = new ByteArrayInputStream(str.getBytes());
		br = new BufferedReader(new InputStreamReader(is));
	}

	public void clearAll() {
		listOfLexems.clear();

//		functionlist.clear();
//		codelist.clear();
		strings.clear();
	}

	public List<List<Token>> getLexems() {
		return listOfLexems;
	}

	public String getString(int i) {
		return strings.get(i);
	}

	public void setFile(File input) throws FileNotFoundException {
		clearAll();
		br = new BufferedReader(new FileReader(input));
		
	}
	
	public Map<String, Type> getVarMap(){
		return varMap;
	}
	
	public List<String> getCodelist() {
		return codelist;
	}

	public List<String> getFunctionList() {
		return functionlist;
	}

}
