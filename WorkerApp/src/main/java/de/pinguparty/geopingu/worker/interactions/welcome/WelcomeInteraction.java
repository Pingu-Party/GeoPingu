package de.pinguparty.geopingu.worker.interactions.welcome;

import de.pinguparty.geopingu.bot.actions.text.TextMessageAction;
import de.pinguparty.geopingu.bot.messages.UserMessage;
import de.pinguparty.geopingu.bot.util.Emoji;
import de.pinguparty.geopingu.worker.interactions.Interaction;
import de.pinguparty.geopingu.worker.services.commander.BotCommander;
import de.pinguparty.geopingu.worker.services.geocache.GeocacheManager;
import de.pinguparty.geopingu.worker.services.registry.InteractionRegistry;

/**
 * {@link Interaction} that outputs a welcome text to the users as soon as they start using the bot.
 */
public class WelcomeInteraction implements Interaction {
    /**
     * Returns a descriptive name of the interaction.
     *
     * @return The name
     */
    @Override
    public String getName() {
        return "Welcome";
    }

    /**
     * Returns the description of the interaction.
     *
     * @return The description
     */
    @Override
    public String getDescription() {
        return "Receive a warm welcome.";
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
        return "start";
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
     */
    @Override
    public void handleMessage(BotCommander botCommander, GeocacheManager geocacheManager, InteractionRegistry interactionRegistry, UserMessage userMessage) {
        //Extract chat ID
        long chatId = userMessage.getChatID();

        //Create text message action
        TextMessageAction action = new TextMessageAction(chatId, String.format("Hello! I am GeoPingu, a friendly penguin that loves geocaching. I would like to support you on your %s-tastic missions in the woods.", Emoji.PENGUIN));

        //Execute the action
        botCommander.executeBotAction(action);
    }
}
