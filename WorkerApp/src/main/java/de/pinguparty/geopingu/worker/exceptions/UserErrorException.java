package de.pinguparty.geopingu.worker.exceptions;

import de.pinguparty.geopingu.bot.actions.error.ErrorMessageAction;

import java.time.Instant;

public abstract class UserErrorException extends Exception {

    private long chatId;
    private Instant timestamp;

    public UserErrorException(long chatId, String message) {
        super(message);
        setChatId(chatId);
        setTimestamp(Instant.now());
    }

    public ErrorMessageAction toAction() {
        return (ErrorMessageAction) new ErrorMessageAction().setText(this.getMessage()).setChatID(chatId);
    }

    public long getChatId() {
        return chatId;
    }

    private UserErrorException setChatId(long chatId) {
        this.chatId = chatId;
        return this;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    private UserErrorException setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
