package com.general_hello.commands;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.DefaultCommands.*;
import com.general_hello.commands.commands.ICommand;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {
    private final List<ICommand> commands = new ArrayList<>();
    public static ArrayList<String> commandNames = new ArrayList<>();
    public CommandManager(EventWaiter waiter) {

        //Default Commands
        addCommand(new SetPrefixCommand());
        addCommand(new PingCommand());
        addCommand(new ForceWinCommand());
        addCommand(new SendQueueCommand());
        addCommand(new HelpCommand(this));
        addCommand(new LockQueueCommand());
        addCommand(new RemoveELOCommand());
        addCommand(new CloseGameCommand());
        addCommand(new SayCommand());
        addCommand(new ResetELOAllCommand());
        addCommand(new LeaderboardCommand());
        addCommand(new ShowELOCommand());
        addCommand(new AddELOCommand());
        addCommand(new RegisterCommand());
    }

    private void addCommand(ICommand cmd) {
        boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));

        System.out.println("Loaded the <" + cmd.getName() + " -> " + cmd.getClass());
        if (nameFound) {
            throw new IllegalArgumentException("A command with this name is already present, " + cmd.getName() + " in " + cmd.getClass());
        }

        commands.add(cmd);
    }

    public List<ICommand> getCommands() {
        return commands;
    }

    @Nullable
    public ICommand getCommand(String search) {
        String searchLower = search.toLowerCase();

        for (ICommand cmd : this.commands) {
            if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                return cmd;
            }
        }

        return null;
    }


    void handle(GuildMessageReceivedEvent event, String prefix) throws InterruptedException, IOException {
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(prefix), "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke);

        if (cmd != null) {
            if (!Listener.count.containsKey(invoke)) {
                Listener.count.put(invoke, 1);
                commandNames.add(invoke);
            } else {
                Integer lastCount = Listener.count.get(invoke);
                Listener.count.put(invoke, lastCount + 1);

                if (!commandNames.contains(invoke)) commandNames.add(invoke);
            }

            event.getChannel().sendTyping().queue();
            List<String> args = Arrays.asList(split).subList(1, split.length);

            CommandContext ctx = new CommandContext(event, args);

            cmd.handle(ctx);

        }
    }
}
