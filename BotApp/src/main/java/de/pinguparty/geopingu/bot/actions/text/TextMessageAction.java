package de.pinguparty.geopingu.bot.actions.text;

import de.pinguparty.geopingu.bot.telegram.TelegramBot;
import de.pinguparty.geopingu.bot.actions.BotAction;
import de.pinguparty.geopingu.bot.util.MessageUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A {@link BotAction} that allows to send a text message to a certain chat, given by its chat ID.
 */
public class TextMessageAction extends BotAction {

    private String text = "";
    private boolean notify = true;
    private boolean isHTML = false;
    private List<List<String>> keyboardButtonNames;
    private boolean isOneTimeKeyboard = true;
    private boolean selectiveKeyboard = false;
    private boolean removeKeyboard = false;

    /**
     * Creates a new, empty {@link TextMessageAction}.
     */
    public TextMessageAction() {

    }

    /**
     * Creates a new {@link TextMessageAction} from a given chat ID and a message text.
     *
     * @param chatId The ID of the chat to which the message is supposed to be sent
     * @param text   The message text
     */
    public TextMessageAction(long chatId, String text) {
        super();
        setChatID(chatId);
        setText(text);
    }

    /**
     * Returns the type name of the {@link BotAction}.
     *
     * @return The type name
     */
    @Override
    public String getTypeName() {
        return "TextMessage";
    }

    /**
     * Executes the action on a given {@link TelegramBot}.
     *
     * @param bot The {@link TelegramBot} on which the action is supposed to be performed.
     */
    @Override
    public void execute(TelegramBot bot) {
        //Sanity check
        if (bot == null) return;

        //Create and configure object for sending a text message
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(Long.toString(this.chatID));
        sendMessage.setText(this.text);
        sendMessage.setDisableNotification(!notify);
        if (this.isHTML) {
            sendMessage.enableHtml(true);
        } else {
            sendMessage.enableMarkdown(true);
        }

        //Check whether a keyboard is supposed to be created
        if (keyboardButtonNames != null) {
            //Create markup for keyboard
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
            keyboardMarkup.setOneTimeKeyboard(isOneTimeKeyboard);

            //Set the selective flag
            keyboardMarkup.setSelective(selectiveKeyboard);

            //Create keyboard buttons from the names
            List<KeyboardRow> keyboardButtons = keyboardButtonNames.stream()
                    .map(r -> r.stream().map(KeyboardButton::new).collect(Collectors.toList()))
                    .map(KeyboardRow::new)
                    .collect(Collectors.toList());

            //Add buttons to markup and markup to action
            keyboardMarkup.setKeyboard(keyboardButtons);
            sendMessage.setReplyMarkup(keyboardMarkup);
        } else if (removeKeyboard) {
            //Remove the keyboard
            sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        }

        //Execute the action
        bot.executeActionSafely(sendMessage);
    }

    /**
     * Returns the text that is supposed to be sent.
     *
     * @return The text to send
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text that is supposed to be sent.
     *
     * @param text The text to send
     * @return The {@link TextMessageAction}
     */
    public TextMessageAction setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * Returns whether the receiver of the text is supposed to be notified.
     *
     * @return True, if the receiver is notified; false otherwise
     */
    public boolean isNotify() {
        return notify;
    }

    /**
     * Sets whether the receiver of the text is supposed to be notified.
     *
     * @param notify True, if the receiver should be notified; false otherwise
     * @return The {@link TextMessageAction}
     */
    public TextMessageAction setNotify(boolean notify) {
        this.notify = notify;
        return this;
    }

    /**
     * Returns whether HTML is supported within the given text instead of Markdown.
     *
     * @return True, if HTML is supported; false otherwise
     */
    public boolean isHTML() {
        return isHTML;
    }

    /**
     * Sets whether HTML is supported within the given text instead of Markdown.
     *
     * @param HTML True, if HTML should be supported; false otherwise
     * @return THe {@link TextMessageAction}
     */
    public TextMessageAction setHTML(boolean HTML) {
        isHTML = HTML;
        return this;
    }

    /**
     * Returns the names of the keyboard buttons as {@link List} of button rows. If no keyboard is supposed
     * to be created, null is returned.
     *
     * @return The keyboard button names
     */
    public List<List<String>> getKeyboardButtonNames() {
        return keyboardButtonNames;
    }

    /**
     * Sets the names of the keyboard buttons as {@link List} of button rows. If no keyboard is supposed to be created,
     * null must be passed instead.
     *
     * @param keyboardButtonNames The keyboard button names to set
     * @return The {@link TextMessageAction}
     */
    public TextMessageAction setKeyboardButtons(List<List<String>> keyboardButtonNames) {
        this.keyboardButtonNames = keyboardButtonNames;
        return this;
    }

    /**
     * Returns whether the keyboard is supposed to disappear after the first use.
     *
     * @return True, if the keyboard should disappear after the first use; false otherwise
     */
    public boolean isOneTimeKeyboard() {
        return isOneTimeKeyboard;
    }

    /**
     * Sets whether the keyboard is supposed to disappear after the first use.
     *
     * @param oneTimeKeyboard True, if the keyboard should disappear after the first use; false otherwise
     * @return The {@link TextMessageAction}
     */
    public TextMessageAction setOneTimeKeyboard(boolean oneTimeKeyboard) {
        isOneTimeKeyboard = oneTimeKeyboard;
        return this;
    }

    /**
     * Returns whether the keyboard is supposed to be displayed to certain users only.
     *
     * @return True, if the keyboard should be displayed only to certain users; false otherwise
     */
    public boolean isSelectiveKeyboard() {
        return selectiveKeyboard;
    }

    /**
     * Sets whether the keyboard is supposed to be displayed to certain users only.
     *
     * @param selectiveKeyboard True, if the keyboard should be displayed only to certain users; false otherwise
     * @return The {@link TextMessageAction}
     */
    public TextMessageAction setSelectiveKeyboard(boolean selectiveKeyboard) {
        this.selectiveKeyboard = selectiveKeyboard;
        return this;
    }

    /**
     * Returns whether the keyboard is supposed to be removed.
     *
     * @return True, if the keyboard is supposed to be removed; false otherwise
     */
    public boolean isRemoveKeyboard() {
        return removeKeyboard;
    }

    /**
     * Sets whether the keyboard is supposed to be removed.
     *
     * @param removeKeyboard True, if the keyboard is supposed to be removed; false otherwise
     * @return The {@link TextMessageAction}
     */
    public TextMessageAction setRemoveKeyboard(boolean removeKeyboard) {
        this.removeKeyboard = removeKeyboard;
        return this;
    }
}
