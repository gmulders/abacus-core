package org.gertje.abacus.nodes;

import org.gertje.abacus.token.Token;
import org.gertje.abacus.nodevisitors.NodeVisitor;
import org.gertje.abacus.nodevisitors.VisitingException;
import org.gertje.abacus.types.Type;

/**
 * Node that represents a string.
 */
public class StringNode extends AbstractNode {

	private String value;

	/**
	 * Constructor
	 */
	public StringNode(String value, Token token) {
		super(1, token);

		this.value = value;
	}

	@Override
	public Type getType() {
		return Type.STRING;
	}

	@Override
	public boolean getIsConstant() {
		return true;
	}

	@Override
	public <R, X extends VisitingException> R accept(NodeVisitor<R, X> visitor) throws X {
		return visitor.visit(this);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
