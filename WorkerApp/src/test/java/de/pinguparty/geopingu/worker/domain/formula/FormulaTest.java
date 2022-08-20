package de.pinguparty.geopingu.worker.domain.formula;

import de.pinguparty.geopingu.worker.domain.formula.literals.Literal;
import de.pinguparty.geopingu.worker.domain.formula.variables.Variable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FormulaTest {

    void testParsing1() {
        Formula formula = new Formula("N48 4[B+D].[F/C][G/B][D+A] E8 5[A-B].[E-8][A][E]");
        assertFalse(formula.allVariablesSet());
        assertEquals(7, formula.getVariables().size());
        assertNotNull(formula.getVariable("A"));
        assertNotNull(formula.getVariable("B"));
        assertNotNull(formula.getVariable("C"));
        assertNotNull(formula.getVariable("D"));
        assertNotNull(formula.getVariable("E"));
        assertNotNull(formula.getVariable("F"));
        assertNotNull(formula.getVariable("G"));
        assertThrows(NotExistingVariableException.class, () -> formula.getVariable("N"));

        formula.setVariable("A", new Literal(8));
        formula.setVariable("B", new Literal(2));
        formula.setVariable("C", new Literal(3));
        formula.setVariable("D", new Literal(1));
        formula.setVariable("E", new Literal(8));
        formula.setVariable("F", new Literal(6));
        formula.setVariable("G", new Literal(8));

        assertTrue(formula.allVariablesSet());

        assertEquals("N48 43.249 E8 56.088", formula.evaluate());
    }

    void testParsing2() {
        Formula formula = new Formula("N48 43.333 E008 52.[A+145]");
        assertFalse(formula.allVariablesSet());
        assertEquals(1, formula.getVariables().size());
        assertNotNull(formula.getVariable("A"));
        assertThrows(NotExistingVariableException.class, () -> formula.getVariable("N"));

        formula.setVariable("A", new Literal(235));

        assertTrue(formula.allVariablesSet());

        assertEquals("N48 43.333 E008 52.380", formula.evaluate());
    }

    void testParsing3() {
        Formula formula = new Formula("N48 43.333 E008 52.[BWW(A#B) + 25]");
        assertFalse(formula.allVariablesSet());
        assertEquals(2, formula.getVariables().size());
        assertNotNull(formula.getVariable("A"));
        assertNotNull(formula.getVariable("B"));
        assertThrows(NotExistingVariableException.class, () -> formula.getVariable("N"));

        formula.setVariable("A", new Literal("hallo"));
        formula.setVariable("B", new Literal("suz"));

        assertTrue(formula.allVariablesSet());

        assertEquals("N48 43.333 E008 52.139", formula.evaluate());
    }

    void testParsing4() {
        Formula formula = new Formula("N48 43.333 E008 52.111");
        assertEquals(0, formula.getVariables().size());
        assertTrue(formula.allVariablesSet());

        assertEquals("N48 43.333 E008 52.111", formula.evaluate());
    }

    @Test
    void testIllegalSyntax1() {
        assertThrows(IllegalFormulaSyntaxException.class, () -> new Formula("N48 43.333 E008 52.[A+B"));
    }
}