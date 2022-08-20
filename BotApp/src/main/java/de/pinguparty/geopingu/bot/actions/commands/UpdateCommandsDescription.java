package de.pinguparty.geopingu.bot.actions.commands;

import de.pinguparty.geopingu.bot.telegram.TelegramBot;
import de.pinguparty.geopingu.bot.actions.BotAction;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.Map;
import java.util.stream.Collectors;


/**
 * A {@link BotAction} that allows to update the publicly listed descriptions of the available commands.
 */
public class UpdateCommandsDescription extends BotAction {

    //Map (command --> description) of command descriptions
    private Map<String, String> commandsDescriptions;

    /**
     * Returns the type name of the {@link BotAction}.
     *
     * @return The type name
     */
    @Override
    public String getTypeName() {
        return "UpdateCommands";
    }

    /**
     * Executes the action on a given {@link TelegramBot}.
     *
     * @param bot The {@link TelegramBot} on which the action is supposed to be performed.
     */
    @Override
    public void execute(TelegramBot bot) {
        //Sanity check
        if (commandsDescriptions == null) return;

        //Create method object
        SetMyCommands setMyCommands = new SetMyCommands();

        //Set commands and descriptions
        setMyCommands.setCommands(commandsDescriptions.keySet().stream()
                .map(c -> new BotCommand(c, commandsDescriptions.get(c)))
                .collect(Collectors.toList()));

        //Execute action
        bot.executeActionSafely(setMyCommands);
    }

    /**
     * Returns the map (command --> description) of command descriptions.
     *
     * @return The command descriptions
     */
    public Map<String, String> getCommandsDescriptions() {
        return commandsDescriptions;
    }

    /**
     * Sets the map (command --> description) of command descriptions.
     *
     * @param commandsDescriptions The command descriptions to set
     * @return The {@link UpdateCommandsDescription}
     */
    public UpdateCommandsDescription setCommandsDescriptions(Map<String, String> commandsDescriptions) {
        //Sanity check
        if (commandsDescriptions == null)
            throw new IllegalArgumentException("The map of commands descriptions must not be null.");

        this.commandsDescriptions = commandsDescriptions;
        return this;
    }
}
