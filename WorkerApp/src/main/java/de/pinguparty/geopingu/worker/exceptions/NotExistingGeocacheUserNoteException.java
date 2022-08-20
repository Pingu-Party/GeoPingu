package de.pinguparty.geopingu.worker.exceptions;

public class NotExistingGeocacheUserNoteException extends UserErrorException {

    private String geocacheId;

    public NotExistingGeocacheUserNoteException(long chatId, String geocacheId) {
        super(chatId, String.format("The geocache \"%s\" is not part of your collection.", geocacheId));
        this.geocacheId = geocacheId;
    }
}
