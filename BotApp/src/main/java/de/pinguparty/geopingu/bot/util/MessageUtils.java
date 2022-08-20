package de.pinguparty.geopingu.bot.util;

import de.pinguparty.geopingu.bot.util.Emoji;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Collection of utility methods for dealing with and creating messages.
 */
public class MessageUtils {

    public static final String KEYBOARD_BUTTON_DONE = "[Done]";
    public static final String KEYBOARD_BUTTON_CANCEL = "[Cancel]";

    /**
     * Escapes a given message string such that it can be safely published as part of a message.
     *
     * @param message The message string to escape
     * @return The escpaed message string
     */
    public static String escapeMessageString(String message) {
        //Sanity check
        if (message == null) throw new IllegalArgumentException("The message must not be null.");

        return message.trim()
                .replace("_", "\\_")
                .replace("*", "\\*")
                .replace("[", "\\[")
                .replace("`", "\\`");
    }

    /**
     * Creates a keyboard with buttons, represented by a {@link List} of button rows, where each row is
     * represented by a {@link List} of button names.
     *
     * @param buttonNames A {@link Collection} of button names from which the keyboard is supposed to be created
     * @param rowSize     The length of the resulting button rows
     * @return The resulting keyboard, represented by a {@link List} of button name {@link List}s.
     */
    public static List<List<String>> createKeyboard(Collection<?> buttonNames, int rowSize) {
        //Sanity check
        if ((buttonNames == null) || (buttonNames.isEmpty()))
            throw new IllegalArgumentException("The collection of button names must not be null or empty.");

        //Create lists for all button rows and for the current row
        List<List<String>> buttonRows = new ArrayList<>();
        List<String> currentButtonRow = new ArrayList<>();

        //Counter for buttons
        int buttonCounter = 0;

        //Iterate over all button names
        for (Object buttonName : buttonNames) {
            //Compare counter with row size
            if ((buttonCounter % rowSize == 0) && (buttonCounter > 0)) {
                //Add previous row to rows list and create new row
                buttonRows.add(currentButtonRow);
                currentButtonRow = new ArrayList<>();
            }

            //Add current button to current row
            currentButtonRow.add(buttonName.toString());

            //Increase counter
            buttonCounter++;
        }

        //Add final row to rows list as well
        buttonRows.add(currentButtonRow);

        //Return list of button rows
        return buttonRows;
    }
}
