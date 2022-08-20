package de.pinguparty.geopingu.worker.domain.formula.variables;

import de.pinguparty.geopingu.worker.domain.formula.FormulaElement;
import de.pinguparty.geopingu.worker.domain.formula.literals.Literal;

import java.util.Objects;

public class Variable implements FormulaElement {
    private String name;
    private Literal value = null;

    public Variable(String name) {
        setName(name);
    }

    public Variable(String name, Literal value) {
        this(name);
        setValue(value);
    }

    public String getName() {
        return name;
    }

    private Variable setName(String name) {
        //Sanity check
        if ((name == null) || name.isEmpty()) throw new IllegalArgumentException("The name must not be null or empty.");

        this.name = name;
        return this;
    }

    public Literal getValue() {
        return value;
    }

    public Variable setValue(Literal value) {
        this.value = value;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return getName().equals(variable.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    public String toString() {
        return getName();
    }
}
