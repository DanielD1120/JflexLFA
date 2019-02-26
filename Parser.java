// Author: Vlad Nedelcu

import java.io.*;
import java.util.*;

public class Parser {
	
	public static String addNewline(String print) {
		Scanner scanner = new Scanner(print);
		String build = "";
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			build += "\t" + line + "\n";
		}
		scanner.close();
		return build;
	}

	public static void reduceStack(Stack<Expression> stack) {

		while (stack.size() != 1) {
			Expression e1 = stack.pop();
			Expression e2 = stack.pop();

			stack.push(new SequenceNode(e2,e1));
		}

		Expression e = stack.pop();
		stack.push(new MainNode(e));
	}
	
	public static void main (String[] args) throws IOException {
		HelloLexer l = new HelloLexer(new FileReader("input"));

		l.yylex();

		reduceStack(l.stack);
		Expression e = l.stack.pop();

		BufferedWriter writer = new BufferedWriter(new FileWriter("arbore"));
		writer.write(e.show());
		writer.close();

		//interpreting
		e.interpret();

	    writer = new BufferedWriter(new FileWriter("output"));

	    if (l.errors.size() != 0) {

	    	writer.write(l.errors.get(0) + "\n");
	    
	    } else if (HelloLexer.error) {

	    	writer.write(HelloLexer.errorString + "\n");
	    
	    } else {

			for (String name : HelloLexer.variables.keySet()) {
				writer.write(name + "=" + HelloLexer.variables.get(name) + "\n");
			}

		}

    	writer.close();

	}
}