package de.pinguparty.geopingu.bot.config;

import de.pinguparty.geopingu.bot.messages.UserMessage;
import de.pinguparty.geopingu.bot.telegram.TelegramBot;
import de.pinguparty.geopingu.bot.actions.BotAction;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for messaging-related aspects.
 */
@Configuration
public class MessagingConfig {
    //Maximum number of concurrent consumptions from queues
    public static final String CONSUMER_CONCURRENCY = "5";

    @Value("${bot.queue.messages}")
    private String userMessagesQueueName;

    @Value("${bot.queue.actions}")
    private String botActionsQueueName;

    /**
     * Creates a {@link Queue} for outgoing {@link UserMessage}s that were received by a {@link TelegramBot}.
     *
     * @return The resulting {@link Queue}
     */
    @Bean
    public Queue userMessagesQueue() {
        return new Queue(userMessagesQueueName, true);
    }

    /**
     * Creates a {@link Queue} for incoming {@link BotAction}s that are supposed to be executed by a
     * {@link TelegramBot}.
     *
     * @return The resulting {@link Queue}
     */
    @Bean
    public Queue botActionsQueue() {
        return new Queue(botActionsQueueName, true);
    }
}
