package com.general_hello.commands.commands.DefaultCommands;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.io.IOException;
import java.util.ArrayList;

public class ShowELOCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException, IOException {
        User user;

        if (ctx.getMessage().getMentionedUsers().isEmpty()) {
            user = ctx.getAuthor();
        } else {
            user = ctx.getMessage().getMentionedUsers().get(0);
        }
        EmbedBuilder embedBuilder = new EmbedBuilder().setColor(SendQueueCommand.randomColor()).setTitle(user.getName() + "'s Profile");

        Integer elo = Data.usersElo.get(user);

        if (Data.userDetails.containsKey(user)) {
            ArrayList<String> data = Data.userDetails.get(user);

            embedBuilder.setDescription("User: " + user.getName() + "\n" +
                    "ELO: " + elo + "\n" +
                    "Epic Name: " + data.get(0) + "\n" +
                    "Platform: " + data.get(1) + "\n" +
                    "Peripheral: " + data.get(2));
        } else {
            String details = "Epic Name: _Not registered_\n" +
                    "Platform: _Not registered_\n" +
                    "Peripheral: _Not registered_\n";

            embedBuilder.setDescription("User: " + user.getName() + "\n" +
                    details + "\n" +
                    "ELO: " + elo);
        }

        ctx.getChannel().sendMessage(embedBuilder.build()).queue();
    }

    @Override
    public String getName() {
        return "profile";
    }

    @Override
    public String getHelp(String prefix) {
        return "Shows your profile\n" +
                "Usage: `" + prefix + getName() + " <mention the member>`";
    }
}
