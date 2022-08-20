package de.pinguparty.geopingu.worker.services.dispatcher;

import de.pinguparty.geopingu.bot.actions.error.ErrorMessageAction;
import de.pinguparty.geopingu.bot.messages.UserMessage;
import de.pinguparty.geopingu.bot.util.JSONConverter;
import de.pinguparty.geopingu.worker.config.MessagingConfig;
import de.pinguparty.geopingu.worker.exceptions.UserErrorException;
import de.pinguparty.geopingu.worker.interactions.Interaction;
import de.pinguparty.geopingu.worker.services.commander.BotCommander;
import de.pinguparty.geopingu.worker.services.geocache.GeocacheManager;
import de.pinguparty.geopingu.worker.services.registry.InteractionRegistry;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Service for retrieving incoming {@link UserMessage}s from the corresponding queue and dispatching them to the
 * {@link Interaction} that is responsible for handling them. This decision is based on two steps: First, it is
 * checked whether the received message constitutes a start command that is declared by one of the {@link Interaction}s.
 * If this is not the case, the {@link InteractionRegistry} is consulted in order to determine whether an
 * {@link Interaction} is registered as currently active for the chat to which the message was originally sent and thus
 * could handle it. In all other cases, the message is just ignored.
 */
@Service
public class UserMessageDispatcher {
    private static final String INTERACTIONS_PACKAGE = "de.pinguparty.geopingu.worker.interactions";
    private static final Map<String, Class<? extends Interaction>> COMMANDS_MAP = new HashMap<>();

    //Set of all available interaction classes
    private static Set<Class<? extends Interaction>> interactionClasses;

    static {
        //Initialize reflections
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages(INTERACTIONS_PACKAGE));
        //Find all available interaction classes
        interactionClasses = reflections.getSubTypesOf(Interaction.class);

        //Iterate over all Interaction subclasses
        for (Class<? extends Interaction> interactionClass : interactionClasses) {
            //Skip class if interface or abstract
            if (interactionClass.isInterface() || Modifier.isAbstract(interactionClass.getModifiers())) continue;

            //Add class to commands map (start command --> interaction)
            COMMANDS_MAP.put(Interaction.instantiateInteraction(interactionClass).getStartCommand().toLowerCase(Locale.ROOT), interactionClass);
        }
    }

    @Value("${worker.command.prefix}")
    private String commandPrefix;

    @Autowired
    private BotCommander botCommander;

    @Autowired
    private GeocacheManager geocacheManager;

    @Autowired
    private InteractionRegistry interactionRegistry;

    @RabbitListener(queues = {"${worker.queue.messages}"}, concurrency = MessagingConfig.CONSUMER_CONCURRENCY)
    public void receiveMessage(@Payload String payload) {
        //Sanity checks
        if ((payload == null) || payload.isEmpty()) {
            return;
        }

        //Create user message from payload
        UserMessage userMessage = JSONConverter.fromJSON(payload, UserMessage.class);

        //Sanity check
        if (userMessage == null) return;

        //Dispatch the user message
        try {
            dispatchUserMessage(userMessage);
        } catch (Exception exception) {
            //Handle all occurring exceptions
            handleException(exception, userMessage);
        }
    }

    /**
     * Dispatches a given {@link UserMessage} by forwarding it to the {@link Interaction} that is responsible
     * for handling it, either based on a matching start command or a corresponding
     * registration in the {@link InteractionRegistry}.
     *
     * @param userMessage The {@link UserMessage} to dispatch
     */
    private void dispatchUserMessage(UserMessage userMessage) throws Exception {
        //Sanity check
        if ((userMessage == null)) return;

        //Try to extract a command name from the message
        Optional<String> commandName = extractCommandName(userMessage);

        //Check if the message constitutes a known start command
        if ((commandName.isPresent()) && COMMANDS_MAP.containsKey(commandName.get())) {
            //Start command, create corresponding interaction to handle the message
            Interaction interaction = Interaction.instantiateInteraction(COMMANDS_MAP.get(commandName.get()));

            //Reset possibly registered interaction for the affected chat
            interactionRegistry.unregisterInteraction(userMessage.getChatID());

            //Let the interaction handle the received message
            interaction.handleMessage(botCommander, geocacheManager, interactionRegistry, userMessage);
            return;
        }

        /*
        Message does not represent a known start command, so a registered interaction may want to handle it
        */
        //Look up a possibly registered active interaction for the affected chat
        Optional<Interaction> interactionOptional = interactionRegistry.getActiveInteraction(userMessage.getChatID());

        //CHeck whether such an interaction exists
        if (interactionOptional.isEmpty()) return;

        //Let the currently active interaction handle the received user message
        interactionOptional.get().handleMessage(botCommander, geocacheManager, interactionRegistry, userMessage);
    }

    /**
     * Handles a given {@link Exception} that occurred while dispatching a given {@link UserMessage}.
     *
     * @param exception   The {@link Exception} to handle
     * @param userMessage THe {@link UserMessage} that led to the {@link Exception}
     */
    private void handleException(Exception exception, UserMessage userMessage) {
        //Check whether the exception is relevant to the user
        if (exception instanceof UserErrorException) {
            //Send a corresponding error message to the chat to which the message was sent
            botCommander.executeBotAction(((UserErrorException) exception).toAction());
            return;
        }

        /*
        Exception is not relevant to the user, handle it differently
         */
        System.err.printf("%s: %s%n", exception.getClass().getName(), exception.getMessage());
        exception.printStackTrace();
        botCommander.executeBotAction(new ErrorMessageAction(userMessage.getChatID(), "An internal error occurred."));
    }

    /**
     * Tries to extract a command name from the text payload of a given {@link UserMessage}. In case the text of
     * the message constitutes no command, the returned {@link Optional} will be empty.
     *
     * @param userMessage The {@link UserMessage} to extract the command from
     * @return The extracted command as {@link Optional}
     */
    private Optional<String> extractCommandName(UserMessage userMessage) {
        //Sanity check
        if (userMessage == null || (!userMessage.hasText())) return Optional.empty();

        //Extract text from message
        String messageText = userMessage.getText()
                .trim() //Remove leading and trailing spaces
                .replaceFirst("@[A-z0-9]+", ""); //Remove mention of the bot name in groups

        //Check whether message constitutes a command
        if (!messageText.startsWith(this.commandPrefix)) return Optional.empty();

        //Fix the message formatting to extract the command
        return Optional.of(messageText.replaceAll("\\s+", " ") //Replace multiple spaces
                .split(" ")[0] //Split at spaces to retrieve the first word
                .replaceFirst(this.commandPrefix, "") //Replace trailing prefix
                .toLowerCase(Locale.ROOT)); //Convert to lowercase
    }

    /**
     * Returns a {@link Set} of all available {@link Interaction} classes.
     *
     * @return The {@link Set} of {@link Interaction} classes
     */
    public static Set<Class<? extends Interaction>> getInteractionClasses() {
        return interactionClasses;
    }
}
