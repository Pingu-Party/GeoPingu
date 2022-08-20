package de.pinguparty.geopingu.bot.telegram;

import de.pinguparty.geopingu.bot.messages.UserMessagesHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * A {@link TelegramLongPollingBot} that allows to perform actions with the bot and handle incoming user messages.
 */
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${bot.telegram.token}")
    private String botToken;

    @Value("${bot.telegram.username}")
    private String botUsername;

    @Autowired
    private UserMessagesHandler userMessagesHandler;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        //Sanity checks
        if ((update == null) || (!update.hasMessage())) {
            return;
        }

        //Handle the message
        userMessagesHandler.handleMessage(update.getMessage());
    }

    /**
     * Executes a given {@link BotApiMethod} and handles possible exceptions.
     *
     * @param method The {@link BotApiMethod} to execute safely
     */
    public void executeActionSafely(BotApiMethod<?> method) {
        //Sanity check
        if (method == null) return;

        //Execute the given method an catch exceptions
        try {
            execute(method);
        } catch (TelegramApiException e) {
            //TODO fancy handling
            System.err.println(e.getClass().getSimpleName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
