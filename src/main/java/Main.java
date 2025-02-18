import java.io.File;
import java.io.IOException;

/**
 * Main.java
 * ---------------
 * Entry point for the lexical analyzer application.
 * This class demonstrates the usage of the Lexer class by:
 * 1. Reading an input file
 * 2. Creating a lexer instance
 * 3. Running the lexical analysis
 * 4. Printing the resulting tokens
 *
 * @author javiergs
 * @author eduardomv
 * @author santiarr
 * @author yawham
 * @version 2.0
 */
public class Main {
	
	public static void main(String[] args) throws IOException {
		File file = new File("src/main/resources/input.txt");
		Lexer lexer = new Lexer(file);
		lexer.run();
		lexer.printTokens();
	}
	
}