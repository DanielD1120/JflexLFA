import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.HashMap;

abstract interface Expression {
	String show();
	Object getValue();
	Expression interpret();
};

class Variable implements Expression {
	String value;

	public Variable(String name) {
		this.value = name;
	}

	public String show() {
		return "<VariableNode> " + value + "\n"; 
	}

	public Object getValue() {
		return value;
	}

	public Expression interpret() {
		return this;
	}
}

class AVal implements Expression {
	int value;

	public AVal(String number) {
		this.value = Integer.parseInt(number);
	}

	public String show() {
		return "<IntNode> " + value + "\n"; 
	}

	public Object getValue() {
		return value;
	}

	public Expression interpret() {
		return this;
	}
}

class BVal implements Expression {

	boolean value;

	public BVal(boolean bl) {
		this.value = bl;
	}

	public String show() {
		if (value)
			return "<BoolNode> true\n";
		else
			return "<BoolNode> false\n"; 
	}

	public Object getValue() {
		return (Boolean)value;
	}

	public Expression interpret() {
		return this;
	}
}

class Operation implements Expression {
	
	String value;
	int priority;

	public Operation(String op) {
		this.value = op;

		if (value.equals("+")) {
			priority = 4;
		} else if (value.equals("/")) {
			priority = 5;
		} else if (value.equals(">")) {
			priority = 2;
		} else if (value.equals("&&")) {
			priority = 1;
		} else if (value.equals("!")) {
			priority = 3;
		} else
			priority = 0;

	}

	public String show() {
		return null;
	}

	public Object getValue() {
		return null;
	}

	public Expression interpret() {
		return null;
	}
}

class Par implements Expression {

	@Override
	public String show() {
		return null;
	}

	public Object getValue() {
		return null;
	}

	@Override
	public Expression interpret() {
		return null;
	}
};

class BPar implements Expression {

	@Override
	public String show() {
		return null;
	}

	public Object getValue() {
		return null;
	}

	@Override
	public Expression interpret() {
		return null;
	}
};

class IfMarker implements Expression {

	@Override
	public String show() {
		return null;
	}

	public Object getValue() {
		return null;
	}

	@Override
	public Expression interpret() {
		return null;
	}

}

class ElseMarker implements Expression {

	@Override
	public String show() {
		return null;
	}

	public Object getValue() {
		return null;
	}

	@Override
	public Expression interpret() {
		return null;
	}

}

class WhileMarker implements Expression {

	@Override
	public String show() {
		return null;
	}

	@Override
	public Expression interpret() {
		return null;
	}

	public Object getValue() {
		return null;
	}

}

class AssignmentNode implements Expression {

	Expression e1, e2;
	int line;

	public AssignmentNode(Expression e1, Expression e2, int line) {
		this.e1 = e1;
		this.e2 = e2;
		this.line = line;
	}

	public String show() {

		String build = "";
		build += "<AssignmentNode> =\n";

		String print = "";
		print += e1.show();
		print += e2.show();

		build += Parser.addNewline(print);

		return build;
	}

	public Object getValue() {

		String varb = (String)e1.getValue();
		int fin;

		if (e2 instanceof Variable) {
			Object aux = HelloLexer.variables.get((String)e2.getValue());

			if (aux == null) {
				HelloLexer.error = true;
				HelloLexer.errorString = "UnassignedVar " + line;
				fin = 1;
			} else {
				fin = HelloLexer.variables.get((String)e2.getValue());
			}

		} else 
			fin = (int) e2.getValue();

		HelloLexer.variables.put(varb,fin);

		return null;

	}

	public Expression interpret() {
		return this;
	}
}

class BracketNode implements Expression {

	Expression e1;

	public BracketNode(Expression e1) {
		this.e1 = e1;
	}

	public String show() {

		String build = "";
		build += "<BracketNode> ()\n";

		String print = "";
		print += e1.show();

		build += Parser.addNewline(print);

		return build;
	}

