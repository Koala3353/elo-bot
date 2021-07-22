package com.general_hello.commands.commands.DefaultCommands;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.Permission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LockQueueCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException, IOException {
        if (!ctx.getMember().hasPermission(Permission.MANAGE_PERMISSIONS)) {
            ctx.getChannel().sendMessage("You don't have the permission to use this command").queue();
            return;
        }

        if (Data.openQueue) {
            Data.openQueue = false;
            ctx.getChannel().sendMessage("All the queues successfully locked!").queue();

        } else {
            Data.openQueue = true;
            ctx.getChannel().sendMessage("All the queues successfully unlocked!").queue();

        }
        ctx.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);
    }

    @Override
    public String getName() {
        return "lock";
    }

    @Override
    public List<String> getAliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add("lq");
        aliases.add("unlock");
        aliases.add("uq");
        return aliases;
    }

    @Override
    public String getHelp(String prefix) {
        return "Locks/unlocks all the queues\n" +
                "Usage: `" + prefix + getName() + "`\n" +
                "Usage: `" + prefix + "unlock`";
    }
}
