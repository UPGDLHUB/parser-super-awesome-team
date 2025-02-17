import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LexerTestOne class to test the Lexer class
 * This version is implemented with JUnit 5
 *
 * @author javiergs
 * @version 1.0
 */
class LexerTest {
	
	@Test
	void testingNumberOfTokens() throws IOException {
		File file = new File("src/main/resources/input.txt");
		Lexer lexer = new Lexer(file);
		lexer.run();
		assertFalse (lexer.getTokens().isEmpty(), "Tokens should not be empty");
		assertEquals(lexer.getTokens().size(), 20, "Number of tokens should be 20");
		System.out.println("Test passed.");

		file = new File("src/main/resources/input2.txt");
		lexer = new Lexer(file);
		lexer.run();
		assertFalse (lexer.getTokens().isEmpty(), "Tokens should not be empty");
		assertEquals(lexer.getTokens().size(), 15, "Number of tokens should be 15");
		System.out.println("Test passed.");

		file = new File("src/main/resources/input3.txt");
		lexer = new Lexer(file);
		lexer.run();
		assertFalse (lexer.getTokens().isEmpty(), "Tokens should not be empty");
		assertEquals(lexer.getTokens().size(), 7, "Number of tokens should be 7");
		System.out.println("Test passed.");

		file = new File("src/main/resources/input4.txt");
		lexer = new Lexer(file);
		lexer.run();
		assertFalse (lexer.getTokens().isEmpty(), "Tokens should not be empty");
		assertEquals(lexer.getTokens().size(), 4, "Number of tokens should be 4");
		System.out.println("Test passed.");

		file = new File("src/main/resources/input5.txt");
		lexer = new Lexer(file);
		lexer.run();
		assertFalse (lexer.getTokens().isEmpty(), "Tokens should not be empty");
		assertEquals(lexer.getTokens().size(), 45, "Number of tokens should be 45");
		System.out.println("Test passed.");
	}

	
}