	public Object getValue() {
		return e1.getValue();
	}

	public Expression interpret() {
		return this;
	}
}

class BlockNode implements Expression {

	Expression e1;

	public BlockNode(Expression e1) {
		this.e1 = e1;
	}

	public String show() {


		String build = "";
		build += "<BlockNode> {}\n";

		if (e1 == null) {
			return build;
		}

		String print = "";
		print += e1.show();

		build += Parser.addNewline(print);

		return build;
	}

	public Object getValue() {
		if (e1 != null)
			e1.getValue();
		return null;
	}

	public Expression interpret() {
		return this;
	}
}

class PlusNode implements Expression {

	Expression e1, e2;
	int line;

	public PlusNode(Expression e1, Expression e2, int line) {
		this.e1 = e1;
		this.e2 = e2;
		this.line = line;
	}

	public String show() {

		String build = "";
		build += "<PlusNode> +\n";

		String print = "";
		print += e1.show();
		print += e2.show();

		build += Parser.addNewline(print);

		return build;
	}

	public Object getValue() {

		int x1, x2;

		if (e1 instanceof Variable) {

			Object aux = HelloLexer.variables.get((String)e1.getValue());

			if (aux == null) {
				HelloLexer.error = true;
				HelloLexer.errorString = "UnassignedVar " + line;
				x1 = 1;
			} else {
				x1 = HelloLexer.variables.get((String)e1.getValue());
			}

		} else {

			x1 = (int)e1.getValue();
		
		}

		if (e2 instanceof Variable) {

			Object aux = HelloLexer.variables.get((String)e2.getValue());

			if (aux == null) {
				HelloLexer.error = true;
				HelloLexer.errorString = "UnassignedVar " + line;
				x2 = 1;
			} else {
				x2 = HelloLexer.variables.get((String)e2.getValue());
			}

		} else {
			
			x2 = (int)e2.getValue();
		
		}


		return (Integer)(x1 + x2);
	}

	public Expression interpret() {
		return this;
	}

}

class DivNode implements Expression {

	Expression e1, e2;
	int line;

	public DivNode(Expression e1, Expression e2, int line) {
		this.e1 = e1;
		this.e2 = e2;
		this.line = line;
	}

	public String show() {

		String build = "";
		build += "<DivNode> /\n";

		String print = "";
		print += e1.show();
		print += e2.show();

		build += Parser.addNewline(print);

		return build;
	}

	public Object getValue() {
		
		int x1, x2;

		if (e1 instanceof Variable) {

			Object aux = HelloLexer.variables.get((String)e1.getValue());

			if (aux == null) {
				HelloLexer.error = true;
				HelloLexer.errorString = "UnassignedVar " + line;
				x1 = 1;
			} else {
				x1 = HelloLexer.variables.get((String)e1.getValue());
			}

		} else {

			x1 = (int)e1.getValue();
		
		}

		if (e2 instanceof Variable) {

			Object aux = HelloLexer.variables.get((String)e2.getValue());

			if (aux == null) {
				HelloLexer.error = true;
				HelloLexer.errorString = "UnassignedVar " + line;
				x2 = 1;
			} else {
				x2 = HelloLexer.variables.get((String)e2.getValue());
			}

		} else {

			x2 = (int)e2.getValue();
		
		}

		if (x2 == 0) {
			HelloLexer.error = true;
			HelloLexer.errorString = "DivideByZero " + line;
			return 1;
		}

		return (Integer)(x1/x2);

	}

	public Expression interpret() {
		return this;
	}

}

class SequenceNode implements Expression {

	Expression e1, e2;

	public SequenceNode(Expression e1, Expression e2) {
		this.e1 = e1;
		this.e2 = e2;
	}

	public String show() {

		String build = "";
		build += "<SequenceNode>\n";

		String print = "";
		print += e1.show();
		print += e2.show();

		build += Parser.addNewline(print);

		return build;
	}

