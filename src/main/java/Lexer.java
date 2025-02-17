import java.io.*;
import java.util.*;


/**
 * Lexer class to analyze the input file
 * This one is an initial version that uses a DFA to recognize binary numbers
 *
 * @author javiergs
 * @author eduardomv
 * @author santiarr
 * @author yawham
 * @version 0.1
 */
public class Lexer {

	private File file;
	private Automata dfa;
	private Vector<Token> tokens;
	private static final Set<String> keywords = new HashSet<>(Arrays.asList(
			"int", "end", "if", "else", "while", "do", "break", "continue"
	));

	public Lexer(File file) {
		this.file = file;
		tokens = new Vector<>();
		dfa = new Automata();

		//Binary transitions
		dfa.addTransition("s0", "0", "s1");
		dfa.addTransition("s1", "b", "s2");
		dfa.addTransition("s1", "B", "s2");
		dfa.addTransition("s2", "0", "s3");
		dfa.addTransition("s2", "1", "s3");
		dfa.addTransition("s3", "0", "s3");
		dfa.addTransition("s3", "1", "s3");

		dfa.addAcceptState("s3", "BINARY");

		// Decimal number transitions
		dfa.addTransition("s0", "1", "s4");
		dfa.addTransition("s0", "2", "s4");
		dfa.addTransition("s0", "3", "s4");
		dfa.addTransition("s0", "4", "s4");
		dfa.addTransition("s0", "5", "s4");
		dfa.addTransition("s0", "6", "s4");
		dfa.addTransition("s0", "7", "s4");
		dfa.addTransition("s0", "8", "s4");
		dfa.addTransition("s0", "9", "s4");
		dfa.addTransition("s4", "0", "s4");
		dfa.addTransition("s4", "1", "s4");
		dfa.addTransition("s4", "2", "s4");
		dfa.addTransition("s4", "3", "s4");
		dfa.addTransition("s4", "4", "s4");
		dfa.addTransition("s4", "5", "s4");
		dfa.addTransition("s4", "6", "s4");
		dfa.addTransition("s4", "7", "s4");
		dfa.addTransition("s4", "8", "s4");
		dfa.addTransition("s4", "9", "s4");

		dfa.addAcceptState("s1", "INTEGER");
		dfa.addAcceptState("s4", "INTEGER");

		// Identifier transitions (start with letter, can contain letters, numbers, underscore)
		for (char c = 'a'; c <= 'z'; c++) {
			dfa.addTransition("s0", String.valueOf(c), "s5");
			dfa.addTransition("s5", String.valueOf(c), "s5");
		}

		for (char c = 'A'; c <= 'Z'; c++) {
			dfa.addTransition("s0", String.valueOf(c), "s5");
			dfa.addTransition("s5", String.valueOf(c), "s5");
		}

		for (char c = '0'; c <= '9'; c++) {
			dfa.addTransition("s5", String.valueOf(c), "s5");
		}
		dfa.addTransition("s0", "_", "s5");
		dfa.addTransition("s0", "$", "s5");
		dfa.addTransition("s5", "_", "s5");
		dfa.addTransition("s5", "$", "s5");
		dfa.addAcceptState("s5", "IDENTIFIER");


		//String transitions (start with ", can contain letters, numbers, underscore and symbols)
		dfa.addTransition("s0", "\"", "s6");
		for (char c = 32; c <= 126; c++) {
			if (c != '"') {
				dfa.addTransition("s6", String.valueOf(c), "s6");
			}
		}
		dfa.addTransition("s6", "\"", "s7");
		dfa.addAcceptState("s7", "STRING");


		//Hexadecimal transitions
		dfa.addTransition("s1", "x", "s8");
		dfa.addTransition("s1", "X", "s8");
		for (char c = 'a'; c <= 'f'; c++) {
			dfa.addTransition("s8", String.valueOf(c), "s9");
			dfa.addTransition("s9", String.valueOf(c), "s9");
		}
		for (char c = 'A'; c <= 'F'; c++) {
			dfa.addTransition("s8", String.valueOf(c), "s9");
			dfa.addTransition("s9", String.valueOf(c), "s9");
		}

		for (char c = '0'; c <= '9'; c++) {
			dfa.addTransition("s8", String.valueOf(c), "s9");
			dfa.addTransition("s9", String.valueOf(c), "s9");
		}
		dfa.addAcceptState("s9", "HEXADECIMAL");


		//Octal transition
		dfa.addTransition("s1", "0", "s10");
		dfa.addTransition("s1", "1", "s10");
		dfa.addTransition("s1", "2", "s10");
		dfa.addTransition("s1", "3", "s10");
		dfa.addTransition("s1", "4", "s10");
		dfa.addTransition("s1", "5", "s10");
		dfa.addTransition("s1", "6", "s10");
		dfa.addTransition("s1", "7", "s10");
		dfa.addTransition("s1", "8", "s4");
		dfa.addTransition("s1", "9", "s4");
		dfa.addTransition("s10", "0", "s10");
		dfa.addTransition("s10", "1", "s10");
		dfa.addTransition("s10", "2", "s10");
		dfa.addTransition("s10", "3", "s10");
		dfa.addTransition("s10", "4", "s10");
		dfa.addTransition("s10", "5", "s10");
		dfa.addTransition("s10", "6", "s10");
		dfa.addTransition("s10", "7", "s10");
		dfa.addTransition("s10", "8", "s4");
		dfa.addTransition("s10", "9", "s4");

		dfa.addAcceptState("s10", "OCTAL");

//		Float transitions
		dfa.addTransition("s1", ".", "s11");
		dfa.addTransition("s4", ".", "s11");
		dfa.addTransition("s10", ".", "s11");
		for (char c = '0'; c <= '9'; c++) {
			dfa.addTransition("s11", String.valueOf(c), "s12");
			dfa.addTransition("s12", String.valueOf(c), "s12");
		}
		dfa.addAcceptState("s11", "FLOAT");
		dfa.addAcceptState("s12", "FLOAT");



		// Exponent transition from integer
		dfa.addTransition("s4", "e", "s13");
		dfa.addTransition("s4", "E", "s13");
		dfa.addTransition("s13", "-", "s14");

		for (char c = '1'; c <= '9'; c++) {
			dfa.addTransition("s13", String.valueOf(c), "s15");
			dfa.addTransition("s14", String.valueOf(c), "s15");
			dfa.addTransition("s15", String.valueOf(c), "s15");
		}
		dfa.addTransition("s15", String.valueOf('0'), "s15");
		dfa.addAcceptState("s15", "INTEGER");

		// Exponent transition from float
		dfa.addTransition("s12", "e", "s16");
		dfa.addTransition("s12", "E", "s16");
		dfa.addTransition("s16", "-", "s17");

		for (char c = '1'; c <= '9'; c++) {
			dfa.addTransition("s16", String.valueOf(c), "s18");
			dfa.addTransition("s17", String.valueOf(c), "s18");
			dfa.addTransition("s18", String.valueOf(c), "s18");
		}
		dfa.addTransition("s18", String.valueOf('0'), "s18");
		dfa.addAcceptState("s18", "FLOAT");


		//Add F to be float
		dfa.addTransition("s1", String.valueOf('f'), "s19");
		dfa.addTransition("s1", String.valueOf('F'), "s19");
		dfa.addTransition("s4", String.valueOf('f'), "s19");
		dfa.addTransition("s4", String.valueOf('F'), "s19");
		dfa.addTransition("s11", String.valueOf('f'), "s19");
		dfa.addTransition("s11", String.valueOf('F'), "s19");
		dfa.addTransition("s12", String.valueOf('f'), "s19");
		dfa.addTransition("s12", String.valueOf('F'), "s19");
		dfa.addTransition("s18", String.valueOf('f'), "s19");
		dfa.addTransition("s18", String.valueOf('F'), "s19");

		dfa.addAcceptState("s19", "FLOAT");

	}

