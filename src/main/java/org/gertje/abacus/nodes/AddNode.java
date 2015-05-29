package org.gertje.abacus.nodes;

import org.gertje.abacus.token.Token;
import org.gertje.abacus.nodevisitors.NodeVisitor;
import org.gertje.abacus.nodevisitors.VisitingException;

import java.math.BigDecimal;
import java.math.BigInteger;

public class AddNode extends AbstractNode implements BinaryOperationNode {

	private AbstractNode lhs;
	private AbstractNode rhs;

	/**
	 * Constructor
	 */
	public AddNode(AbstractNode lhs, AbstractNode rhs, Token token) {
		super (5, token);

		this.lhs = lhs;
		this.rhs = rhs;
	}

	@Override
	public Class<?> getType() {
		if (lhs.getType().equals(String.class)) {
			return String.class;
		}
		if (lhs.getType().equals(BigDecimal.class) || rhs.getType().equals(BigDecimal.class)) {
			return BigDecimal.class;
		}
		return BigInteger.class;
	}

	@Override
	public boolean getIsConstant() {
		return false;
	}

	@Override
	public <R, X extends VisitingException> R accept(NodeVisitor<R, X> visitor) throws X {
		return visitor.visit(this);
	}

	public AbstractNode getLhs() {
		return lhs;
	}

	public void setLhs(AbstractNode lhs) {
		this.lhs = lhs;
	}

	public AbstractNode getRhs() {
		return rhs;
	}

	public void setRhs(AbstractNode rhs) {
		this.rhs = rhs;
	}
}
