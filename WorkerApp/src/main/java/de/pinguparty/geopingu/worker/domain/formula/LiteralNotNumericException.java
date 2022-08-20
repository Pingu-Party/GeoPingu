package de.pinguparty.geopingu.worker.domain.formula;

public class LiteralNotNumericException extends RuntimeException {
    public LiteralNotNumericException(String message) {
        super(message);
    }
}
