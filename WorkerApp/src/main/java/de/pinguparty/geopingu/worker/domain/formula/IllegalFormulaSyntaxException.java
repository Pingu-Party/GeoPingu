package de.pinguparty.geopingu.worker.domain.formula;

public class IllegalFormulaSyntaxException extends Exception {
    public IllegalFormulaSyntaxException(String message) {
        super(message);
    }
}
