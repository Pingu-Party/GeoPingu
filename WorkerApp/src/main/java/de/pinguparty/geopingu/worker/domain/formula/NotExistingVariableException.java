package de.pinguparty.geopingu.worker.domain.formula;

public class NotExistingVariableException extends RuntimeException {
    public NotExistingVariableException(String message) {
        super(message);
    }
}
