package de.pinguparty.geopingu.worker.domain.formula.literals;

import de.pinguparty.geopingu.worker.domain.formula.FormulaElement;
import de.pinguparty.geopingu.worker.domain.formula.LiteralNotNumericException;

import java.util.Objects;

public class Literal implements FormulaElement {
    private String value = "";

    public Literal(double value) {
        setValue(value);
    }

    public Literal(String value) {
        setValue(value);
    }

    public String getString() {
        return value;
    }

    public double getNumber() {
        //Check if value can be converted to number
        if (!isNumber()) throw new LiteralNotNumericException(String.format("The value \"%s\" is not numeric.", value));

        //Convert value to double
        return Double.parseDouble(value);
    }

    public boolean isNumber() {
        try {
            Double.parseDouble(value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void setValue(double value) {
        this.value = Double.toString(value);
    }

    private void setValue(String value) {
        //Sanity check
        if (value == null) throw new IllegalArgumentException("The value must not be null.");

        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Literal literal = (Literal) o;
        return Objects.equals(value, literal.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public String toString() {
        return this.value;
    }
}
