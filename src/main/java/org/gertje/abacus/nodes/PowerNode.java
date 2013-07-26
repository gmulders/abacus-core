package org.gertje.abacus.nodes;

import java.math.BigDecimal;

import org.gertje.abacus.AnalyserException;
import org.gertje.abacus.EvaluationException;
import org.gertje.abacus.Token;
import org.gertje.abacus.nodevisitors.NodeVisitor;
import org.gertje.abacus.nodevisitors.VisitingException;
import org.gertje.abacus.symboltable.SymbolTable;

public class PowerNode extends AbstractNode {

	private AbstractNode base;
	private AbstractNode power;

	/**
	 * Constructor
	 */
	public PowerNode(AbstractNode base, AbstractNode power, Token token, NodeFactory nodeFactory) {
		super(4, token, nodeFactory);

		this.base = base;
		this.power = power;
	}

	@Override
	public BigDecimal evaluate(SymbolTable sym) throws EvaluationException {
		// Evalueer de expressies voor de basis en de macht.
		Number baseValue = (Number) base.evaluate(sym);
		Number powerValue = (Number) power.evaluate(sym);
		
		// Wanneer de basis of de macht leeg is, is het resultaat van deze expressie ook leeg.
		if (baseValue == null || powerValue == null) {
			return null;
		}
		
		return BigDecimal.valueOf(Math.pow(
				baseValue.doubleValue(), 
				powerValue.doubleValue()));
	}

	/**
	 * Controleert of de node correct is van syntax.
	 */
	private boolean checkTypes() {
		// Zowel de basis als de macht moet een getal zijn.
		return isNumber(base.getType()) && isNumber(power.getType());
	}
	
	@Override
	public AbstractNode analyse(SymbolTable sym) throws AnalyserException {
        // Vereenvoudig de nodes indien mogelijk.
		base = base.analyse(sym);
		power = power.analyse(sym);

		// Beide zijden moeten van het type 'number' zijn.
		if (!checkTypes()) {
			throw new AnalyserException("Expected two parameters of type 'number' to POWER-expression.", token);
		}

		// Wanneer beide zijden constant zijn kunnen we de node vereenvoudigen.
		if (base.getIsConstant() && power.getIsConstant()) {
			try {
				return nodeFactory.createFloatNode(evaluate(sym), token);
			} catch (EvaluationException e) {
				throw new AnalyserException(e.getMessage(), token);
			}
		}

		// Geef de huidige instantie terug.
		return this;
	}

	@Override
	public Class<?> getType() {
		return BigDecimal.class;
	}

	@Override
	public boolean getIsConstant() {
		return false;
	}

	@Override
	public <R, X extends VisitingException> R accept(NodeVisitor<R, X> visitor) throws X {
		return visitor.visit(this);
	}

	public AbstractNode getBase() {
		return base;
	}

	public void setBase(AbstractNode base) {
		this.base = base;
	}

	public AbstractNode getPower() {
		return power;
	}

	public void setPower(AbstractNode power) {
		this.power = power;
	}
}
