package org.gertje.abacus.nodes;

import org.gertje.abacus.Token;
import org.gertje.abacus.nodevisitors.NodeVisitor;
import org.gertje.abacus.nodevisitors.VisitingException;
import org.gertje.abacus.symboltable.SymbolTable;

public class BooleanNode extends AbstractNode {

	private Boolean value;

	/**
	 * Constructor
	 */
	public BooleanNode(Boolean value, Token token, NodeFactory nodeFactory) {
		super(1, token, nodeFactory);

		this.value = value;
	}

	@Override
	public Boolean evaluate(SymbolTable sym) {
		return value;
	}

	@Override
	public BooleanNode analyse(SymbolTable sym) {
		// Deze node kunnen we niet eenvoudiger maken. Geef de huidige instantie terug.	
		return this;
	}

	@Override
	public Class<?> getType() {
		return Boolean.class;
	}

	@Override
	public boolean getIsConstant() {
		return true;
	}

	@Override
	public <R, X extends VisitingException> R accept(NodeVisitor<R, X> visitor) throws X {
		return visitor.visit(this);
	}

	public Boolean getValue() {
		return value;
	}

	public void setValue(Boolean value) {
		this.value = value;
	}
}
