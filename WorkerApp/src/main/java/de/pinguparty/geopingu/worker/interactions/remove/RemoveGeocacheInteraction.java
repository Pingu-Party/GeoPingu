package de.pinguparty.geopingu.worker.interactions.remove;

import de.pinguparty.geopingu.bot.actions.text.TextMessageAction;
import de.pinguparty.geopingu.bot.messages.UserMessage;
import de.pinguparty.geopingu.worker.domain.GeocacheUserNote;
import de.pinguparty.geopingu.worker.exceptions.IllegalGeocacheIDException;
import de.pinguparty.geopingu.worker.exceptions.NotExistingGeocacheUserNoteException;
import de.pinguparty.geopingu.worker.exceptions.UserErrorException;
import de.pinguparty.geopingu.worker.interactions.Interaction;
import de.pinguparty.geopingu.worker.services.commander.BotCommander;
import de.pinguparty.geopingu.worker.services.geocache.GeocacheManager;
import de.pinguparty.geopingu.worker.services.registry.InteractionRegistry;
import de.pinguparty.geopingu.worker.utils.GeocacheUtils;
import de.pinguparty.geopingu.bot.util.MessageUtils;
import de.pinguparty.geopingu.worker.domain.geocache.Geocache;

import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link Interaction} that allows users to delete {@link Geocache}s from their collection by deleting the
 * corresponding {@link GeocacheUserNote}.
 */
public class RemoveGeocacheInteraction implements Interaction {

    //Indicates whether the geocache ID has already been requested from the user
    boolean geocacheIdRequested = false;

    /**
     * Returns a descriptive name of the interaction.
     *
     * @return The name
     */
    @Override
    public String getName() {
        return "Remove Geocache";
    }

    /**
     * Returns the description of the interaction.
     *
     * @return The description
     */
    @Override
    public String getDescription() {
        return "Remove a geocache from your collection.";
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
        return "remove";
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

        //Check whether the geocache ID has already been requested
        if (!geocacheIdRequested) {
            //Retrieve all geocache user notes of the user and collect the GC codes
            List<List<String>> gcCodesList = geocacheManager.getGeocacheUserNotes(chatId)
                    .stream()
                    .map(GeocacheUserNote::getGeocache)
                    .map(g -> List.of(String.format("[%s] %s", g.getId(), g.getName())))
                    .collect(Collectors.toList());

            //Add cancel button
            gcCodesList.add(List.of(MessageUtils.KEYBOARD_BUTTON_CANCEL));

            //Request the geocache ID from the user
            botCommander.executeBotAction(new TextMessageAction(chatId, "Please select the geocache that should be removed from your collection.")
                    .setKeyboardButtons(gcCodesList));
            geocacheIdRequested = true;

            //Register interaction as active to handle further messages
            interactionRegistry.registerInteraction(chatId, this);
            return;
        }

        //Extract message
        String message = userMessage.getText().trim();

        //Check for cancel
        if (message.equalsIgnoreCase(MessageUtils.KEYBOARD_BUTTON_CANCEL)) {
            interactionRegistry.unregisterInteraction(chatId);
            return;
        }

        //Try to extract the geocache ID from the message
        String geocacheId = GeocacheUtils.findGeocacheId(message);

        //Check whether geocache ID could be extracted
        if (geocacheId == null) throw new IllegalGeocacheIDException(chatId, message);

        //Check whether a user note exists for this geocache and user
        if (geocacheManager.getGeocacheUserNote(chatId, geocacheId).isEmpty()) {
            throw new NotExistingGeocacheUserNoteException(chatId, geocacheId);
        }

        //Delete the corresponding user note
        geocacheManager.deleteGeocacheUserNote(chatId, geocacheId);

        //Inform the user
        botCommander.executeBotAction(new TextMessageAction(chatId, "The geocache was removed from your collection."));
    }
}
