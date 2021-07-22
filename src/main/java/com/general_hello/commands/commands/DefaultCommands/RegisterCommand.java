package com.general_hello.commands.commands.DefaultCommands;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;

import java.io.IOException;

public class RegisterCommand implements ICommand
{
    @Override
    public void handle(CommandContext ctx) throws InterruptedException, IOException {
        if (Data.progressInQuestioning.containsKey(ctx.getAuthor())) {
            if (Data.progressInQuestioning.get(ctx.getAuthor()).equals(100)) {
                ctx.getChannel().sendMessage("You already registered an account!").queue();
                return;
            }

            ctx.getChannel().sendMessage("You are currently registering an account!").queue();
            return;
        }

        Data.progressInQuestioning.put(ctx.getAuthor(), 1);
        ctx.getChannel().sendMessage("What is your Epic Name?").queue();
    }

    @Override
    public String getName() {
        return "register";
    }

    @Override
    public String getHelp(String prefix) {
        return "Registers the user to the database.\n" +
                "Usage: `" + prefix + getName() + "`";
    }
}
