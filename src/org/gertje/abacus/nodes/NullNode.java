package org.gertje.abacus.nodes;

import org.gertje.abacus.AnalyserException;
import org.gertje.abacus.Token;
import org.gertje.abacus.symboltable.SymbolTableInterface;

public class NullNode extends AbstractNode {

	public NullNode(Token token, NodeFactoryInterface nodeFactory) {
		super(0, token, nodeFactory);
	}

	@Override
	public AbstractNode analyse(SymbolTableInterface sym) throws AnalyserException {
		return this;
	}

	@Override
	public Object evaluate(SymbolTableInterface sym) {
		return null;
	}

	@Override
	public String generateJavascript(SymbolTableInterface sym) {
		return "null";
	}

	@Override
	public boolean getIsConstant() {
		return true;
	}

	@Override
	public Class<?> getType() {
		return Object.class;
	}
}