package de.pinguparty.geopingu.worker.exceptions;

public class InternalErrorException extends UserErrorException {
    public InternalErrorException(long chatId, String message) {
        super(chatId, String.format("An internal error occurred: %s", message));
    }
}
