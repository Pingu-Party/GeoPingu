package de.pinguparty.geopingu.worker.domain.formula.functions.operators;

import de.pinguparty.geopingu.worker.domain.formula.functions.Function;
import de.pinguparty.geopingu.worker.domain.formula.literals.Literal;

import java.util.List;

public class HelperFunction extends Function {
    @Override
    public String getName() {
        return "(";
    }

    @Override
    public int getNumberOfArguments() {
        return 0;
    }

    @Override
    public Literal evaluate(List<Literal> arguments) {
        return null;
    }
}
