package de.pinguparty.geopingu.worker.domain.formula.functions.operators;

import de.pinguparty.geopingu.worker.domain.formula.literals.Literal;
import java.util.List;

public class MultiplicationOperator extends BinaryOperator {
    @Override
    public String getName() {
        return "*";
    }

    @Override
    public Literal evaluate(List<Literal> arguments) {
        //Sanity check
        if (arguments.size() != 2)
            throw new IllegalArgumentException("This operator can only handle exactly two arguments.");

        return new Literal(arguments.get(0).getNumber() * arguments.get(1).getNumber());
    }

    public int getPrecedence() {
        return 1;
    }
}
