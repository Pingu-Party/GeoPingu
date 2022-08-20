package de.pinguparty.geopingu.bot.actions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.pinguparty.geopingu.bot.telegram.TelegramBot;

/**
 * Model interface for bot actions.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BotAction {
    //Always required fields
    protected long chatID;

    /**
     * Creates a new empty {@link BotAction}.
     */
    public BotAction() {

    }

    /**
     * Creates a new {@link BotAction} from a given chat ID:
     *
     * @param chatID The chat ID to use
     */
    public BotAction(long chatID) {
        setChatID(chatID);
    }

    /**
     * Returns the chat ID of the {@link BotAction}.
     *
     * @return The chat ID
     */
    public long getChatID() {
        return chatID;
    }

    /**
     * Sets the chat ID of the {@link BotAction}.
     *
     * @param chatID The chat ID to set
     * @return The {@link BotAction}
     */
    public BotAction setChatID(long chatID) {
        this.chatID = chatID;
        return this;
    }

    /**
     * Returns the type name of the {@link BotAction}.
     *
     * @return The type name
     */
    @JsonProperty("type")
    public abstract String getTypeName();

    /**
     * Executes the action on a given {@link TelegramBot}.
     *
     * @param bot The {@link TelegramBot} on which the action is supposed to be performed.
     */
    @JsonIgnore
    public abstract void execute(TelegramBot bot);
}
