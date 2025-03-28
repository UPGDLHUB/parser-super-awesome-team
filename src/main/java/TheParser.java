import java.util.Vector;

/**
 * Main class to run the lexer
 *
 * @author eduardomv
 * @author santiarr
 * @author yawham
 * @version 1.5
 */

public class TheParser {

	private Vector<TheToken> tokens;
	private int currentToken;

	public TheParser(Vector<TheToken> tokens) {
		this.tokens = tokens;
		currentToken = 0;
	}

	public void run() {
		RULE_PROGRAM();
		if (currentToken != tokens.size()) {
			error(99); // Unexpected tokens at the end
		}
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
		} else if (tokens.get(currentToken).getType().equals("KEYWORD") &&
				tokens.get(currentToken).getValue().equals("class")) {
			currentToken++;
			System.out.println("-- class");

			if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
				System.out.println("--- IDENTIFIER: " + tokens.get(currentToken).getValue());
				currentToken++;
			} else {
				error(2);
			}

			if (tokens.get(currentToken).getValue().equals("{")) {
				currentToken++;
				System.out.println("---- {");

				while (!tokens.get(currentToken).getValue().equals("}")) {
					if (isType()) {
						if (isMethodDeclaration()) {
							RULE_METHODS();
						} else {
							RULE_VARIABLE();
							if (tokens.get(currentToken).getValue().equals(";")) {
								currentToken++;
								System.out.println("---- ;");
							} else {
								error(3);
							}
						}
					} else {
						error(4);
					}
				}

				if (tokens.get(currentToken).getValue().equals("}")) {
					currentToken++;
					System.out.println("---- }");
				} else {
					error(5);
				}
			} else {
				error(6);
			}
		} else {
			error(7);
		}
	}

	private void RULE_METHODS() {
		System.out.println("----- RULE_METHODS");
		RULE_TYPE();

		if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
			System.out.println("----- IDENTIFIER: " + tokens.get(currentToken).getValue());
			currentToken++;
		} else {
			error(8);
		}

		if (tokens.get(currentToken).getValue().equals("(")) {
			currentToken++;
			System.out.println("----- (");
			RULE_PARAMS();

			if (tokens.get(currentToken).getValue().equals(")")) {
				currentToken++;
				System.out.println("----- )");
			} else {
				error(9);
			}

			if (tokens.get(currentToken).getValue().equals("{")) {
				currentToken++;
				System.out.println("----- {");
				RULE_BODY();

				if (tokens.get(currentToken).getValue().equals("}")) {
					currentToken++;
					System.out.println("----- }");
				} else {
					error(10);
				}
			} else {
				error(11);
			}
		} else {
			error(12);
		}
	}

	private void RULE_PARAMS() {
		System.out.println("------ RULE_PARAMS");
		if (isType()) {
			RULE_TYPE();

			if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
				System.out.println("------ IDENTIFIER: " + tokens.get(currentToken).getValue());
				currentToken++;
			} else {
				error(13);
			}

			while (tokens.get(currentToken).getValue().equals(",")) {
				currentToken++;
				System.out.println("------ ,");

				if (isType()) {
					RULE_TYPE();

					if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
						System.out.println("------ IDENTIFIER: " + tokens.get(currentToken).getValue());
						currentToken++;
					} else {
						error(14);
					}
				} else {
					error(15);
				}
			}
		}
	}

	private void RULE_BODY() {
		System.out.println("-- RULE_BODY");
		while (!tokens.get(currentToken).getValue().equals("}")) {
			if (isType()) {
				RULE_VARIABLE();
				if (tokens.get(currentToken).getValue().equals(";")) {
					currentToken++;
					System.out.println("-- ;");
				} else {
					error(16);
				}
			} else if (isAssignment()) {
				RULE_ASSIGNMENT();
				if (tokens.get(currentToken).getValue().equals(";")) {
					currentToken++;
					System.out.println("-- ;");
				} else {
					error(17);
				}
			} else if (isMethodCall()) {
				RULE_CALL_METHOD();
				if (tokens.get(currentToken).getValue().equals(";")) {
					currentToken++;
					System.out.println("-- ;");
				} else {
					error(18);
				}
			} else if (isReturnStatement()) {
				RULE_RETURN();
			} else if (isWhileStatement()) {
				RULE_WHILE();
			} else if (isIfStatement()) {
				RULE_IF();
			} else if (isDoStatement()) {
				RULE_DO_WHILE();
			} else if (isForStatement()) {
				RULE_FOR();
			} else if (isSwitchStatement()) {
				RULE_SWITCH();
			} else if (tokens.get(currentToken).getValue().equals("break")) {
				currentToken++;
				System.out.println("-- break");
				if (tokens.get(currentToken).getValue().equals(";")) {
					currentToken++;
					System.out.println("-- ;");
				} else {
					error(20);
				}
			} else {
				RULE_EXPRESSION();
				if (tokens.get(currentToken).getValue().equals(";")) {
					currentToken++;
					System.out.println("-- ;");
				} else {
					error(21);
				}
			}
		}
	}

	private void RULE_VARIABLE() {
		System.out.println("--- RULE_VARIABLE");
		RULE_TYPE();

		if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
			System.out.println("--- IDENTIFIER: " + tokens.get(currentToken).getValue());
			currentToken++;

			if (tokens.get(currentToken).getValue().equals("=")) {
				currentToken++;
				System.out.println("--- =");
				RULE_EXPRESSION();
			}
		} else {
			error(22);
		}
	}

	private void RULE_ASSIGNMENT() {
		System.out.println("--- RULE_ASSIGNMENT");
		if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
			System.out.println("--- IDENTIFIER: " + tokens.get(currentToken).getValue());
			currentToken++;

			if (tokens.get(currentToken).getValue().equals("=")) {
				currentToken++;
				System.out.println("--- =");
				RULE_EXPRESSION();
			} else {
				error(23);
			}
		} else {
			error(24);
		}
	}

	private void RULE_CALL_METHOD() {
		System.out.println("--- RULE_CALL_METHOD");
		if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
			System.out.println("--- IDENTIFIER: " + tokens.get(currentToken).getValue());
			currentToken++;

			if (tokens.get(currentToken).getValue().equals("(")) {
				currentToken++;
				System.out.println("--- (");
				RULE_PARAM_VALUES();

				if (tokens.get(currentToken).getValue().equals(")")) {
					currentToken++;
					System.out.println("--- )");
				} else {
					error(25);
				}
			} else {
				error(26);
			}
		} else {
			error(27);
		}
	}

	private void RULE_PARAM_VALUES() {
		System.out.println("---- RULE_PARAM_VALUES");
		if (!tokens.get(currentToken).getValue().equals(")")) {
			RULE_EXPRESSION();

			while (tokens.get(currentToken).getValue().equals(",")) {
				currentToken++;
				System.out.println("---- ,");
				RULE_EXPRESSION();
			}
		}
	}

	private void RULE_RETURN() {
		System.out.println("--- RULE_RETURN");
		if (tokens.get(currentToken).getValue().equals("return")) {
			currentToken++;
			System.out.println("--- return");

			if (!tokens.get(currentToken).getValue().equals(";")) {
				RULE_EXPRESSION();
			}

			if (tokens.get(currentToken).getValue().equals(";")) {
				currentToken++;
				System.out.println("--- ;");
			} else {
				error(19);
			}
		} else {
			error(28);
		}
	}

	private void RULE_WHILE() {
		System.out.println("--- RULE_WHILE");
		if (tokens.get(currentToken).getValue().equals("while")) {
			currentToken++;
			System.out.println("--- while");

			if (tokens.get(currentToken).getValue().equals("(")) {
				currentToken++;
				System.out.println("--- (");
				RULE_EXPRESSION();

				if (tokens.get(currentToken).getValue().equals(")")) {
					currentToken++;
					System.out.println("--- )");
					RULE_STATEMENT_BLOCK();
				} else {
					error(29);
				}
			} else {
				error(30);
			}
		} else {
			error(31);
		}
	}

	private void RULE_IF() {
		System.out.println("--- RULE_IF");
		if (tokens.get(currentToken).getValue().equals("if")) {
			currentToken++;
			System.out.println("--- if");

			if (tokens.get(currentToken).getValue().equals("(")) {
				currentToken++;
				System.out.println("--- (");
				RULE_EXPRESSION();

				if (tokens.get(currentToken).getValue().equals(")")) {
					currentToken++;
					System.out.println("--- )");
					RULE_STATEMENT_BLOCK();

					if (tokens.get(currentToken).getValue().equals("else")) {
						currentToken++;
						System.out.println("--- else");
						RULE_STATEMENT_BLOCK();
					}
				} else {
					error(32);
				}
			} else {
				error(33);
			}
		} else {
			error(34);
		}
	}

	private void RULE_DO_WHILE() {
		System.out.println("--- RULE_DO_WHILE");
		if (tokens.get(currentToken).getValue().equals("do")) {
			currentToken++;
			System.out.println("--- do");
			RULE_STATEMENT_BLOCK();

			if (tokens.get(currentToken).getValue().equals("while")) {
				currentToken++;
				System.out.println("--- while");

				if (tokens.get(currentToken).getValue().equals("(")) {
					currentToken++;
					System.out.println("--- (");
					RULE_EXPRESSION();

					if (tokens.get(currentToken).getValue().equals(")")) {
						currentToken++;
						System.out.println("--- )");

						if (tokens.get(currentToken).getValue().equals(";")) {
							currentToken++;
							System.out.println("--- ;");
						} else {
							error(35);
						}
					} else {
						error(36);
					}
				} else {
					error(37);
				}
			} else {
				error(38);
			}
		} else {
			error(39);
		}
	}

	private void RULE_FOR() {
		System.out.println("--- RULE_FOR");
		if (tokens.get(currentToken).getValue().equals("for")) {
			currentToken++;
			System.out.println("--- for");

			if (tokens.get(currentToken).getValue().equals("(")) {
				currentToken++;
				System.out.println("--- (");

				if (isType()) {
					RULE_VARIABLE();
				} else if (!tokens.get(currentToken).getValue().equals(";")) {
					RULE_EXPRESSION();
				}

				if (tokens.get(currentToken).getValue().equals(";")) {
					currentToken++;
					System.out.println("--- ;");
				} else {
					error(40);
				}

				if (!tokens.get(currentToken).getValue().equals(";")) {
					RULE_EXPRESSION();
				}

				if (tokens.get(currentToken).getValue().equals(";")) {
					currentToken++;
					System.out.println("--- ;");
				} else {
					error(41);
				}

				if (!tokens.get(currentToken).getValue().equals(")")) {
					if (isAssignment()) {
						RULE_ASSIGNMENT();
					} else {
						RULE_EXPRESSION();
					}
				}

				if (tokens.get(currentToken).getValue().equals(")")) {
					currentToken++;
					System.out.println("--- )");
					RULE_STATEMENT_BLOCK();
				} else {
					error(42);
				}
			} else {
				error(43);
			}
		} else {
			error(44);
		}
	}

	private void RULE_SWITCH() {
		System.out.println("--- RULE_SWITCH");
		if (tokens.get(currentToken).getValue().equals("switch")) {
			currentToken++;
			System.out.println("--- switch");

			if (tokens.get(currentToken).getValue().equals("(")) {
				currentToken++;
				System.out.println("--- (");
				RULE_EXPRESSION();

				if (tokens.get(currentToken).getValue().equals(")")) {
					currentToken++;
					System.out.println("--- )");

					if (tokens.get(currentToken).getValue().equals("{")) {
						currentToken++;
						System.out.println("--- {");

						while (!tokens.get(currentToken).getValue().equals("}")) {
							if (tokens.get(currentToken).getValue().equals("case")) {
								currentToken++;
								System.out.println("---- case");
								RULE_EXPRESSION();

								if (tokens.get(currentToken).getValue().equals(":")) {
									currentToken++;
									System.out.println("---- :");

									while (!tokens.get(currentToken).getValue().equals("case") &&
											!tokens.get(currentToken).getValue().equals("default") &&
											!tokens.get(currentToken).getValue().equals("}")) {
										RULE_BODY();
									}
								} else {
									error(45);
								}
							} else if (tokens.get(currentToken).getValue().equals("default")) {
								currentToken++;
								System.out.println("---- default");

								if (tokens.get(currentToken).getValue().equals(":")) {
									currentToken++;
									System.out.println("---- :");

									while (!tokens.get(currentToken).getValue().equals("}")) {
										RULE_BODY();
									}
								} else {
									error(46);
								}
							} else {
								error(47);
							}
						}

						if (tokens.get(currentToken).getValue().equals("}")) {
							currentToken++;
							System.out.println("--- }");
						} else {
							error(48);
						}
					} else {
						error(49);
					}
				} else {
					error(50);
				}
			} else {
				error(51);
			}
		} else {
			error(52);
		}
	}

	private void RULE_STATEMENT_BLOCK() {
		System.out.println("---- RULE_STATEMENT_BLOCK");
		if (tokens.get(currentToken).getValue().equals("{")) {
			currentToken++;
			System.out.println("---- {");
			RULE_BODY();

			if (tokens.get(currentToken).getValue().equals("}")) {
				currentToken++;
				System.out.println("---- }");
			} else {
				error(53);
			}
		} else {
			if (isType()) {
				RULE_VARIABLE();
				if (tokens.get(currentToken).getValue().equals(";")) {
					currentToken++;
					System.out.println("---- ;");
				} else {
					error(54);
				}
			} else if (isAssignment()) {
				RULE_ASSIGNMENT();
				if (tokens.get(currentToken).getValue().equals(";")) {
					currentToken++;
					System.out.println("---- ;");
				} else {
					error(55);
				}
			} else if (isMethodCall()) {
				RULE_CALL_METHOD();
				if (tokens.get(currentToken).getValue().equals(";")) {
					currentToken++;
					System.out.println("---- ;");
				} else {
					error(56);
				}
			} else if (isReturnStatement()) {
				RULE_RETURN();
				if (tokens.get(currentToken).getValue().equals(";")) {
					currentToken++;
					System.out.println("---- ;");
				} else {
					error(57);
				}
			} else if (isWhileStatement()) {
				RULE_WHILE();
			} else if (isIfStatement()) {
				RULE_IF();
			} else if (isDoStatement()) {
				RULE_DO_WHILE();
			} else if (isForStatement()) {
				RULE_FOR();
			} else if (isSwitchStatement()) {
				RULE_SWITCH();
			} else {
				RULE_EXPRESSION();
				if (tokens.get(currentToken).getValue().equals(";")) {
					currentToken++;
					System.out.println("---- ;");
				} else {
					error(58);
				}
			}
		}
	}

	private void RULE_EXPRESSION() {
		System.out.println("--- RULE_EXPRESSION");
		RULE_X();

		while (tokens.get(currentToken).getValue().equals("||")) {
			currentToken++;
			System.out.println("--- ||");
			RULE_X();
		}
	}

	private void RULE_X() {
		System.out.println("---- RULE_X");
		RULE_Y();

		while (tokens.get(currentToken).getValue().equals("&&")) {
			currentToken++;
			System.out.println("---- &&");
			RULE_Y();
		}
	}

	private void RULE_Y() {
		System.out.println("----- RULE_Y");
		while (tokens.get(currentToken).getValue().equals("!")) {
			currentToken++;
			System.out.println("----- !");
		}
		RULE_R();
	}

	private void RULE_R() {
		System.out.println("------ RULE_R");
		RULE_E();

		while (true) {
			String currentVal = tokens.get(currentToken).getValue();
			if (currentVal.equals("<") || currentVal.equals(">")) {
				System.out.println("------ " + currentVal);
				currentToken++;
				RULE_E();
			} else if (currentVal.equals("==")) {
				System.out.println("------ ==");
				currentToken++;
				RULE_E();
			} else if (currentVal.equals("!=")) {
				System.out.println("------ !=");
				currentToken++;
				RULE_E();
			} else {
				break;
			}
		}
	}

	private void RULE_E() {
		System.out.println("------- RULE_E");
		RULE_A();

		while (tokens.get(currentToken).getValue().equals("+") ||
				tokens.get(currentToken).getValue().equals("-")) {
			System.out.println("------- " + tokens.get(currentToken).getValue());
			currentToken++;
			RULE_A();
		}
	}

	private void RULE_A() {
		System.out.println("-------- RULE_A");
		RULE_B();

		while (tokens.get(currentToken).getValue().equals("*") ||
				tokens.get(currentToken).getValue().equals("/")) {
			System.out.println("-------- " + tokens.get(currentToken).getValue());
			currentToken++;
			RULE_B();
		}
	}

	private void RULE_B() {
		System.out.println("--------- RULE_B");
		if (tokens.get(currentToken).getValue().equals("-")) {
			currentToken++;
			System.out.println("--------- -");
		}
		RULE_C();
	}

	private void RULE_C() {
		System.out.println("---------- RULE_C");
		if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
			if (currentToken + 1 < tokens.size() && tokens.get(currentToken + 1).getValue().equals("(")) {
				RULE_CALL_METHOD();
			} else {
				System.out.println("---------- IDENTIFIER: " + tokens.get(currentToken).getValue());
				currentToken++;
			}
		} else if (tokens.get(currentToken).getType().equals("INTEGER") ||
				tokens.get(currentToken).getType().equals("FLOAT") ||
				tokens.get(currentToken).getType().equals("CHAR") ||
				tokens.get(currentToken).getType().equals("STRING") ||
				(tokens.get(currentToken).getType().equals("KEYWORD") &&
						(tokens.get(currentToken).getValue().equals("true") ||
								tokens.get(currentToken).getValue().equals("false")))) {
			System.out.println("---------- LITERAL: " + tokens.get(currentToken).getValue());
			currentToken++;
		} else if (tokens.get(currentToken).getValue().equals("(")) {
			currentToken++;
			System.out.println("---------- (");
			RULE_EXPRESSION();
			if (tokens.get(currentToken).getValue().equals(")")) {
				currentToken++;
				System.out.println("---------- )");
			} else {
				error(59);
			}
		} else {
			error(60);
		}
	}

	private void RULE_TYPE() {
		System.out.println("----- RULE_TYPE");
		if (tokens.get(currentToken).getType().equals("KEYWORD") &&
				(tokens.get(currentToken).getValue().equals("int") ||
						tokens.get(currentToken).getValue().equals("float") ||
						tokens.get(currentToken).getValue().equals("void") ||
						tokens.get(currentToken).getValue().equals("char") ||
						tokens.get(currentToken).getValue().equals("string") ||
						tokens.get(currentToken).getValue().equals("boolean"))) {
			System.out.println("----- TYPE: " + tokens.get(currentToken).getValue());
			currentToken++;
		} else {
			error(61);
		}
	}

	private boolean isType() {
		return tokens.get(currentToken).getType().equals("KEYWORD") &&
				(tokens.get(currentToken).getValue().equals("int") ||
						tokens.get(currentToken).getValue().equals("float") ||
						tokens.get(currentToken).getValue().equals("void") ||
						tokens.get(currentToken).getValue().equals("char") ||
						tokens.get(currentToken).getValue().equals("string") ||
						tokens.get(currentToken).getValue().equals("boolean"));
	}

	private boolean isMethodDeclaration() {
		int savePos = currentToken;
		try {
			if (isType()) {
				currentToken++;
				if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
					currentToken++;
					return tokens.get(currentToken).getValue().equals("(");
				}
			}
			return false;
		} finally {
			currentToken = savePos;
		}
	}

	private boolean isAssignment() {
		if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
			if (currentToken + 1 < tokens.size() &&
					tokens.get(currentToken + 1).getValue().equals("=")) {
				return true;
			}
		}
		return false;
	}

	private boolean isMethodCall() {
		if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
			if (currentToken + 1 < tokens.size() &&
					tokens.get(currentToken + 1).getValue().equals("(")) {
				return true;
			}
		}
		return false;
	}

	private boolean isReturnStatement() {
		return tokens.get(currentToken).getType().equals("KEYWORD") &&
				tokens.get(currentToken).getValue().equals("return");
	}

	private boolean isWhileStatement() {
		return tokens.get(currentToken).getType().equals("KEYWORD") &&
				tokens.get(currentToken).getValue().equals("while");
	}

	private boolean isIfStatement() {
		return tokens.get(currentToken).getType().equals("KEYWORD") &&
				tokens.get(currentToken).getValue().equals("if");
	}

	private boolean isDoStatement() {
		return tokens.get(currentToken).getType().equals("KEYWORD") &&
				tokens.get(currentToken).getValue().equals("do");
	}

	private boolean isForStatement() {
		return tokens.get(currentToken).getType().equals("KEYWORD") &&
				tokens.get(currentToken).getValue().equals("for");
	}

	private boolean isSwitchStatement() {
		return tokens.get(currentToken).getType().equals("KEYWORD") &&
				tokens.get(currentToken).getValue().equals("switch");
	}

	private void error(int error) {
		System.out.println("Error " + error +
				" at line " + tokens.get(currentToken).getLineNumber() +
				", token: " + tokens.get(currentToken).getValue());
		System.exit(1);
	}
}