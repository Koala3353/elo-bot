package com.general_hello.commands.commands.DefaultCommands;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import java.awt.*;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SendQueueCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException, IOException {
        if (!ctx.getMember().hasPermission(Permission.MANAGE_SERVER)) {
            ctx.getChannel().sendMessage("You don't have the permission to use this command").queue();
            return;
        }

        ctx.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);

        EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("Matchmaking").setTimestamp(OffsetDateTime.now()).setColor(randomColor());
        embedBuilder.setDescription("React 1️⃣ to queue for **1v1 realistic**\n" +
                "React to 2️⃣ to queue for **2v2 boxfights**").setFooter("Un-react to leave queue.");
        ctx.getChannel().sendMessage(embedBuilder.build()).queue((message -> {
            message.addReaction("1️⃣").queue();
            message.addReaction("2️⃣").queue();
            Data.queueMessageId.add(message.getIdLong());
        }));
    }

    public static Color randomColor() {
        Random colorpicker = new Random();
        int red = colorpicker.nextInt(255) + 1;
        int green = colorpicker.nextInt(255) + 1;
        int blue = colorpicker.nextInt(255) + 1;
        return new Color(red, green, blue);
    }

    @Override
    public String getName() {
        return "sendqueue";
    }

    @Override
    public List<String> getAliases() {
        List<String> list = new ArrayList<>();
        list.add("sq");
        list.add("createqueue");
        return list;
    }

    @Override
    public String getHelp(String prefix) {
        return "Sends a queue\n" +
                "Usage: `" + prefix + getName() + "`";
    }
}
