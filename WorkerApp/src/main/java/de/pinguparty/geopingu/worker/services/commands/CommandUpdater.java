package de.pinguparty.geopingu.worker.services.commands;

import de.pinguparty.geopingu.bot.actions.commands.UpdateCommandsDescription;
import de.pinguparty.geopingu.worker.interactions.Interaction;
import de.pinguparty.geopingu.worker.services.commander.BotCommander;
import de.pinguparty.geopingu.worker.services.dispatcher.UserMessageDispatcher;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CommandUpdater implements SmartLifecycle {

    @Value("${worker.command.prefix}")
    private String commandPrefix;

    @Autowired
    private BotCommander botCommander;

    private boolean running = false;

    /**
     * Start this component.
     * <p>Should not throw an exception if the component is already running.
     * <p>In the case of a container, this will propagate the start signal to all
     * components that apply.
     *
     * @see SmartLifecycle#isAutoStartup()
     */
    @Override
    public void start() {
        Map<String, String> commandDescriptions = UserMessageDispatcher.getInteractionClasses().stream()
                .map(Interaction::instantiateInteraction).filter(Objects::nonNull)
                .filter(Interaction::addToCommandsList)
                .collect(Collectors.toMap(i -> commandPrefix + i.getStartCommand(), Interaction::getDescription));
        botCommander.executeBotAction(new UpdateCommandsDescription().setCommandsDescriptions(commandDescriptions));
        this.running = true;
    }

    /**
     * Stop this component, typically in a synchronous fashion, such that the component is
     * fully stopped upon return of this method. Consider implementing {@link SmartLifecycle}
     * and its {@code stop(Runnable)} variant when asynchronous stop behavior is necessary.
     * <p>Note that this stop notification is not guaranteed to come before destruction:
     * On regular shutdown, {@code Lifecycle} beans will first receive a stop notification
     * before the general destruction callbacks are being propagated; however, on hot
     * refresh during a context's lifetime or on aborted refresh attempts, a given bean's
     * destroy method will be called without any consideration of stop signals upfront.
     * <p>Should not throw an exception if the component is not running (not started yet).
     * <p>In the case of a container, this will propagate the stop signal to all components
     * that apply.
     *
     * @see SmartLifecycle#stop(Runnable)
     * @see DisposableBean#destroy()
     */
    @Override
    public void stop() {
        this.running = false;
    }

    /**
     * Check whether this component is currently running.
     * <p>In the case of a container, this will return {@code true} only if <i>all</i>
     * components that apply are currently running.
     *
     * @return whether the component is currently running
     */
    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }
}
