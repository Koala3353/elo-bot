package com.general_hello.commands.commands.DefaultCommands;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class CloseGameCommand implements ICommand
{
    @Override
    public void handle(CommandContext ctx) throws InterruptedException, IOException {
        if (!ctx.getMember().hasPermission(Permission.MANAGE_PERMISSIONS)) {
            ctx.getChannel().sendMessage("You don't have the permission to use this command").queue();
            return;
        }

        String oneOrTwo;

        try {
            oneOrTwo = ctx.getArgs().get(0);
        } catch (Exception e) {
            ctx.getChannel().sendMessage("Kindly place whether it is 1 or 2").queue();
            return;
        }
        Member member;

        try {
            member = ctx.getMessage().getMentionedMembers().get(0);
        } catch (Exception e) {
            ctx.getChannel().sendMessage("Kindly mention any member of a game that you want to close").queue();
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
                int index = Data.secondEmojiMember.indexOf(ctx.getMember());
                secondMember = ctx.getMember();
                firstMember = Data.firstEmojiMember1.get(index);
                pop = true;
            }

            if (pop) {
                Data.firstEmojiMember1.remove(firstMember);
                Data.secondEmojiMember1.remove(secondMember);
                Data.textChannelsToFirstMember.get(firstMember).delete().queueAfter(60, TimeUnit.SECONDS);
                ctx.getChannel().sendMessage("Successfully ended the game").queue();
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
                Data.firstEmojiMember.remove(firstMember);
                Data.secondEmojiMember.remove(secondMember);
                Data.thirdEmojiMember.remove(thirdMember);
                Data.fourthEmojiMember.remove(fourthMember);
                Data.textChannelsToFirstMember.get(firstMember).delete().queueAfter(60, TimeUnit.SECONDS);

                ctx.getChannel().sendMessage("Successfully ended the game").queue();
                return;
            } else if (member.equals(thirdMember) || member.equals(fourthMember)) {
                //second team win
                Data.firstEmojiMember.remove(firstMember);
                Data.secondEmojiMember.remove(secondMember);
                Data.thirdEmojiMember.remove(thirdMember);
                Data.textChannelsToFirstMember.get(firstMember).delete().queueAfter(60, TimeUnit.SECONDS);
                Data.fourthEmojiMember.remove(fourthMember);
                ctx.getChannel().sendMessage("Successfully ended the game").queue();
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
        return "close";
    }

    @Override
    public String getHelp(String prefix) {
        return "Close/Ends the game, doesn't give the ELO\n" +
                "Usage: `" + prefix + getName() + " [1 or 2] [mention any user of that game]`";
    }
}
