package de.pinguparty.geopingu.worker.domain.formula.functions.operators;

import de.pinguparty.geopingu.worker.domain.formula.literals.Literal;
import java.util.List;

public class PlusOperator extends BinaryOperator{
    @Override
    public String getName() {
        return "+";
    }

    @Override
    public Literal evaluate(List<Literal> arguments) {
        return new Literal(arguments.stream().mapToDouble(Literal::getNumber).sum());
    }

    public int getPrecedence() {
        return 0;
    }
}
