package edu.java.bot.messageProcessor;

import edu.java.bot.client.ScrapperClient;
import edu.java.bot.command.Command;
import edu.java.bot.command.HelpCommand;
import edu.java.bot.command.ListCommand;
import edu.java.bot.command.StartCommand;
import edu.java.bot.command.TrackCommand;
import edu.java.bot.command.UntrackCommand;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UserMessageProcessor implements MessageProcessor {

    static final String UNKNOWN_COMMAND = "неизвестная команда";

    private final Counter messageCounter;
    private final MeterRegistry meterRegistry;

    private final List<Command> commands = new ArrayList<>();

    public UserMessageProcessor(ScrapperClient client, MeterRegistry meterRegistry) {
        commands.addAll(List.of(
            new StartCommand(this, client),
            new HelpCommand(this, client),
            new TrackCommand(this, client),
            new UntrackCommand(this, client),
            new ListCommand(this, client)
        ));
        this.meterRegistry = meterRegistry;
        this.messageCounter = meterRegistry.counter("messages_processed_total");
    }

    @Override
    public List<? extends Command> commands() {
        return commands;
    }

    @Override
    public SendMessage process(Update update) {
        Optional<Command> command = commands.stream()
            .filter(c -> c.supports(update))
            .findFirst();
        messageCounter.increment();

        return command.map(value -> {
                    meterRegistry.counter(
                            "command_processed_total",
                            "command_type",
                            value.command()
                        )
                        .increment();
                    return value.handle(update);
                }
            )
            .orElseGet(() -> new SendMessage(update.getUpdateId().toString(), UNKNOWN_COMMAND));
    }
}
