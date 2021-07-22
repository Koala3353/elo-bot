package com.general_hello.commands.commands.DefaultCommands;


import com.general_hello.commands.Config;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.PrefixStoring;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import java.awt.*;

public class SayCommand implements ICommand {
    @Override
    public void handle(CommandContext e) {
        final long guildID = e.getGuild().getIdLong();
        String prefix = PrefixStoring.PREFIXES.computeIfAbsent(guildID, (id) -> Config.get("prefix"));

        if (!e.getMember().hasPermission(Permission.MANAGE_SERVER)) {
            e.getChannel().sendMessage("You don't have permission to use this command").queue();
            return;
        }
        if(e.getArgs().isEmpty()) {
            e.getChannel().sendMessage(getHelp(prefix)).queue();
            return;
        }

        if("embed".equals(e.getArgs().get(0))) {
                String[] tokens = e.getMessage().getContentRaw().split(" ");
                String content = "";

                for (int i = 0; i < tokens.length; i++) {
                    content += i == 0 || i == 1 || i == 2 ? "" : tokens[i] + " "; //Ignore first two tokens: =say and embed
                }

                e.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE);

                EmbedBuilder embedmsg = new EmbedBuilder();
                embedmsg.setColor(Color.red);

                embedmsg.setAuthor(e.getArgs().get(1), null, e.getAuthor().getAvatarUrl());
                embedmsg.setDescription(content);
                embedmsg.setFooter("Requested by " + e.getAuthor().getName(), null);
                e.getChannel().sendMessage(embedmsg.build()).queue();

            e.getMessage().delete().queue();
        }

        else
        {
            String[] tokens = e.getMessage().getContentRaw().split(" ");
            String content = "";

            for(int i = 0; i < tokens.length; i++) {
                content += i == 0 ? "" : tokens[i] + " "; //Ignore first token: =say
            }
            e.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE);

            e.getChannel().sendMessage(content).queue();

            e.getMessage().delete().queue();
        }
    }

    @Override
    public String getName() {
        return "say";
    }

    @Override
    public String getHelp(String prefix) {
        return "Bot says something for you.\n"
                + "Usage: `" + prefix + "say`\n"
                + "Parameter: `[Content] | embed [Content]`\n"
                + "[Content]: The sentence the bot says in normal message form.\n"
                + "embed [Content]: The sentence bot says in embed message form.\n"
                + "Support @mention(s): Anything.";
    }
}
