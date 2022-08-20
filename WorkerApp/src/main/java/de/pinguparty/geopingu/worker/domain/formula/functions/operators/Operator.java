package de.pinguparty.geopingu.worker.domain.formula.functions.operators;

import de.pinguparty.geopingu.worker.domain.formula.functions.Function;

public abstract class Operator extends Function {

    public abstract int getPrecedence();
}
