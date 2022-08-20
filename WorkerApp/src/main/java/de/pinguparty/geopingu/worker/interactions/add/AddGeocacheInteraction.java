package de.pinguparty.geopingu.worker.interactions.add;

import de.pinguparty.geopingu.bot.actions.text.TextMessageAction;
import de.pinguparty.geopingu.bot.messages.UserMessage;
import de.pinguparty.geopingu.worker.domain.GeocacheUserNote;
import de.pinguparty.geopingu.worker.domain.geocache.Geocache;
import de.pinguparty.geopingu.worker.exceptions.IllegalGeocacheIDException;
import de.pinguparty.geopingu.worker.exceptions.InternalErrorException;
import de.pinguparty.geopingu.worker.exceptions.UserErrorException;
import de.pinguparty.geopingu.worker.interactions.Interaction;
import de.pinguparty.geopingu.worker.services.commander.BotCommander;
import de.pinguparty.geopingu.worker.services.geocache.GeocacheManager;
import de.pinguparty.geopingu.worker.services.registry.InteractionRegistry;
import de.pinguparty.geopingu.worker.utils.ValidationUtils;

import java.util.Locale;
import java.util.Optional;

/**
 * {@link Interaction} that allows users to add new {@link Geocache}s to their collection by creating
 * a corresponding {@link GeocacheUserNote}.
 */
public class AddGeocacheInteraction implements Interaction {

    //Indicates whether the geocache ID has already been requested from the user
    boolean geocacheIdRequested = false;

    /**
     * Returns a descriptive name of the interaction.
     *
     * @return The name
     */
    @Override
    public String getName() {
        return "Add Geocache";
    }

    /**
     * Returns the description of the interaction.
     *
     * @return The description
     */
    @Override
    public String getDescription() {
        return "Add a new geocache to your collection.";
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
        return "add";
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
            //Request the geocache ID from the user
            botCommander.executeBotAction(new TextMessageAction(chatId, "Please enter the GC code of the geocache that you want to to add to your collection."));
            geocacheIdRequested = true;

            //Register interaction as active to handle further messages
            interactionRegistry.registerInteraction(chatId, this);
            return;
        }

        //Extract the geocache ID message text
        String geocacheId = userMessage.getText().replaceAll(" ", "").trim().toUpperCase(Locale.ROOT);

        //Check whether the geocache ID is syntactically valid
        if (!ValidationUtils.isValidGeocacheID(geocacheId)) {
            throw new IllegalGeocacheIDException(chatId, geocacheId);
        }

        //Check whether this geocache has already been added by the user
        if (geocacheManager.getGeocacheUserNote(chatId, geocacheId).isPresent()) {
            //Inform the user
            botCommander.executeBotAction(new TextMessageAction(chatId, "This geocache is already part of your collection."));
            return;
        }

        //Geocache data
        Geocache geocache;

        //Retrieve data of this geocache if already available
        Optional<Geocache> retrievedGeocache = geocacheManager.getGeocache(geocacheId);
        if (retrievedGeocache.isPresent()) {
            geocache = retrievedGeocache.get();
        } else {
            //Fetch details about the geocache
            try {
                geocache = geocacheManager.getDetailsFetcher().fetchById(geocacheId);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
                e.printStackTrace();
                throw new InternalErrorException(chatId, "Failed to retrieve geocache details.");
            }

            //Update stored geocache data if necessary
            geocache = geocacheManager.updateGeocache(geocache);
        }

        //Create and save user note for this geocache
        geocacheManager.updateGeocacheUserNote(new GeocacheUserNote(chatId, geocache));

        //Send geocache details overview as bot reply
        System.out.println(geocache.toString());
        botCommander.executeBotAction(new TextMessageAction(chatId, geocache.toString()));
        botCommander.executeBotAction(new TextMessageAction(chatId, "The geocache was added to your collection."));
    }
}
