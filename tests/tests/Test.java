package tests;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	public static void main(String[] args) {
		Pattern pattern = Pattern.compile("\"(\\.|[^\"])*\"");
		Matcher matcher = pattern.matcher("\"ab 0&.)\"..\"jhj%^2 h\"");

		while (matcher.find()) {
			
		    String s = matcher.group();
			System.out.println(s);
		}
	}
}
