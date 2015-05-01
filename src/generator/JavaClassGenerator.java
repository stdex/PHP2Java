package generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JavaClassGenerator {

	public static final String path =  "src\\generated\\";
	private final String className;
	private BufferedWriter bw;
	
	public JavaClassGenerator(String name) {
		className = name;
		File f = new File(path + className +".java");
		try {
				if (!f.exists())
					f.createNewFile();
				bw= new BufferedWriter(new FileWriter(f));
			} catch (IOException e) {
				System.err.println("Can't create file " + f.getName() + ":\n" + f.getAbsolutePath());
			}
	}
	
	public void generateStart() throws IOException{
		if (bw == null) return;
		bw.append("package generated;\n\n");
		bw.append("public class "+ className +" {\n\n");
	}
	public void openMainMethod() throws IOException{
		if (bw == null) return;
		bw.append("\n\tpublic static void main(String[] args) {\n");
	}
	
	public void openMethod(String name, String... args) throws IOException{
		if (bw == null) return;
		bw.append("\n\tprivate static void "+name+"("); 
		for (int i = 0; i < args.length-2; i=i+2) {
			bw.append(args[i]+" "+ args[i+1] + ", ");
		} 
		if (args.length > 0){
			bw.append(args[args.length-2]+" "+ args[args.length-1]);
		}
		bw.append("){\n");
	}
	
	public void closeMethod() throws IOException{
		bw.append("\t}\n");
	}
	
	public void generateEnd() throws IOException {
		if (bw == null) return;
		bw.append("}");
		bw.flush();
		bw.close();
	}
	
	public  void generateDefault(){
		try {
			generateStart();
			bw.append(defaultBody());
			generateEnd();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void generateMainBody(List<String> list) throws IOException {
		openMainMethod();
		if (bw == null) return;
		for (int i = 0; i < list.size(); i++) {
			bw.append("\t\t"+list.get(i)+"\n");
		}
		closeMethod();
	}



	private static String defaultBody() {
		StringBuffer sb = new StringBuffer();
		sb.append("\tpublic static void main(String[] args) {\n");
		sb.append("\t\tSystem.out.println(\"Hello WORLD!!!\");\n");
		sb.append("\t}\n");
		return sb.toString();
	}
	
	
	public static void main(String[] args) {
		JavaClassGenerator jcg = new JavaClassGenerator("HelloWorld");
		jcg.generateDefault();
	}

	public void generateMethods(List<String> list) throws IOException {
		if (bw == null) return;
		for (int i = 0; i < list.size(); i++) {
			bw.append("\t"+list.get(i)+"\n");
		}
	}
}
