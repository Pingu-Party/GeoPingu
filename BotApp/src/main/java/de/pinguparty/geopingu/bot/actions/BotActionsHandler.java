package de.pinguparty.geopingu.bot.actions;

import de.pinguparty.geopingu.bot.config.MessagingConfig;
import de.pinguparty.geopingu.bot.telegram.TelegramBot;
import de.pinguparty.geopingu.bot.util.JSONConverter;
import org.json.JSONObject;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Service for handling incoming {@link BotAction}s which where received on the corresponding queue form the worker
 * applications and are supposed to be executed by a {@link TelegramBot}.
 */
@Service
public class BotActionsHandler {
    private static final String BOT_ACTIONS_PACKAGE = "de.pinguparty.geopingu.bot.actions";
    private static final Map<String, Class<? extends BotAction>> BOT_ACTION_MAP = new HashMap<>();
    private static final String JSON_ACTION_TYPE_KEY = "type";

    @Autowired
    private TelegramBot telegramBot;

    static {
        //Initialize reflections
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages(BOT_ACTIONS_PACKAGE));
        //Find all available bot action classes
        Set<Class<? extends BotAction>> botActionClasses = reflections.getSubTypesOf(BotAction.class);

        //Iterate over all Interaction subclasses
        for (Class<? extends BotAction> botActionClass : botActionClasses) {
            //Skip class if interface or abstract
            if (botActionClass.isInterface() || Modifier.isAbstract(botActionClass.getModifiers())) continue;

            //Add class to map of bot actions
            try {
                BOT_ACTION_MAP.put(botActionClass.getDeclaredConstructor().newInstance().getTypeName(), botActionClass);
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Listens to the actions queue and handles the incoming actions by executing them on a {@link TelegramBot}.
     *
     * @param payload The payload of the incoming action
     */
    @RabbitListener(queues = {"${bot.queue.actions}"}, concurrency = MessagingConfig.CONSUMER_CONCURRENCY)
    public void receiveAction(@Payload String payload) {
        //Sanity check
        if ((payload == null) || payload.isEmpty() || (telegramBot == null)) return;

        //Try to convert payload to JSON object
        JSONObject actionJSON;
        try {
            actionJSON = new JSONObject(payload);
        } catch (Exception e) {
            return;
        }

        //Extract type from payload
        String actionType = actionJSON.optString(JSON_ACTION_TYPE_KEY, null);

        //Check whether an action of this type exists
        if ((actionType == null) || (!BOT_ACTION_MAP.containsKey(actionType))) return;

        //Convert payload into object of the class corresponding to the action type
        BotAction botAction = JSONConverter.fromJSON(actionJSON, BOT_ACTION_MAP.get(actionType));

        //Sanity check
        if (botAction == null) return;

        //Execute the action
        botAction.execute(telegramBot);
    }
}
