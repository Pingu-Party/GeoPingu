package de.pinguparty.geopingu.worker.interactions.formula;

import de.pinguparty.geopingu.bot.actions.text.TextMessageAction;
import de.pinguparty.geopingu.bot.messages.UserMessage;
import de.pinguparty.geopingu.worker.domain.GeocacheUserNote;
import de.pinguparty.geopingu.worker.domain.geocache.Geocache;
import de.pinguparty.geopingu.worker.exceptions.UserErrorException;
import de.pinguparty.geopingu.worker.interactions.Interaction;
import de.pinguparty.geopingu.worker.services.commander.BotCommander;
import de.pinguparty.geopingu.worker.services.geocache.GeocacheManager;
import de.pinguparty.geopingu.worker.services.registry.InteractionRegistry;

import java.util.stream.Collectors;

/**
 * {@link Interaction} that allows users to list all {@link Geocache}s of their collection.
 */
public class SetGeocacheFormulaInteraction implements Interaction {

    /**
     * Returns a descriptive name of the interaction.
     *
     * @return The name
     */
    @Override
    public String getName() {
        return "Set geocache formula";
    }

    /**
     * Returns the description of the interaction.
     *
     * @return The description
     */
    @Override
    public String getDescription() {
        return "Sets a formula for a geocache in your collection.";
    }

    /**
     * Returns whether the interaction is supposed to be added to the list of commands and their descriptions.
     *
     * @return True, if the interaction is supposed to be added to the commands list; false otherwise
     */
    @Override
    public boolean addToCommandsList() {
        return true;
    }

    /**
     * Returns the prefix-less command with which the interaction can be started.
     *
     * @return The start command
     */
    @Override
    public String getStartCommand() {
        return "list";
    }

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
    @Override
    public void handleMessage(BotCommander botCommander, GeocacheManager geocacheManager, InteractionRegistry interactionRegistry, UserMessage userMessage) throws UserErrorException {
        //Ignore non-text messages
        if (!userMessage.hasText()) return;

        //Extract chat ID
        long chatId = userMessage.getChatID();

        //Retrieve all geocache user notes of the user and generate an overview
        String geocacheOverview = geocacheManager.getGeocacheUserNotes(chatId).stream()
                .map(GeocacheUserNote::getGeocache)
                .map(g -> String.format("[%s] %s %s", g.getId(), g.getName(), g.getType().getIcon()))
                .collect(Collectors.joining("\n"));

        //Check whether the overview is empty
        if (geocacheOverview.isEmpty()) geocacheOverview = "(empty)";

        //Inform the user
        botCommander.executeBotAction(new TextMessageAction(chatId, String.format("The collection contains:\n%s", geocacheOverview)));
    }
}
