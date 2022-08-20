package de.pinguparty.geopingu.worker.domain.formula;

public class IllegalFormulaSyntaxException extends RuntimeException {
    public IllegalFormulaSyntaxException(String message) {
        super(message);
    }
}
