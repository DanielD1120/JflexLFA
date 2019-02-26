import java.util.*;
import java.util.Stack;
import java.util.HashMap;

%%
 
%class HelloLexer
%line
%int

%{

  	Stack<Expression> stack = new Stack<>();
    static HashMap<String, Integer> variables = new HashMap<>();
    boolean variableDeclaration = false;
    ArrayList <String> errors = new ArrayList<>();
    static boolean error;
    static String errorString;

    Expression get_nth_element_from_stack(int element_number) {
        Stack<Expression> temp_stack = new Stack<>();

        if (element_number > stack.size()) {
            return null;
        }

        for (int j = 0; j < element_number; ++j) {
            temp_stack.push(stack.pop());
        }

        Expression res = temp_stack.peek();

        for (int j = 0; j < element_number; ++j) {
            stack.push(temp_stack.pop());
        }
        return res;
    }

    boolean checkIfReductionNedded(Operation k) {

        Expression aux = get_nth_element_from_stack(2);
        Operation x;

        if (aux instanceof Operation) {
            x = (Operation) aux;
        } else
            return false;

        if (k.priority > x.priority)
            return false;
        else
            return true;


    }

    void makeSpecificReduction(Operation op) {

        if (op.value.equals("=")) {

            Expression e1 = stack.pop();
            stack.pop();
            Expression e2 = stack.pop();

            stack.push(new AssignmentNode(e2, e1, yyline + 1));
        }

        if (op.value.equals("+")) {

            Expression e1 = stack.pop();
            stack.pop();
            Expression e2 = stack.pop();

            stack.push(new PlusNode(e2, e1, yyline + 1));

        }

        if (op.value.equals("/")) {

            Expression e1 = stack.pop();
            stack.pop();
            Expression e2 = stack.pop();

            stack.push(new DivNode(e2, e1, yyline + 1));

        }

        if (op.value.equals(">")) {

            Expression e1 = stack.pop();
            stack.pop();
            Expression e2 = stack.pop();

            stack.push(new GreaterNode(e2, e1));

        }

        if (op.value.equals("!")) {

            Expression e1 = stack.pop();
            stack.pop();

            stack.push(new NotNode(e1));

        }

        if (op.value.equals("&&")) {

            Expression e1 = stack.pop();
            stack.pop();
            Expression e2 = stack.pop();

            stack.push(new AndNode(e2, e1));
        }

    }

    void reduceOneExpresion() {

        Expression x = get_nth_element_from_stack(2);

        if (x instanceof Operation) {

            Operation op = (Operation)x;

            makeSpecificReduction(op);

        }

    }

    void reduceExpresion() {

        Expression x = get_nth_element_from_stack(2);

        while (x instanceof Operation) {

            Operation op = (Operation)x;

            makeSpecificReduction(op);

            x = get_nth_element_from_stack(2);

        }
    }

    void makeBracket() {

        Expression x = get_nth_element_from_stack(2);

        while (x instanceof Operation) {

            Operation op = (Operation)x;

            makeSpecificReduction(op);

            x = get_nth_element_from_stack(2);

        }

        x = stack.pop();
        stack.pop();
        stack.push(new BracketNode(x));
    
    }

    void checkIfBlock() {

        Expression x = get_nth_element_from_stack(2);

        if (x instanceof ElseMarker) {
            Expression b2 = stack.pop();
            stack.pop();
            Expression b1 = stack.pop();
            Expression condition = stack.pop();
            stack.pop();
            stack.push(new IfNode(condition,b1,b2));
        }
    }

    void checkWhileBlock() {

        Expression x = get_nth_element_from_stack(3);

        if (x instanceof WhileMarker) {
            Expression b1 = stack.pop();
            Expression condition = stack.pop();
            stack.pop();
            stack.push(new WhileNode(condition,b1));
        }

    }

    void makeBlock() {

        if (stack.peek() instanceof BPar) {
            stack.pop();
            stack.push(new BlockNode(null));
            checkIfBlock();
            checkWhileBlock();
            return;
        }

        if (get_nth_element_from_stack(2) instanceof BPar) {
            Expression x = stack.pop();
            stack.pop();
            stack.push(new BlockNode(x));
            checkIfBlock();
            checkWhileBlock();
            return;
        }

        Expression x = get_nth_element_from_stack(2);

        while (!(x instanceof BPar)) {

            Expression e1 = stack.pop();
            Expression e2 = stack.pop();

            stack.push(new SequenceNode(e2,e1));

            x = get_nth_element_from_stack(2);
        }

        x = stack.pop();
        stack.pop();
        stack.push(new BlockNode(x));
        checkIfBlock();
        checkWhileBlock();
        
    }

%}

LineTerminator = \r|\n|\r\n
digit = [1-9]
number = {digit}(0|{digit})* | 0
string = [a-z]+
var = {string}
aval = {number}
bval = "true" | "false"
ints = "int"
statementEnd = ";"
assigment = "="
add = "+"
div = "/"
open_par = "("
close_par = ")"
ifs = "if"
elses = "else"
open_block = "{"
close_block = "}"
greater = ">"
not = "!"
and = "&&"
whiles = "while"
 
%%   

{LineTerminator} {}

{statementEnd} 
{
    if (variableDeclaration) {
        variableDeclaration = false;
    } else {
        reduceExpresion();
    }
}


{ints} 
{
    variableDeclaration = true;
}


{bval} { stack.push(new BVal(Boolean.parseBoolean(yytext())));}

{open_par} { stack.push(new Par());}

{close_par} { makeBracket();}

{assigment} { stack.push(new Operation(yytext())); }

{add} { 
    Operation k = new Operation(yytext());
    while (checkIfReductionNedded(k))
        reduceOneExpresion();
    stack.push(k);
}

{div} { 
    Operation k = new Operation(yytext());
    while (checkIfReductionNedded(k))
        reduceOneExpresion();
    stack.push(k);
}

{greater} {
    Operation k = new Operation(yytext());
    while (checkIfReductionNedded(k))
        reduceOneExpresion();
    stack.push(k);
}

{and} {
    Operation k = new Operation(yytext());
    while (checkIfReductionNedded(k))
        reduceOneExpresion();
    stack.push(k);
}

{not} {
    Operation k = new Operation(yytext());
    while (checkIfReductionNedded(k))
        reduceOneExpresion();
    stack.push(k);
}

{ifs} { stack.push(new IfMarker()); }

{elses} { stack.push(new ElseMarker()); }

{open_block} { stack.push(new BPar()); }

{close_block} { makeBlock(); }

{whiles} {stack.push(new WhileMarker());}


{var} 
{
    if (variableDeclaration) {
        variables.put(yytext(), null);
    } else {
        stack.push(new Variable(yytext()));

        if (!variables.containsKey(yytext())) {
            int currentLine = yyline + 1;
            String k = "UnassignedVar " + currentLine;
            errors.add(k);
        }

    }

}


{aval} { stack.push(new AVal(yytext())); }
 
. {}

