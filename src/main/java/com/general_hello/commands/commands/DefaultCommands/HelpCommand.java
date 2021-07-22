package com.general_hello.commands.commands.DefaultCommands;

import com.general_hello.commands.CommandManager;
import com.general_hello.commands.Config;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.PrefixStoring;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.OffsetDateTime;
import java.util.List;

public class HelpCommand implements ICommand {

    private final CommandManager manager;

    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();
        final long guildID = ctx.getGuild().getIdLong();
        String prefix = PrefixStoring.PREFIXES.computeIfAbsent(guildID, (id) -> Config.get("prefix"));

        if (args.isEmpty()) {
            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setTitle("Groups");
            embedBuilder.setColor(SendQueueCommand.randomColor());
            embedBuilder.addField("❓ | Info (6)", "Shows some information about the bot", false);
            embedBuilder.addField("😎 | Administrators (8)", "Shows commands that starts queues etc.", false);
            embedBuilder.addField("📜 | Owner (2)", "Commands that controls the database", false);

            embedBuilder.setFooter("Type " + prefix + "help [group name] to see their commands");


            channel.sendMessage(embedBuilder.build()).queue();
            return;
        }

        if (args.get(0).equalsIgnoreCase("info")) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Bot's info");
            embedBuilder.setColor(SendQueueCommand.randomColor());
            embedBuilder.addField("1.) Profile Command", "`" + prefix + "profile`", false);
            embedBuilder.addField("2.) Register Command", "`" + prefix + "register`", false);
            embedBuilder.addField("3.) Set Prefix Command **ADMIN**", "`" + prefix + "setprefix`", false);
            embedBuilder.addField("4.) Ping Command", "`" + prefix + "ping`", false);
            embedBuilder.addField("5.) Help Command", "`" + prefix + "help`", false);
            embedBuilder.addField("6.) Leaderboard Command", "`" + prefix + "leaderboard`", false);

            embedBuilder.setFooter("\nType " + prefix + "help [command name] to see what they do");

            channel.sendMessage(embedBuilder.build()).queue();
            return;
        }

        if (args.get(0).equalsIgnoreCase("owner")) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("About Managing the bot Commands OWNER ONLY");
            embedBuilder.setColor(SendQueueCommand.randomColor());
            embedBuilder.addField("1.) Reset ELO command ","`" + prefix + "resetelo`", false);
            embedBuilder.addField("2.) Shutdown command ","`" + prefix + "shutdown`", false);

            embedBuilder.setFooter("\nType " + prefix + "help [command name] to see what they do");

            channel.sendMessage(embedBuilder.build()).queue();
            return;
        }

        if (args.get(0).equalsIgnoreCase("admin") || args.get(0).equalsIgnoreCase("administrators")) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Moderation Commands");
            embedBuilder.setColor(SendQueueCommand.randomColor());
            embedBuilder.addField("1.) Send the queue message command ","`" + prefix + "sendqueue`", false);
            embedBuilder.addField("2.) Locks the queue", "`" + prefix + "lock`", false);
            embedBuilder.addField("3.) Unlocks the queue", "`" + prefix + "unlock`", false);
            embedBuilder.addField("4.) Close game command", "`" + prefix + "close`", false);
            embedBuilder.addField("5.) Force win command", "`" + prefix + "forcewin`", false);
            embedBuilder.addField("6.) Remove ELO command", "`" + prefix + "removeelo`", false);
            embedBuilder.addField("7.) Add ELO command", "`" + prefix + "addelo`", false);
            embedBuilder.addField("7.) Say command", "`" + prefix + "say`", false);

            embedBuilder.setFooter("\nType " + prefix + "help [command name] to see what they do");

            channel.sendMessage(embedBuilder.build()).queue();
            return;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();

        String search = args.get(0);
        ICommand command = manager.getCommand(search);

        if (command == null) {
            channel.sendMessage("Nothing found for " + search).queue();
            return;
        }

        embedBuilder.setTitle("Help!!!");
        embedBuilder.setColor(SendQueueCommand.randomColor());
        embedBuilder.setDescription(command.getHelp(prefix));
        embedBuilder.setTimestamp(OffsetDateTime.now());
        channel.sendMessage(embedBuilder.build()).queue();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp(String prefix) {
        return "Shows the list with commands in the bot\n" +
                "Usage: `" + prefix + "help [command]`";
    }

    @Override
    public List<String> getAliases() {
        List<String> strings = new java.util.ArrayList<>();
        strings.add("commands");
        strings.add("cmds");
        strings.add("commandlist");
        return strings;
    }
}
