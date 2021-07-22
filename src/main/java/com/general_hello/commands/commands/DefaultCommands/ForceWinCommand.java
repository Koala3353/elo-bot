package com.general_hello.commands.commands.DefaultCommands;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ForceWinCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException, IOException {
        if (!ctx.getMember().hasPermission(Permission.MANAGE_PERMISSIONS)) {
            ctx.getChannel().sendMessage("You don't have the permission to use this command").queue();
            return;
        }

        String oneOrTwo = ctx.getArgs().get(0);
        Member member;

        try {
            member = ctx.getMessage().getMentionedMembers().get(0);
        } catch (Exception e) {
            ctx.getChannel().sendMessage("Kindly mention the member you want to force win!").queue();
            return;
        }
        Member secondMember = null;
        Member firstMember = null;
        boolean pop = false;

        if (oneOrTwo.equalsIgnoreCase("1")) {
            if (Data.firstEmojiMember1.contains(ctx.getMember())) {
                int index = Data.firstEmojiMember1.indexOf(ctx.getMember());
                secondMember = Data.secondEmojiMember1.get(index);
                firstMember = ctx.getMember();
                pop = true;
            }

            if (Data.secondEmojiMember1.contains(ctx.getMember())) {
                int index = Data.secondEmojiMember1.indexOf(ctx.getMember());
                secondMember = ctx.getMember();
                firstMember = Data.firstEmojiMember1.get(index);
                pop = true;
            }

            if (pop) {
                Integer elo = Data.usersElo.get(member.getUser());
                Data.usersElo.put(member.getUser(), elo + 1);
                Data.firstEmojiMember1.remove(firstMember);
                Data.secondEmojiMember1.remove(secondMember);
                Data.textChannelsToFirstMember.get(firstMember).delete().queueAfter(60, TimeUnit.SECONDS);

                ctx.getChannel().sendMessage("Congratulations " + member.getAsMention() + ", " + ctx.getAuthor().getAsMention() + " forced the win to you! 1 ELO has been given to you for winning.").queue();
                return;
            }
        } else if (oneOrTwo.equalsIgnoreCase("2")) {
            Member thirdMember = null;
            Member fourthMember = null;

            if (Data.firstEmojiMember.contains(ctx.getMember())) {
                int index = Data.firstEmojiMember.indexOf(ctx.getMember());
                secondMember = Data.secondEmojiMember.get(index);
                firstMember = ctx.getMember();
                thirdMember = Data.thirdEmojiMember.get(index);
                fourthMember = Data.fourthEmojiMember.get(index);
            }

            if (Data.secondEmojiMember.contains(ctx.getMember())) {
                int index = Data.secondEmojiMember.indexOf(ctx.getMember());
                secondMember = ctx.getMember();
                firstMember = Data.firstEmojiMember.get(index);
                thirdMember = Data.thirdEmojiMember.get(index);
                fourthMember = Data.fourthEmojiMember.get(index);
            }

            if (Data.thirdEmojiMember.contains(ctx.getMember())) {
                int index = Data.thirdEmojiMember.indexOf(ctx.getMember());
                secondMember = Data.secondEmojiMember.get(index);
                firstMember = Data.firstEmojiMember.get(index);
                thirdMember = ctx.getMember();
                fourthMember = Data.fourthEmojiMember.get(index);
            }

            if (Data.fourthEmojiMember.contains(ctx.getMember())) {
                int index = Data.fourthEmojiMember.indexOf(ctx.getMember());
                secondMember = Data.secondEmojiMember.get(index);
                firstMember = Data.firstEmojiMember.get(index);
                thirdMember = Data.thirdEmojiMember.get(index);
                fourthMember = ctx.getMember();
            }

            if (member.equals(firstMember) || member.equals(secondMember)) {
                //first team win
                Integer elo = Data.usersElo.get(firstMember.getUser());
                Integer elo1 = Data.usersElo.get(secondMember.getUser());
                Data.usersElo.put(firstMember.getUser(), elo + 1);
                Data.usersElo.put(secondMember.getUser(), elo1 + 1);

                Data.firstEmojiMember.remove(firstMember);
                Data.secondEmojiMember.remove(secondMember);
                Data.thirdEmojiMember.remove(thirdMember);
                Data.fourthEmojiMember.remove(fourthMember);
                Data.textChannelsToFirstMember.get(firstMember).delete().queueAfter(60, TimeUnit.SECONDS);

                ctx.getChannel().sendMessage("Congratulations " + firstMember.getAsMention() + " and " + secondMember.getAsMention() + ", " + ctx.getAuthor().getAsMention() + " forced the win to you! 1 ELO has been given to you for winning.").queue();
                return;
            } else if (member.equals(thirdMember) || member.equals(fourthMember)) {
                //second team win
                Integer elo = Data.usersElo.get(thirdMember.getUser());
                Integer elo1 = Data.usersElo.get(fourthMember.getUser());
                Data.usersElo.put(thirdMember.getUser(), elo + 1);
                Data.usersElo.put(fourthMember.getUser(), elo1 + 1);

                Data.firstEmojiMember.remove(firstMember);
                Data.secondEmojiMember.remove(secondMember);
                Data.thirdEmojiMember.remove(thirdMember);
                Data.fourthEmojiMember.remove(fourthMember);
                Data.textChannelsToFirstMember.get(firstMember).delete().queueAfter(60, TimeUnit.SECONDS);
                ctx.getChannel().sendMessage("Congratulations " + thirdMember.getAsMention() + " and " + fourthMember.getAsMention() + ", " + ctx.getAuthor().getAsMention() + " forced the win to you! 1 ELO has been given to you for winning.").queue();
                return;
            }
        } else {
            ctx.getChannel().sendMessage("It has to be either 1 or 2!").queue();
            return;
        }

        ctx.getChannel().sendMessage("The member you mentioned is not in a game/queue!").queue();
    }

    @Override
    public String getName() {
        return "forcewin";
    }

    @Override
    public String getHelp(String prefix) {
        return "Forces the specified user to win!\n" +
                "Usage: `" + prefix + getName() + " [1 or 2] [user/any user of the team]`\n" +
                "1 is a 1v1 game\n" +
                "2 is a 2v2 game";
    }
}
