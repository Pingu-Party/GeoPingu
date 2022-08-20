package de.pinguparty.geopingu.worker.domain.formula.functions;

import de.pinguparty.geopingu.worker.domain.formula.FormulaElement;
import de.pinguparty.geopingu.worker.domain.formula.literals.Literal;

import java.util.List;

public abstract class Function implements FormulaElement {

    protected static final int MAX_PRECEDENCE = Integer.MAX_VALUE;

    public abstract String getName();

    public abstract int getNumberOfArguments();

    public abstract Literal evaluate(List<Literal> arguments);

    public int getPrecedence() {
        return MAX_PRECEDENCE;
    }

    public boolean isRightAssociative() {
        return false;
    }

    public String toString() {
        return this.getName();
    }
}