	public Object getValue() {
		e1.getValue();
		e2.getValue();
		return null;
	}

	public Expression interpret() {
		return this;
	}

}

class IfNode implements Expression {

	Expression condition;
	Expression b1,b2;

	public IfNode(Expression e1, Expression e2, Expression e3) {
		this.condition = e1;
		this.b1 = e2;
		this.b2 = e3;
	}

	public String show() {

		String build = "";
		build += "<IfNode> if\n";

		String print = "";
		print += condition.show();
		print += b1.show();
		print += b2.show();

		build += Parser.addNewline(print);

		return build;
	}

	public Object getValue() {
		boolean cond = (Boolean)condition.getValue();

		if (cond) {
			b1.getValue();
		} else {
			b2.getValue();
		}

		return null;
	}

	public Expression interpret() {
		return this;
	}

}

class WhileNode implements Expression {

	Expression condition;
	Expression b1;

	public WhileNode(Expression e1, Expression e2) {
		this.condition = e1;
		this.b1 = e2;
	}

	public String show() {

		String build = "";
		build += "<WhileNode> while\n";

		String print = "";
		print += condition.show();
		print += b1.show();

		build += Parser.addNewline(print);

		return build;
	}

	public Object getValue() {
		boolean cond = (Boolean)condition.getValue();

		while (cond) {
			b1.getValue();
			cond = (Boolean)condition.getValue();
		}

		return null;
	}

	public Expression interpret() {
		return this;
	}

}

class GreaterNode implements Expression {

	Expression e1, e2;

	public GreaterNode(Expression e1, Expression e2) {
		this.e1 = e1;
		this.e2 = e2;
	}

	public String show() {

		String build = "";
		build += "<GreaterNode> >\n";

		String print = "";
		print += e1.show();
		print += e2.show();

		build += Parser.addNewline(print);

		return build;
	}

	public Object getValue() {
		int x1, x2;

		if (e1 instanceof Variable) {
			x1 = HelloLexer.variables.get((String)e1.getValue());
		} else {
			x1 = (int)e1.getValue();
		}

		if (e2 instanceof Variable) {
			x2 = HelloLexer.variables.get((String)e2.getValue());
		} else {
			x2 = (int)e2.getValue();
		}

		return (Boolean)(x1 > x2);
	}

	public Expression interpret() {
		return this;
	}

}

class AndNode implements Expression {

	Expression e1, e2;

	public AndNode(Expression e1, Expression e2) {
		this.e1 = e1;
		this.e2 = e2;
	}

	public String show() {

		String build = "";
		build += "<AndNode> &&\n";

		String print = "";
		print += e1.show();
		print += e2.show();

		build += Parser.addNewline(print);

		return build;
	}

	public Object getValue() {
		boolean bl1 = (Boolean) e1.getValue();
		boolean bl2 = (Boolean) e2.getValue();
		return (Boolean) (bl1 && bl2);
	}

	public Expression interpret() {
		return this;
	}

}

class NotNode implements Expression {

	Expression e1;

	public NotNode(Expression e1) {
		this.e1 = e1;
	}

	public String show() {

		String build = "";
		build += "<NotNode> !\n";

		String print = "";
		print += e1.show();

		build += Parser.addNewline(print);

		return build;
	}

	public Object getValue() {
		return (Boolean)(!(Boolean)e1.getValue());
	}

	public Expression interpret() {
		return this;
	}

}

class MainNode implements Expression {

	Expression e1;

	public MainNode(Expression e1) {
		this.e1 = e1;
	}

	public String show() {

		String build = "";
		build += "<MainNode>\n";

		String print = "";
		print += e1.show();

		build += Parser.addNewline(print);

		return build;
	}

	public Object getValue() {
		e1.getValue();
		return null;
	}

	public Expression interpret() {
		e1.getValue();
		return this;
	}

}