/**
 * A Token is a pair of a value (string or word) and its type
 *
 * @author javiergs
 * @author eduardomv
 * @author santiarr
 * @author yawham
 * @version 1.0
 */
public class Token {
	
	private String value;
	private String type;
	private int lineNumber;
	
	public Token(String value, String type, int lineNumber) {
		this.value = value;
		this.type = type;
		this.lineNumber = lineNumber;
	}
	
	public String getValue() {
		return value;
	}
	
	public String getType() {
		return type;
	}

	public int getLineNumber() {
		return lineNumber;
	}
}

