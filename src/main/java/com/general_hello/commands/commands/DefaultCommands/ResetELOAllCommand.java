package com.general_hello.commands.commands.DefaultCommands;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.Permission;

import java.io.IOException;
import java.util.HashMap;

public class ResetELOAllCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException, IOException {
        if (!ctx.getMember().hasPermission(Permission.MANAGE_PERMISSIONS)) {
            ctx.getChannel().sendMessage("You don't have the permission to use this command").queue();
            return;
        }

        Data.usersElo = new HashMap<>();
        ctx.getChannel().sendMessage("Successfully reset all the user's ELO").queue();
    }

    @Override
    public String getName() {
        return "resetelo";
    }

    @Override
    public String getHelp(String prefix) {
        return "Resets **ALL** the ELO of users.\n" +
                "Usage: `" + prefix + getName() + "`";
    }
}
