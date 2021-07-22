package com.general_hello.commands.commands.DefaultCommands;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;

import java.io.IOException;

public class AddELOCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException, IOException {
        if (!ctx.getMember().hasPermission(Permission.MANAGE_PERMISSIONS)) {
            ctx.getChannel().sendMessage("You don't have the permission to use this command").queue();
            return;
        }

        int elo;

        try {
            elo = Integer.parseInt(ctx.getArgs().get(0));
        } catch (Exception e) {
            ctx.getChannel().sendMessage("Kindly place the ammount to be given to the user").queue();
            return;
        }
        User member;

        try {
            member = ctx.getMessage().getMentionedUsers().get(0);
        } catch (Exception e) {
            ctx.getChannel().sendMessage("Kindly mention a member to give the ELO to.").queue();
            return;
        }

        Integer usersElo = Data.usersElo.get(member);

        if (usersElo==null) {
            Data.usersElo.put(member, 0);
            usersElo = Data.usersElo.get(member);
            Data.users.add(member);
        }

        Data.usersElo.put(member, usersElo + elo);
        ctx.getChannel().sendMessage("Successfully gave " + elo + " to " + member.getAsMention() + ". " + member.getName() + "'s ELO is now " + Data.usersElo.get(member)).queue();
    }

    @Override
    public String getName() {
        return "addelo";
    }

    @Override
    public String getHelp(String prefix) {
        return "Gives ELO to an user\n" +
                "Usage: `" + prefix + getName() + " [amount] [mentioned member]`";
    }
}