	public void run() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;
		int lineNumber = 1;
		while ((line = reader.readLine()) != null) {
			algorithm(line, lineNumber);
			lineNumber++;
		}
	}

	private void algorithm(String line, int lineNumber) {
		String currentState = "s0";
		String nextState;
		String string = "";
		int index = 0;

		while (index < line.length()) {

			char currentChar = line.charAt(index);
			if (
					//Pass if String
					Objects.equals(currentState, "s6") ||
					//Pass if Integer and Octal To Float
					((Objects.equals(currentState, "s4") || Objects.equals(currentState, "s1")|| Objects.equals(currentState, "s10")) && currentChar == '.') ||
					// Pass if Integer of Float with negative Exp
					(Objects.equals(currentState,"s13") || (Objects.equals(currentState,"s16")) && currentChar == '-')

			){
				//Ignores specific operators and delimiters for certain states
				nextState = dfa.getNextState(currentState, currentChar);
				string = string + currentChar;
				currentState = nextState;
				if (Objects.equals(currentState, "s7") ) {
					//Accepts a complete string
					String tokenType = dfa.getAcceptStateName(currentState);
					tokens.add(new Token(string, tokenType, lineNumber));
					currentState = "s0";
					string = "";
				}
			} else if (!(isOperator(currentChar) || isDelimiter(currentChar) || isSpace(currentChar))) {
				if (currentChar == '"' && !string.isEmpty()) {
					processString(currentState, string, lineNumber);
					currentState = "s0";
					string = "";
				}
				nextState = dfa.getNextState(currentState, currentChar);
				string = string + currentChar;
				currentState = nextState;
			} else {
				if (!string.isEmpty()) {
					processString(currentState, string, lineNumber);
				}
				if (isOperator(currentChar)) {
					tokens.add(new Token(String.valueOf(currentChar), "OPERATOR", lineNumber));
				} else if (isDelimiter(currentChar)) {
					tokens.add(new Token(String.valueOf(currentChar), "DELIMITER", lineNumber));
				}
				currentState = "s0";
				string = "";
			}
			index++;
		}
		// last word
		if (!string.isEmpty()) {
			processString(currentState, string, lineNumber);
		}
	}

	private void processString(String currentState, String string, int lineNumber) {
		if (dfa.isAcceptState(currentState)) {
			String tokenType = dfa.getAcceptStateName(currentState);
			if (tokenType.equals("IDENTIFIER") && keywords.contains(string.toLowerCase())) {
				tokenType = "KEYWORD";
			}
			tokens.add(new Token(string, tokenType, lineNumber));
		} else {
			tokens.add(new Token(string, "ERROR", lineNumber));
		}
	}

	private boolean isSpace(char c) {
		return c == ' ' || c == '\t' || c == '\n';
	}

	private boolean isDelimiter(char c) {
		return c == ',' || c == ';' || c == '.' || c == '(' || c == ')' ||
				c == '{' || c == '}' || c == '[' || c == ']';
	}

	private boolean isOperator(char c) {
		return c == '=' || c == '+' || c == '-' || c == '*' || c == '/' ||
				c == '<' || c == '>' || c == '!' || c == '&' || c == '|';
	}

	public void printTokens() {
		System.out.println("\nToken List:");
		System.out.printf("%10s\t|\t%10s\t|\t%s\n", "Value", "Type", "Line");
		System.out.println("----------------------------------------");
		for (Token token : tokens) {
			System.out.printf("%10s\t|\t%10s\t|\t%d\n",
					token.getValue(), token.getType(), token.getLineNumber());
		}
	}

	public Vector<Token> getTokens() {
		return tokens;
	}

}
