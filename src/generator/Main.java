package generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import translator.Executer;

public class Main {

	public static void main(String[] args) {
		//String inputname = "input2.php";
		//File input = new File(inputname);
		//запуск с параметром - имя входного файла
		if (args.length < 1)
			throw new IllegalArgumentException("No arguments error: need a filename");

		File input = new File(args[0]);
		if (!input.exists())
			throw new IllegalArgumentException("File '" + args[0] + "' doesn't exist");
		//отрезаем .php в имени
		int lastIndex = input.getName().lastIndexOf(".");
		String outputname = (lastIndex == -1)? input.getName(): input.getName().substring(0, lastIndex);
		System.out.println("Generating " + outputname + ".java");
		//  генератор java-класса
		JavaClassGenerator jdc = new JavaClassGenerator(outputname);
		try {
			// заполняет верхнюю часть класса (package, public class Name)
			jdc.generateStart();
			
			Executer e = new Executer();
			e.setFile(input);
			e.execute(); // вызов анализатора, в результате в нем будут 2 списка:
			//1й - код в main , 2й - список методов класса
			
			jdc.generateMainBody(e.getCodelist()); //заполняем мэйн
			jdc.generateMethods(e.getFunctionList()); //заполняет методы
			
			jdc.generateEnd(); //закрывающая скобочка класса
			
		} catch (FileNotFoundException e) {
			System.err.println("File "+input.getName()+" not found: \n" + input.getAbsolutePath());
		} catch (IOException e) {
			System.err.println("IO error while writing "+input.getName());
		}
		
		
	}
}
