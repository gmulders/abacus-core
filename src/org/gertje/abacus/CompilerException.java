package org.gertje.abacus;

public class CompilerException extends AbacusException {
	public CompilerException(String message, int lineNumber, int columnNumber) {
		super(message, lineNumber, columnNumber);
	}
}