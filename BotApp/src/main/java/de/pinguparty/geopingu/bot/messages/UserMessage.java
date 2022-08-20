package de.pinguparty.geopingu.bot.messages;

import de.pinguparty.geopingu.bot.telegram.TelegramBot;

import java.time.Instant;

/**
 * Model class for {@link UserMessage} that were received by a {@link TelegramBot} from users.
 */
public class UserMessage {
    private long chatID;
    private String username;
    private String firstName;
    private String lastName;
    private long userID;
    private Instant timestamp;
    private String text;

    /**
     * Creates a new, empty {@link UserMessage}.
     */
    public UserMessage() {

    }

    /**
     * Returns the ID of the chat to which the {@link UserMessage} was sent.
     *
     * @return The chat ID
     */
    public long getChatID() {
        return chatID;
    }

    /**
     * Sets the ID of the chat to which the {@link UserMessage} was sent.
     *
     * @param chatID The chat ID to set
     * @return The {@link UserMessage}
     */
    public UserMessage setChatID(long chatID) {
        this.chatID = chatID;
        return this;
    }

    /**
     * Returns the name of the user who sent the {@link UserMessage}.
     *
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the name of the user who sent the {@link UserMessage}.
     *
     * @param username The username to set
     * @return The {@link UserMessage}
     */
    public UserMessage setUsername(String username) {
        this.username = username;
        return this;
    }

    /**
     * Returns the first name of the user who sent the {@link UserMessage}.
     *
     * @return The first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user who sent the {@link UserMessage}.
     *
     * @param firstName The first name to set
     * @return The {@link UserMessage}
     */
    public UserMessage setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    /**
     * Returns the last name of the user who sent the {@link UserMessage}.
     *
     * @return The last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user who sent the {@link UserMessage}.
     *
     * @param lastName The last name to set
     * @return The {@link UserMessage}
     */
    public UserMessage setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    /**
     * Returns the ID of the user who sent the {@link UserMessage}.
     *
     * @return The user ID
     */
    public long getUserID() {
        return userID;
    }

    /**
     * Sets the ID of the user who sent the {@link UserMessage}.
     *
     * @param userID The user ID to set
     * @return The {@link UserMessage}
     */
    public UserMessage setUserID(long userID) {
        this.userID = userID;
        return this;
    }

    /**
     * Returns the timestamp describing when the {@link UserMessage} was received.
     *
     * @return The timestamp
     */
    public Instant getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp describing when the {@link UserMessage} was recevied.
     *
     * @param timestamp The timestamp to set
     * @return The {@link UserMessage}
     */
    public UserMessage setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    /**
     * Returns the text payload of the {@link UserMessage}.
     *
     * @return The text payload
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text payload of the {@link UserMessage}.
     *
     * @param text The text payload
     * @return The {@link UserMessage}
     */
    public UserMessage setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * Returns whether the payload of the {@link UserMessage} contains text.
     *
     * @return True, if the payload contains text; false otherwise
     */
    public boolean hasText() {
        return (this.text != null) && (!this.text.isEmpty());
    }

}
