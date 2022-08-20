package de.pinguparty.geopingu.worker.domain.formula.functions.operators;

import de.pinguparty.geopingu.worker.domain.formula.functions.Function;

public abstract class UnaryOperator extends Function {
    public int getPrecedence() {
        return MAX_PRECEDENCE;
    }

    public int getNumberOfArguments() {
        return 1;
    }
}
