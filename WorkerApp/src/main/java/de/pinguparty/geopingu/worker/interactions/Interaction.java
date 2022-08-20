package de.pinguparty.geopingu.worker.interactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.pinguparty.geopingu.bot.messages.UserMessage;
import de.pinguparty.geopingu.worker.exceptions.UserErrorException;
import de.pinguparty.geopingu.worker.services.commander.BotCommander;
import de.pinguparty.geopingu.worker.services.geocache.GeocacheManager;
import de.pinguparty.geopingu.worker.services.registry.InteractionRegistry;

import java.lang.reflect.InvocationTargetException;

/**
 * Base interface for all kinds of single- or multi-message interactions between the users and the bot that are started
 * by a command message.
 */
public interface Interaction {
    /**
     * Returns a descriptive name of the interaction.
     *
     * @return The name
     */
    String getName();

    /**
     * Returns the description of the interaction.
     *
     * @return The description
     */
    @JsonIgnore
    String getDescription();

    /**
     * Returns whether the interaction is supposed to be added to the list of commands and their descriptions.
     *
     * @return True, if the interaction is supposed to be added to the commands list; false otherwise
     */
    @JsonIgnore
    boolean addToCommandsList();

    /**
     * Returns the prefix-less command with which the interaction can be started.
     *
     * @return The start command
     */
    @JsonIgnore
    String getStartCommand();

    /**
     * Handles an incoming {@link UserMessage} that was forwarded to this {@link Interaction} by making use of
     * a {@link BotCommander} for performing actions with the bot, a {@link GeocacheManager} for managing
     * the stored data and a {@link InteractionRegistry} for controlling the forwarding of non-command messages
     * to the {@link Interaction}.
     *
     * @param botCommander        The {@link BotCommander} for performing actions with the bot
     * @param geocacheManager     The {@link GeocacheManager} for managing the stored data
     * @param interactionRegistry The {@link InteractionRegistry} for controlling message forwarding
     * @param userMessage         The {@link UserMessage} to handle
     * @throws UserErrorException In case of errors
     */
    @JsonIgnore
    void handleMessage(BotCommander botCommander, GeocacheManager geocacheManager, InteractionRegistry interactionRegistry, UserMessage userMessage) throws UserErrorException;

    /**
     * Instantiates an {@link Interaction} of a given {@link Class}. Returns null in case the instantiation failed.
     *
     * @param interactionClass The {@link Class} of the interaction to instantiate
     * @return The instantiated {@link Interaction} or null
     */
    static Interaction instantiateInteraction(Class<? extends Interaction> interactionClass) {
        //Sanity check
        if (interactionClass == null) return null;

        try {
            //Try to instantiate the interaction
            return interactionClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            return null;
        }
    }
}
