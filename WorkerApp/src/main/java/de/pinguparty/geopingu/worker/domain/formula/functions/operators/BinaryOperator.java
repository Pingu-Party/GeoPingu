package de.pinguparty.geopingu.worker.domain.formula.functions.operators;

import de.pinguparty.geopingu.worker.domain.formula.functions.Function;

public abstract class BinaryOperator extends Function {
    public int getNumberOfArguments() {
        return 2;
    }
}
