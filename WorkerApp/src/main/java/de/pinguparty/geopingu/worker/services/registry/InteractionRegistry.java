package de.pinguparty.geopingu.worker.services.registry;

import de.pinguparty.geopingu.worker.interactions.Interaction;
import de.pinguparty.geopingu.worker.persistence.InteractionRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Component that is responsible for managing {@link InteractionRegistration}s, i.e. registrations of
 * {@link Interaction}s that are currently active within a certain chat and thus demand to receive all further
 * non-command messages of the users in this chat.
 * Only one {@link Interaction} can be active within a chat at the same time.
 */
@Component
public class InteractionRegistry {

    @Autowired
    private InteractionRegistrationRepository interactionRegistrationRepository;

    /**
     * Looks up and returns the {@link Interaction} that has previously been registered as active for a certain chat,
     * given by its chat ID . In case no such {@link Interaction} exists, the returned {@link Optional} will be empty.
     *
     * @param chatID The ID of the chat for which the registered {@link Interaction} is supposed to be retrieved
     * @return The currently active {@link Interaction}
     */
    public Optional<Interaction> getActiveInteraction(long chatID) {
        //Try to retrieve a corresponding registration from the repository
        Optional<InteractionRegistration> registrationOptional = interactionRegistrationRepository.findById(chatID);

        //Check whether the registration could be found
        if (registrationOptional.isEmpty()) return Optional.empty();

        //Extract the active interaction from the registration and wrap it
        return Optional.ofNullable(registrationOptional.get().getInteraction());
    }

    /**
     * Registers a given {@link Interaction} as active within a certain chat, given by its chat ID, so that all
     * further non-command messages of the users within this chat are forwarded to the {@link Interaction}.
     *
     * @param chatID      The ID of the affected chat
     * @param interaction The {@link Interaction} to register
     */
    public void registerInteraction(long chatID, Interaction interaction) {
        //Sanity check
        if (interaction == null) throw new IllegalArgumentException("The interaction must not be null.");

        //Create corresponding interaction registration
        InteractionRegistration registration = new InteractionRegistration(chatID, interaction);

        //Write to repository, i.e. upsert based on the chat ID
        interactionRegistrationRepository.save(registration);
    }

    /**
     * Unregisters a possibly previously registered {@link Interaction} from a certain chat, given by its chat ID, so
     * that all further non-commend messages of the users within this chat are not forwarded to
     * an {@link Interaction} anymore.
     *
     * @param chatID The ID of the chat for which the active {@link Interaction} is supposed to be unregistered
     */
    public void unregisterInteraction(long chatID) {
        //Remove any registration with the given chat ID from the repository
        interactionRegistrationRepository.deleteById(chatID);
    }
}
