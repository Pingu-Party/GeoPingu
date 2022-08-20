package de.pinguparty.geopingu.worker.services.registry;

import de.pinguparty.geopingu.worker.interactions.Interaction;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents registrations of interactions that are currently active within a certain
 * chat and thus demand to receive all further non-command messages of the users in this chat.
 */
@Document
public class InteractionRegistration {
    @Id
    private long chatId;
    private Interaction interaction;

    /**
     * Creates a new, empty {@link InteractionRegistration}.
     */
    public InteractionRegistration() {

    }

    /**
     * Creates a new {@link InteractionRegistration} from the ID of the af the affected chat and the {@link Interaction}
     * to which all further non-command messages of this chat are supposed to be forwarded.
     *
     * @param chatId      The ID of the affected chat
     * @param interaction The active {@link Interaction}
     */
    public InteractionRegistration(long chatId, Interaction interaction) {
        setChatId(chatId);
        setInteraction(interaction);
    }

    /**
     * Returns the ID of the affected chat.
     *
     * @return The chat ID
     */
    public long getChatId() {
        return chatId;
    }

    /**
     * Sets the ID of the affected chat.
     *
     * @param chatId The chat ID
     * @return The {@link InteractionRegistration}
     */
    public InteractionRegistration setChatId(long chatId) {
        this.chatId = chatId;
        return this;
    }

    /**
     * Returns the currently active {@link Interaction} to which all further non-command messages of the chat are
     * supposed to be forwarded.
     *
     * @return The active {@link Interaction}
     */
    public Interaction getInteraction() {
        return interaction;
    }

    /**
     * Sets the currently active {@link Interaction} to which all further non-command messages of the chat are
     * supposed to be forwarded.
     *
     * @param interaction The active {@link Interaction} to set
     * @return THe {@link InteractionRegistration}
     */
    public InteractionRegistration setInteraction(Interaction interaction) {
        this.interaction = interaction;
        return this;
    }
}
