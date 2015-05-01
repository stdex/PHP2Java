package translator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

	/**
	 * Лекс. разбор строки
	 * 	-	находит все вхождения токенов и сохраняет их в массив 
	 * 		по индексу равному индексу начала токена в строке
	 *  -	валидация токенов (отбрасывание ошибочных)
	 * @param str
	 * @return	List<Token> 
	 * @throws LexException
	 */
	public List<Token> read(String str) throws LexException {
		List<Token> tokens = new ArrayList<Token>();
		Map<Integer, Token> map = new TreeMap<Integer, Token>();
		Token tarr [] = new Token[str.length()];
		
		str = str.trim(); // отрезаем пробелы по краям строки
		if (str.isEmpty())
			return tokens;
		
		TokenType[] ttypes = TokenType.values(); // массив типов токенов
		
		Matcher matcher;
		
		for (int i = 0; i < ttypes.length; i++) {
			Pattern p = Pattern.compile(ttypes[i].pattern);
			matcher = p.matcher(str);
			while (matcher.find()) {
				//if(map.containsKey(matcher.start()))
				//если нашелся токен в строке
				// кладем его в карту <индекс первого символа, токен>
				map.put(matcher.start(), new Token(ttypes[i], matcher.group()));
				// и кладем его в массив tarr[индекс первого символа] = токен
				tarr[matcher.start()] = new Token(ttypes[i], matcher.group());
			}
		}
		validate(tarr, map); //валидация
		
		int pos = 0;
		
		for (Iterator<Entry<Integer, Token>> iterator = map.entrySet().iterator(); iterator.hasNext();) {
			Entry<Integer, Token> e = (Entry<Integer, Token>) iterator.next();
			if (pos < e.getKey()) {
				String s = str.substring(pos, e.getKey());
				throw new LexException(s + " is not a lexeme");
			}
			Token t = e.getValue();
			tokens.add(t);
			pos += t.value.length();
		}
		if (pos<str.length()){
			String s = str.substring(pos);
			throw new LexException(s+ " is not a lexeme");
		}
		return tokens;
	}

	/**
	 * здесь удаляются все лишние токены
	 * например, в строчке "String bla-bla"
	 * найдется по индексу " строка в кавычках "String bla-bla",
	 * а по индексу S и b имена функций String и bla-bla
	 * поэтому мы смотрим какая длина у строки  в кавычках,
	 * и если у нас что-то еще нашлось на протяжении этой строчки, 
	 * удаляем это. Так делаем для любых токенов
	 * 
	 * @param tarr
	 * @param map
	 */
	private void validate(Token[] tarr, Map<Integer, Token> map) {
		int pos = 0;
		int len;
		for (int i = 0; i < tarr.length; i++) {
			if(tarr[i] != null){
				if(pos > i){
					tarr[i]=null;
					map.remove(i);
				}else{
					len = tarr[i].value.length();
					pos += len;
				}
			}
		}
	}
}
