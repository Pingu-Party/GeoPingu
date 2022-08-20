package de.pinguparty.geopingu.bot.messages;

import de.pinguparty.geopingu.bot.telegram.TelegramBot;
import de.pinguparty.geopingu.bot.util.JSONConverter;
import org.json.JSONObject;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.Instant;

/**
 * Service for handling incoming user messages that were received by a {@link TelegramBot} and are supposed
 * to be forwarded to the worker applications via the corresponding queue.
 */
@Service
public class UserMessagesHandler {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue userMessagesQueue;

    /**
     * Handles a given {@link Message} by transforming it to a {@link UserMessage} and forwarding it to the
     * corresponding queue.
     *
     * @param message The {@link Message} to handle
     */
    public void handleMessage(Message message) {
        //Sanity check
        if (message == null) return;

        //Create corresponding user message
        UserMessage userMessage = transformMessage(message);

        //Convert user message to JSON
        JSONObject messageJSON = JSONConverter.toJSON(userMessage);

        //Sanity check
        if (messageJSON.isEmpty()) return;

        //Send user message to queue
        rabbitTemplate.convertAndSend(userMessagesQueue.getName(), messageJSON.toString());
    }

    /**
     * Transforms a given {@link Message} to a {@link UserMessage}.
     *
     * @return The resulting {@link UserMessage}
     */
    private UserMessage transformMessage(Message message) {
        //Sanity check
        if (message == null) throw new IllegalArgumentException("The message must not be null.");

        //Create corresponding UserMessage
        UserMessage userMessage = new UserMessage()
                .setChatID(message.getChatId())
                .setUserID(message.getFrom().getId())
                .setTimestamp(Instant.ofEpochSecond(message.getDate()))
                .setUsername(message.getFrom().getUserName())
                .setFirstName(message.getFrom().getFirstName())
                .setLastName(message.getFrom().getLastName());

        //Check for text data
        if (message.hasText()) {
            userMessage.setText(message.getText());
        }

        //Return result
        return userMessage;
    }
}
