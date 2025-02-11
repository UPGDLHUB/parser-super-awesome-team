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

		dfa.addAcceptState("s4", "NUMBER");

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

		dfa.addTransition("s5", "_", "s5");
		dfa.addAcceptState("s5", "IDENTIFIER");

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
			if (!(isOperator(currentChar) || isDelimiter(currentChar) || isSpace(currentChar))) {
				nextState = dfa.getNextState(currentState, currentChar);
				if (nextState != null) {
					string = string + currentChar;
					currentState = nextState;
				} else {
					if (!string.isEmpty()) {
						processString(currentState, string, lineNumber);
						string = "";
					}
					tokens.add(new Token(String.valueOf(currentChar), "ERROR", lineNumber));
					currentState = "s0";
				}
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
