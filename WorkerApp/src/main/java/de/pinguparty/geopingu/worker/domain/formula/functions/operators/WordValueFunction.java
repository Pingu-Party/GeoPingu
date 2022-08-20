package de.pinguparty.geopingu.worker.domain.formula.functions.operators;

import de.pinguparty.geopingu.worker.domain.formula.functions.Function;
import de.pinguparty.geopingu.worker.domain.formula.literals.Literal;

import java.util.List;

public class WordValueFunction extends Function {
    @Override
    public String getName() {
        return "bww";
    }

    @Override
    public int getNumberOfArguments() {
        return 1;
    }

    @Override
    public Literal evaluate(List<Literal> arguments) {
        //Sanity check
        if (arguments.size() != 1)
            throw new IllegalArgumentException("This function can only handle exactly one argument.");

        String evaluate = arguments.get(0).getString().replaceAll(" ", "").toLowerCase();
        int sum = 0;
        for(char c : evaluate.toCharArray()) {
            sum += c - 96;
        }

        return new Literal(sum);
    }
}
