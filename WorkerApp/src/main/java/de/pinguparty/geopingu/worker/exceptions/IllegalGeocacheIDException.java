package de.pinguparty.geopingu.worker.exceptions;

public class IllegalGeocacheIDException extends UserErrorException {

    private String invalidGeocacheId;

    public IllegalGeocacheIDException(long chatId, String invalidGeocacheId) {
        super(chatId, String.format("\"%s\" is not a valid GC code for a geocache.", invalidGeocacheId));
        this.invalidGeocacheId = invalidGeocacheId;
    }
}
