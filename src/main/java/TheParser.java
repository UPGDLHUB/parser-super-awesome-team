import java.util.Vector;

public class TheParser {
	
	private Vector<TheToken> tokens;
	private int currentToken;
	
	public TheParser(Vector<TheToken> tokens) {
		this.tokens = tokens;
		currentToken = 0;
	}
	
	public void run() {
		RULE_PROGRAM();
	}
	
	private void RULE_PROGRAM() {
		System.out.println("- RULE_PROGRAM");
		if (tokens.get(currentToken).getValue().equals("{")) {
			currentToken++;
			System.out.println("- {");
			RULE_BODY();
			if (tokens.get(currentToken).getValue().equals("}")) {
				currentToken++;
				System.out.println("- }");
			} else {
				error(1);
			}
		} else if (tokens.get(currentToken).getType().equals("KEYWORD") && tokens.get(currentToken).getValue().equals("class")) {
			currentToken++;
			System.out.println("-- class");
			if (tokens.get(currentToken).getType().equals("IDENTIFIER")){
				System.out.println("--- IDENTIFIER");
				currentToken++;
			}else{
				error(2);
			}
			if (tokens.get(currentToken).getValue().equals("{")) {
				currentToken++;
				System.out.println("---- {");
			}else{
				error(2);
			}
			while (tokens.get(currentToken).getType().equals("KEYWORD")&&
					(tokens.get(currentToken).getValue().equals("int")||
					tokens.get(currentToken).getValue().equals("float")||
					tokens.get(currentToken).getValue().equals("void")||
					tokens.get(currentToken).getValue().equals("string")||
					tokens.get(currentToken).getValue().equals("char")||
					tokens.get(currentToken).getValue().equals("boolean"))){
				RULE_METHODS();
			}
			if (tokens.get(currentToken).getValue().equals("}")) {
				currentToken++;
				System.out.println("---- }");
			}else{
				error(2);
			}
		}else {
			error(1);
		}
	}

	public void RULE_METHODS() {
		System.out.println("----- RULE_METHODS");
		if (tokens.get(currentToken).getType().equals("KEYWORD")&&
				(tokens.get(currentToken).getValue().equals("int")||
				tokens.get(currentToken).getValue().equals("float")||
				tokens.get(currentToken).getValue().equals("void")||
				tokens.get(currentToken).getValue().equals("char")||
				tokens.get(currentToken).getValue().equals("string")||
				tokens.get(currentToken).getValue().equals("boolean"))) {
			System.out.println("----- TYPE");
			currentToken++;
		}else{
			error(6);
		}
		if (tokens.get(currentToken).getType().equals("IDENTIFIER")){
			System.out.println("----- IDENTIFIER");
			currentToken++;
		}else{
			error(6);
		}
		if (tokens.get(currentToken).getValue().equals("(")){
			System.out.println("----- (");
			currentToken++;
		}else{
			error(6);
		}
		RULE_PARAMS();
		if (tokens.get(currentToken).getValue().equals(")")){
			System.out.println("----- )");
			currentToken++;
		}else{
			error(6);
		}
		if (tokens.get(currentToken).getValue().equals("{")){
			System.out.println("----- {");
			currentToken++;
		}else{
			error(6);
		}
		RULE_BODY();
		if (tokens.get(currentToken).getValue().equals("}")){
			System.out.println("----- }");
			currentToken++;
		}else{
			error(6);
		}



		}

	public void RULE_PARAMS() {
		System.out.println("------ RULE_PARAMS");
		if (tokens.get(currentToken).getType().equals("KEYWORD")) {
			if (tokens.get(currentToken).getType().equals("KEYWORD") &&
					(tokens.get(currentToken).getValue().equals("int") ||
							tokens.get(currentToken).getValue().equals("float") ||
							tokens.get(currentToken).getValue().equals("void") ||
							tokens.get(currentToken).getValue().equals("char") ||
							tokens.get(currentToken).getValue().equals("string") ||
							tokens.get(currentToken).getValue().equals("boolean"))) {
				System.out.println("------- TYPE");
				currentToken++;
			} else {
				error(7);
			}
			if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
				System.out.println("------- IDENTIFIER");
				currentToken++;
			} else {
				error(7);
			}
			if (tokens.get(currentToken).getValue().equals(",")){
				System.out.println("------- ,");
				currentToken++;
				RULE_PARAMS();
			}
		}

	}

	public void RULE_BODY() {
		System.out.println("-- RULE_BODY");
		while (!tokens.get(currentToken).getValue().equals("}")) {
			RULE_EXPRESSION();
			if (tokens.get(currentToken).getValue().equals(";")) {
				System.out.println("-- ;");
				currentToken++;
			} else {
				error(3);
			}
		}
	}
	
	public void RULE_EXPRESSION() {
		System.out.println("--- RULE_EXPRESSION");
		RULE_X();
		while (tokens.get(currentToken).getValue().equals("|")) {
			currentToken++;
			System.out.println("--- |");
			RULE_X();
		}
	}
	
	public void RULE_X() {
		System.out.println("---- RULE_X");
		RULE_Y();
		while (tokens.get(currentToken).getValue().equals("&")) {
			currentToken++;
			System.out.println("---- &");
			RULE_Y();
		}
	}
	
	public void RULE_Y() {
		System.out.println("----- RULE_Y");
		if (tokens.get(currentToken).getValue().equals("!")) {
			currentToken++;
			System.out.println("----- !");
		}
		RULE_R();
	}
	
	public void RULE_R() {
		System.out.println("------ RULE_R");
		RULE_E();
		while (tokens.get(currentToken).getValue().equals("<")
			| tokens.get(currentToken).getValue().equals(">")
			| tokens.get(currentToken).getValue().equals("==")
			| tokens.get(currentToken).getValue().equals("!=")
		) {
			currentToken++;
			System.out.println("------ relational operator");
			RULE_E();
		}
	}
	
	public void RULE_E() {
		System.out.println("------- RULE_E");
		RULE_A();
		while (tokens.get(currentToken).getValue().equals("-")
			| tokens.get(currentToken).getValue().equals("+")
		) {
			currentToken++;
			System.out.println("------- + or -");
			RULE_A();
		}
		
	}
	
	public void RULE_A() {
		System.out.println("-------- RULE_A");
		RULE_B();
		while (tokens.get(currentToken).getValue().equals("/")
			| tokens.get(currentToken).getValue().equals("*")
		) {
			currentToken++;
			System.out.println("-------- * or /");
			RULE_B();
		}
		
	}
	
	public void RULE_B() {
		System.out.println("--------- RULE_B");
		if (tokens.get(currentToken).getValue().equals("-")) {
			currentToken++;
			System.out.println("--------- -");
		}
		RULE_C();
	}
	
		public void RULE_C() {
		System.out.println("---------- RULE_C");
		if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
			currentToken++;
			System.out.println("---------- IDENTIFIER");
		} else if (tokens.get(currentToken).getType().equals("INTEGER")) {
			currentToken++;
			System.out.println("---------- INTEGER");
		} else if (tokens.get(currentToken).getValue().equals("(")) {
			currentToken++;
			System.out.println("---------- (");
			RULE_EXPRESSION();
			if (tokens.get(currentToken).getValue().equals(")")) {
				currentToken++;
				System.out.println("---------- )");
			} else {
				error(4);
			}
		} else {
			error(5);
		}
	}
	
	private void error(int error) {
		System.out.println("Error " + error +
			" at line " + tokens.get(currentToken).getLineNumber());
		System.exit(1);
	}
	
}

