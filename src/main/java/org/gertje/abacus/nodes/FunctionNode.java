package org.gertje.abacus.nodes;

import org.gertje.abacus.nodevisitors.NodeVisitor;
import org.gertje.abacus.nodevisitors.VisitingException;
import org.gertje.abacus.token.Token;
import org.gertje.abacus.types.Type;

import java.util.List;

/**
 * Node that represents a function.
 */
public class FunctionNode extends AbstractNode {

	private String identifier;
	private List<Node> parameters;
	private Type returnType;

	/**
	 * Constructor
	 */
	public FunctionNode(String identifier, List<Node> parameters, Token token) {
		super(1, token);

		this.identifier = identifier;
		this.parameters = parameters;
	}

	@Override
	public Type getType() {
		return returnType;
	}

	@Override
	public boolean getIsConstant() {
		return false;
	}

	@Override
	public <R, X extends VisitingException> R accept(NodeVisitor<R, X> visitor) throws X {
		return visitor.visit(this);
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public List<Node> getParameters() {
		return parameters;
	}

	public void setParameters(List<Node> parameters) {
		this.parameters = parameters;
	}

	public Type getReturnType() {
		return returnType;
	}

	public void setReturnType(Type returnType) {
		this.returnType = returnType;
	}
}
