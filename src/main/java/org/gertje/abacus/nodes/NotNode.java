package org.gertje.abacus.nodes;

import org.gertje.abacus.token.Token;
import org.gertje.abacus.nodevisitors.NodeVisitor;
import org.gertje.abacus.nodevisitors.VisitingException;

public class NotNode extends AbstractNode {

	AbstractNode argument;

	/**
	 * Constructor
	 */
	public NotNode(AbstractNode argument, Token token) {
		super(2, token);

		this.argument = argument;
	}

	@Override
	public boolean getIsConstant() {
		return false;
	}

	@Override
	public Class<?> getType() {
		return Boolean.class;
	}

	@Override
	public <R, X extends VisitingException> R accept(NodeVisitor<R, X> visitor) throws X {
		return visitor.visit(this);
	}

	public AbstractNode getArgument() {
		return argument;
	}

	public void setArgument(AbstractNode argument) {
		this.argument = argument;
	}
}
