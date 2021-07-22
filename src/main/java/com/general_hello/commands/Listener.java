package com.general_hello.commands;

import com.general_hello.commands.Database.DatabaseManager;
import com.general_hello.commands.commands.DefaultCommands.Data;
import com.general_hello.commands.commands.DefaultCommands.SendQueueCommand;
import com.general_hello.commands.commands.PrefixStoring;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ResumedEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Listener extends ListenerAdapter {
    private final CommandManager manager;
    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    public static HashMap<String, Integer> count = new HashMap<>();
    private static OffsetDateTime timeDisconnected = OffsetDateTime.now();
    public static JDA jda;

    public Listener(EventWaiter waiter) {
        manager = new CommandManager(waiter);
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
    }

    @Override
    public void onResumed(@NotNull ResumedEvent event)  {
        EmbedBuilder em = new EmbedBuilder().setColor(Color.RED).setTitle("🔴 Disconnected");
        em.setDescription("The bot disconnected for " +
                (OffsetDateTime.now().getHour() - timeDisconnected.getHour())  + " hour(s) " +
                (OffsetDateTime.now().getMinute() - timeDisconnected.getMinute()) + " minute(s) " +
                (OffsetDateTime.now().getSecond() - timeDisconnected.getSecond()) + " second(s) and " +
                (timeDisconnected.getNano() /1000000) + " milliseconds due to connectivity issues!\n" +
                "Response number: " + event.getResponseNumber()).setTimestamp(OffsetDateTime.now());
        User owner_id = event.getJDA().getUserById(Config.get("owner_id"));
        User owner_id1 = event.getJDA().getUserById(Config.get("owner_id_partner"));
        owner_id.openPrivateChannel().complete().sendMessage(em.build()).queue();
        owner_id1.openPrivateChannel().complete().sendMessage(em.build()).queue();
    }

    @Override
    public void onDisconnect(@NotNull DisconnectEvent event) {
        timeDisconnected = event.getTimeDisconnected();
    }

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        if (Data.openQueue) {
            //testing if can join queue
            if (Data.queueMessageId.contains(event.getMessageIdLong())) {
                if (event.getUser().isBot()) return;

                if (Data.firstGameMemberQueue.contains(event.getMember()) || Data.secondGameMemberQueue.contains(event.getMember())) {
                    EmbedBuilder embedBuilder = new EmbedBuilder().setColor(SendQueueCommand.randomColor()).setTimestamp(OffsetDateTime.now()).setTitle("Unsuccessful").setFooter("DM \uD835\uDD0A\uD835\uDD22\uD835\uDD2B\uD835\uDD22\uD835\uDD2F\uD835\uDD1E\uD835\uDD29 ℌ\uD835\uDD22\uD835\uDD29\uD835\uDD29\uD835\uDD2C#3153 if you want a custom discord bot.");
                    event.getReaction().removeReaction(event.getUser()).queue();
                    embedBuilder.setDescription("You already joined either the **1v1 realistic** or the **2v2 boxfights**. Kindly wait to be done in your turn before joining the queue again.");
                    event.getUser().openPrivateChannel().complete().sendMessage(embedBuilder.build()).queue();
                    return;
                }

                //joining queue
                if (event.getReaction().getReactionEmote().getEmoji().equals("1️⃣")) {
                    Data.firstGameMemberQueue.add(event.getMember());
                    EmbedBuilder embedBuilder = new EmbedBuilder().setColor(SendQueueCommand.randomColor()).setTimestamp(OffsetDateTime.now()).setTitle("Success").setFooter("DM \uD835\uDD0A\uD835\uDD22\uD835\uDD2B\uD835\uDD22\uD835\uDD2F\uD835\uDD1E\uD835\uDD29 ℌ\uD835\uDD22\uD835\uDD29\uD835\uDD29\uD835\uDD2C#3153 if you want a custom discord bot.");
                    embedBuilder.setDescription("You have successfully joined the queue for **1v1 realistic**. The bot will ping and DM you once it is your turn.");
                    event.getUser().openPrivateChannel().complete().sendMessage(embedBuilder.build()).queue();
                } else if (event.getReaction().getReactionEmote().getEmoji().equals("2️⃣")) {
                    Data.secondGameMemberQueue.add(event.getMember());
                    EmbedBuilder embedBuilder = new EmbedBuilder().setColor(SendQueueCommand.randomColor()).setTimestamp(OffsetDateTime.now()).setTitle("Success").setFooter("DM \uD835\uDD0A\uD835\uDD22\uD835\uDD2B\uD835\uDD22\uD835\uDD2F\uD835\uDD1E\uD835\uDD29 ℌ\uD835\uDD22\uD835\uDD29\uD835\uDD29\uD835\uDD2C#3153 if you want a custom discord bot.");
                    embedBuilder.setDescription("You have successfully joined the queue for **2v2 boxfights**. The bot will ping and DM you once it is your turn.");
                    event.getUser().openPrivateChannel().complete().sendMessage(embedBuilder.build()).queue();
                }

                if (Data.firstGameMemberQueue.size() > 1) {
                    Member member = Data.firstGameMemberQueue.get(0);
                    Member member1 = Data.firstGameMemberQueue.get(1);
                    makeChannel(event, member, member1);
                    member.getUser().openPrivateChannel().complete().sendMessage("It is your turn already in the queue! Kindly go to the server and to the channel you were pinged at.").queue();
                    member1.getUser().openPrivateChannel().complete().sendMessage("It is your turn already in the queue! Kindly go to the server and to the channel you were pinged at.").queue();
                }

                if (Data.secondGameMemberQueue.size() > 3) {
                    Member member = Data.secondGameMemberQueue.get(0);
                    Member member1 = Data.secondGameMemberQueue.get(1);
                    Member member2 = Data.secondGameMemberQueue.get(2);
                    Member member3 = Data.secondGameMemberQueue.get(3);
                    makeChannel2v2(event, member, member1, member2, member3);
                    member.getUser().openPrivateChannel().complete().sendMessage("It is your turn already in the queue! Kindly go to the server and to the channel you were pinged at.").queue();
                    member1.getUser().openPrivateChannel().complete().sendMessage("It is your turn already in the queue! Kindly go to the server and to the channel you were pinged at.").queue();
                    member2.getUser().openPrivateChannel().complete().sendMessage("It is your turn already in the queue! Kindly go to the server and to the channel you were pinged at.").queue();
                    member3.getUser().openPrivateChannel().complete().sendMessage("It is your turn already in the queue! Kindly go to the server and to the channel you were pinged at.").queue();
                }
            }
        }

        if (!Data.thirdEmojiMember.isEmpty() && !Data.fourthEmojiMember.isEmpty() && Data.firstEmojiMember.contains(event.getMember()) || Data.secondEmojiMember.contains(event.getMember()) || Data.thirdEmojiMember.contains(event.getMember())|| Data.fourthEmojiMember.contains(event.getMember())) {
            //2v2

            Member secondMember = null;
            Member firstMember = null;
            Member thirdMember = null;
            Member fourthMember = null;

            if (Data.firstEmojiMember.contains(event.getMember())) {
                int index = Data.firstEmojiMember.indexOf(event.getMember());
                secondMember = Data.secondEmojiMember.get(index);
                firstMember = event.getMember();
                thirdMember = Data.thirdEmojiMember.get(index);
                fourthMember = Data.fourthEmojiMember.get(index);
            }

            if (Data.secondEmojiMember.contains(event.getMember())) {
                int index = Data.secondEmojiMember.indexOf(event.getMember());
                secondMember = event.getMember();
                firstMember = Data.firstEmojiMember.get(index);
                thirdMember = Data.thirdEmojiMember.get(index);
                fourthMember = Data.fourthEmojiMember.get(index);
            }

            if (Data.thirdEmojiMember.contains(event.getMember())) {
                int index = Data.thirdEmojiMember.indexOf(event.getMember());
                secondMember = Data.secondEmojiMember.get(index);
                firstMember = Data.firstEmojiMember.get(index);
                thirdMember = event.getMember();
                fourthMember = Data.fourthEmojiMember.get(index);
            }

            if (Data.fourthEmojiMember.contains(event.getMember())) {
                int index = Data.fourthEmojiMember.indexOf(event.getMember());
                secondMember = Data.secondEmojiMember.get(index);
                firstMember = Data.firstEmojiMember.get(index);
                thirdMember = Data.thirdEmojiMember.get(index);
                fourthMember = event.getMember();
            }

            Integer voteCountFirst = Data.voteCount.get(firstMember);
            Integer voteCountSecond = Data.voteCount.get(secondMember);
            Integer voteCountThird = Data.voteCount.get(thirdMember);
            Integer voteCountFourth = Data.voteCount.get(fourthMember);

            if (event.getReaction().getReactionEmote().getEmoji().equals("\uD83D\uDC36")) {
                Data.voteCount.put(firstMember, voteCountFirst + 1);
                Data.voteCount.put(secondMember, voteCountSecond + 1);
            } else if (event.getReactionEmote().getEmoji().equals("\uD83D\uDC14")) {
                Data.voteCount.put(thirdMember, voteCountThird + 1);
                Data.voteCount.put(fourthMember, voteCountFourth + 1);
            } else if (event.getReaction().getReactionEmote().getEmoji().equals("❓")) {
                TextChannel channel = Data.textChannelsToFirstMember.get(firstMember);
                channel.sendMessage(event.getJDA().getUserById("513854184450162699").getAsMention() + " and " /*+ event.getJDA().getUserById("424531571970801664").getAsMention()*/ + ", We have a problem here! Both of them says that they won.").queue();
            }

            System.out.println("ok");

            if (Data.voteCount.get(firstMember) == 4 || Data.voteCount.get(secondMember) == 4) {
                TextChannel channel = Data.textChannelsToFirstMember.get(firstMember);
                System.out.println(channel);
                Integer integer = Data.usersElo.get(firstMember.getUser());
                Integer integer1 = Data.usersElo.get(secondMember.getUser());
                Data.usersElo.put(firstMember.getUser(), integer + 1);
                Data.usersElo.put(secondMember.getUser(), integer1 + 1);

                channel.sendMessage("Congratulations " + firstMember.getAsMention() + " and " + secondMember.getAsMention() + " for winning the game! 1 ELO point has been given to both of you for winning.\n" +
                        "Total **ELO** is now " + Data.usersElo.get(firstMember.getUser()) + " for " + firstMember.getAsMention() + " and " + Data.usersElo.get(secondMember.getUser()) + " ELO for " + secondMember.getAsMention()).queue();
                channel.delete().queueAfter(60, TimeUnit.SECONDS);

                Data.firstEmojiMember.remove(firstMember);
                Data.secondEmojiMember.remove(secondMember);
                Data.thirdEmojiMember.remove(thirdMember);
                Data.fourthEmojiMember.remove(fourthMember);
            } else if (Data.voteCount.get(thirdMember) == 4 || Data.voteCount.get(fourthMember) == 4) {
                TextChannel channel = Data.textChannelsToFirstMember.get(firstMember);
                channel.sendMessage("Congratulations " + thirdMember.getAsMention() + " and " + fourthMember.getAsMention() + " for winning the game! 1 ELO point has been given to both of you for winning.\n" +
                        "Total **ELO** is now " + Data.usersElo.get(thirdMember.getUser()) + " for " + thirdMember.getAsMention() + " and " + Data.usersElo.get(fourthMember.getUser()) + " ELO for " + fourthMember.getAsMention()).queue();
                channel.delete().queueAfter(60, TimeUnit.SECONDS);
                Integer integer = Data.usersElo.get(thirdMember.getUser());
                Integer integer1 = Data.usersElo.get(fourthMember.getUser());
                Data.usersElo.put(thirdMember.getUser(), integer + 1);
                Data.usersElo.put(fourthMember.getUser(), integer1 + 1);
                Data.firstEmojiMember.remove(firstMember);
                Data.secondEmojiMember.remove(secondMember);
                Data.thirdEmojiMember.remove(thirdMember);
                Data.fourthEmojiMember.remove(fourthMember);
            } else if (Data.voteCount.get(firstMember) == 1){
                if (Data.voteCount.get(thirdMember) == 1) {
                    TextChannel channel = Data.textChannelsToFirstMember.get(firstMember);
                    channel.sendMessage(event.getJDA().getUserById("513854184450162699").getAsMention() + " and " + event.getJDA().getUserById("424531571970801664").getAsMention() + ", We have a problem here! Both of them says that they won.").queue();
                }
            }

            return;
        }

        //1v1
        if (Data.thirdEmojiMember.isEmpty() && Data.fourthEmojiMember.isEmpty() && Data.firstEmojiMember.contains(event.getMember()) || Data.secondEmojiMember.contains(event.getMember())) {
            Member secondMember = null;
            Member firstMember = null;

            if (Data.firstEmojiMember1.contains(event.getMember())) {
                int index = Data.firstEmojiMember1.indexOf(event.getMember());
                secondMember = Data.secondEmojiMember1.get(index);
                firstMember = event.getMember();
            }

            if (Data.secondEmojiMember1.contains(event.getMember())) {
                int index = Data.secondEmojiMember.indexOf(event.getMember());
                secondMember = event.getMember();
                firstMember = Data.firstEmojiMember1.get(index);
            }

            Integer voteCountFirst = Data.voteCount.get(firstMember);
            Integer voteCountSecond = Data.voteCount.get(secondMember);

            if (event.getReaction().getReactionEmote().getEmoji().equals("\uD83D\uDC36")) {
                Data.voteCount.put(firstMember, voteCountFirst + 1);
            } else if (event.getReactionEmote().getEmoji().equals("\uD83D\uDC14")) {
                Data.voteCount.put(secondMember, voteCountSecond + 1);
            } else if (event.getReaction().getReactionEmote().getEmoji().equals("❓")) {
                TextChannel channel = Data.textChannelsToFirstMember.get(firstMember);
                channel.sendMessage(event.getJDA().getUserById("513854184450162699").getAsMention() + " and " /*+ event.getJDA().getUserById("424531571970801664").getAsMention()*/ + ", We have a problem here! Both of them says that they won.").queue();
            }

            if (Data.voteCount.get(firstMember) == 2) {
                TextChannel channel = Data.textChannelsToFirstMember.get(firstMember);
                System.out.println(channel);
                Data.firstEmojiMember1.remove(firstMember);
                Data.secondEmojiMember1.remove(secondMember);
                Integer integer = Data.usersElo.get(firstMember.getUser());
                Data.usersElo.put(firstMember.getUser(), integer + 1);
                channel.sendMessage("Congratulations " + firstMember.getAsMention() + " for winning the game! 1 ELO point has been given to you for winning.\n" +
                        "Total **ELO** is now " + Data.usersElo.get(firstMember.getUser())).queue();
                channel.delete().queueAfter(10, TimeUnit.SECONDS);
            } else if (Data.voteCount.get(secondMember) == 2) {
                Data.firstEmojiMember1.remove(firstMember);
                Data.secondEmojiMember1.remove(secondMember);
                TextChannel channel = Data.textChannelsToFirstMember.get(firstMember);
                channel.sendMessage("Congratulations " + secondMember.getAsMention() + " for winning the game! 1 ELO point has been given to you for winning.\n" +
                        "Total **ELO** is now " + Data.usersElo.get(secondMember.getUser())).queue();
                channel.delete().queueAfter(10, TimeUnit.SECONDS);
                Integer integer = Data.usersElo.get(secondMember.getUser());
                Data.usersElo.put(secondMember.getUser(), integer + 1);
            } else if (Data.voteCount.get(firstMember) == 1){
                if (Data.voteCount.get(secondMember) == 1) {
                    TextChannel channel = Data.textChannelsToFirstMember.get(firstMember);
                    channel.sendMessage(event.getJDA().getUserById("513854184450162699").getAsMention() + " and " + event.getJDA().getUserById("424531571970801664").getAsMention() + ", We have a problem here! Both of them says that they won.").queue();
                }
            }
        }
    }

    @Override
    public void onGuildMessageReactionRemove(@NotNull GuildMessageReactionRemoveEvent event) {
        if (Data.openQueue) {
            //testing if can leave queue
            if (Data.queueMessageId.contains(event.getMessageIdLong())) {
                if (event.getUser().isBot()) return;

                if (Data.firstGameMemberQueue.contains(event.getMember()) || Data.secondGameMemberQueue.contains(event.getMember())) {
                    //leaving queue
                    if (event.getReaction().getReactionEmote().getEmoji().equals("1️⃣")) {
                        Data.firstGameMemberQueue.remove(event.getMember());
                        EmbedBuilder embedBuilder = new EmbedBuilder().setColor(SendQueueCommand.randomColor()).setTimestamp(OffsetDateTime.now()).setTitle("Success").setFooter("DM \uD835\uDD0A\uD835\uDD22\uD835\uDD2B\uD835\uDD22\uD835\uDD2F\uD835\uDD1E\uD835\uDD29 ℌ\uD835\uDD22\uD835\uDD29\uD835\uDD29\uD835\uDD2C#3153 if you want a custom discord bot.");
                        embedBuilder.setDescription("You have successfully left the queue for **1v1 realistic**.");
                        event.getUser().openPrivateChannel().complete().sendMessage(embedBuilder.build()).queue();
                    } else if (event.getReaction().getReactionEmote().getEmoji().equals("2️⃣")) {
                        Data.secondGameMemberQueue.remove(event.getMember());
                        EmbedBuilder embedBuilder = new EmbedBuilder().setColor(SendQueueCommand.randomColor()).setTimestamp(OffsetDateTime.now()).setTitle("Success").setFooter("DM \uD835\uDD0A\uD835\uDD22\uD835\uDD2B\uD835\uDD22\uD835\uDD2F\uD835\uDD1E\uD835\uDD29 ℌ\uD835\uDD22\uD835\uDD29\uD835\uDD29\uD835\uDD2C#3153 if you want a custom discord bot.");
                        embedBuilder.setDescription("You have successfully left the queue for **2v2 boxfights**.");
                        event.getUser().openPrivateChannel().complete().sendMessage(embedBuilder.build()).queue();
                    }
                }
            }
        }
    }

    @Override
    public void onShutdown(@NotNull ShutdownEvent event) {
        System.out.println("Shut downed the bot at " +
                event.getTimeShutdown().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)+ " due to maintenance.\n" +
                "With response number of " + event.getResponseNumber() + "\n" +
                "With the code of " + event.getCloseCode().getCode() + "\n");
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        final long guildID = event.getGuild().getIdLong();
        String prefix = PrefixStoring.PREFIXES.computeIfAbsent(guildID, DatabaseManager.INSTANCE::getPrefix);
        String raw = event.getMessage().getContentRaw();


        jda = event.getJDA();

        if (event.getAuthor().isBot()) return;

        if (Data.progressInQuestioning.containsKey(event.getAuthor())) {
            if (Data.progressInQuestioning.get(event.getAuthor()) != 100) {
                registering(event);
            }
        }

        if (raw.equalsIgnoreCase(prefix + "shutdown") && event.getAuthor().getId().equals(Config.get("owner_id"))) {
            shutdown(event, true);
            return;
        } else if (raw.equalsIgnoreCase(prefix + "shutdown") && event.getAuthor().getId().equals(Config.get("owner_id_partner"))) {
            shutdown(event, false);
            return;
        }

        if (raw.startsWith(prefix)) {
            try {
                manager.handle(event, prefix);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String commandsCount() {
        int x = 0;
        int size = CommandManager.commandNames.size();
        StringBuilder result = new StringBuilder();

        while (x < size) {
            String commandName = CommandManager.commandNames.get(x);
            result.append(x+1).append(".) ").append(commandName).append(" - ").append(count.get(commandName)).append("\n");
            x++;
        }

        return String.valueOf(result);
    }

    public static void registering (GuildMessageReceivedEvent event) {
        Integer progress = Data.progressInQuestioning.get(event.getAuthor());
        ArrayList<String> userDetails = new ArrayList<>();

        if (Data.userDetails.containsKey(event.getAuthor())) {
            userDetails = Data.userDetails.get(event.getAuthor());
        }

        String message = event.getMessage().getContentRaw();

        if (progress == 1) {
            userDetails.add(message);
            Data.userDetails.put(event.getAuthor(), userDetails);
            event.getChannel().sendMessage("Awesome, **" + message + "**! So **" + message + "** what is your platform?\n" +
                    "Is it **Playstation**, **Xbox**, or **Computer**").queue();
            Data.progressInQuestioning.put(event.getAuthor(), 2);
            return;
        }

        if (progress == 2) {
            if (message.equalsIgnoreCase("playstation")) {
                userDetails.add("<:playstation:856358776638144512>");
            } else if (message.equalsIgnoreCase("xbox")) {
                userDetails.add("<:xbox:856360506439630878>");
            } else if (message.equalsIgnoreCase("computer")) {
                userDetails.add("💻");
            } else {
                event.getChannel().sendMessage("Invalid platform placed!\n" + "Kindly choose only from Xbox, Computer, or Playstation").queue();
                return;
            }

            Data.userDetails.put(event.getAuthor(), userDetails);
            event.getChannel().sendMessage("Wow! You're actually using a " + message + "!\n" +
                    "What peripheral are you using? (**Keyboard** or a **Controller**)").queue();
            Data.progressInQuestioning.put(event.getAuthor(), 3);
            return;
        }

        if (progress == 3) {
            if (message.equalsIgnoreCase("keyboard")) {
                userDetails.add("⌨");
            } else if (message.equalsIgnoreCase("controller")) {
                userDetails.add("🎮");
            } else {
                event.getChannel().sendMessage("Invalid peripheral placed!\n" + "Kindly choose only from a **Controller** or a **Keyboard**").queue();
                return;
            }

            Data.userDetails.put(event.getAuthor(), userDetails);
            event.getChannel().sendMessage("Excellent! A " + message + " is an awesome peripheral!").queue();

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(event.getAuthor().getName() + "'s Profile").setColor(SendQueueCommand.randomColor()).setTimestamp(OffsetDateTime.now());
            embedBuilder.setDescription("Discord Tag: " + event.getAuthor().getAsTag() + "\n" +
                    "Epic name: " + userDetails.get(0) + "\n" +
                    "Platform: " + userDetails.get(1) + "\n" +
                    "Peripheral: " + userDetails.get(2))
                    .setFooter("Type accept if the above data is correct and if you allow the bot to store your data");

            event.getChannel().sendMessage(embedBuilder.build()).queue();
            Data.progressInQuestioning.put(event.getAuthor(), 4);
            return;
        }

        if (progress == 4) {
            if (message.equalsIgnoreCase("accept")) {
                Data.progressInQuestioning.put(event.getAuthor(), 100);
                event.getChannel().sendMessage("Successfully registered you to the database!").queue();
            } else {
                event.getChannel().sendMessage("Oh well.... We're now deleting you from the database since you didn't accept it").queue();
                Data.userDetails.remove(event.getAuthor());
                Data.progressInQuestioning.remove(event.getAuthor());
            }
        }
    }

    public static void makeChannel(GuildMessageReactionAddEvent event, Member member, Member member1) {

        event.getGuild().getCategoriesByName("queue", true).get(0).createTextChannel(member.getEffectiveName() + " vs " + member1.getEffectiveName())
                .addMemberPermissionOverride(member.getIdLong(), Collections.singletonList(Permission.VIEW_CHANNEL), Collections.emptyList())
                .addMemberPermissionOverride(member1.getIdLong(), Collections.singletonList(Permission.VIEW_CHANNEL), Collections.emptyList())
                .addMemberPermissionOverride(event.getGuild().getSelfMember().getIdLong(), Collections.singletonList(Permission.VIEW_CHANNEL), Collections.emptyList())
                .addRolePermissionOverride(event.getGuild().getIdLong(), Collections.emptyList(), Collections.singletonList(Permission.VIEW_CHANNEL)).setTopic("Run <help to show which commands you can use").queue(
                        channel -> {
                            Data.firstGameMemberQueue.remove(0);
                            Data.firstGameMemberQueue.remove(0);
                            channel.sendMessage(member.getAsMention() + " and " +
                                    member1.getAsMention() + " come here!").queue();

                            EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("A 1v1 realistic game has been found for " + member.getEffectiveName() + " and " + member1.getEffectiveName()).setColor(SendQueueCommand.randomColor());
                            embedBuilder.setAuthor(event.getJDA().getSelfUser().getName(), null, event.getJDA().getSelfUser().getAvatarUrl());
                            //continue here
                            String details = "Epic Name: _Not registered_\n" +
                                    "Platform: _Not registered_\n" +
                                    "Peripheral: _Not registered_\n";

                            String details1 = "Epic Name: _Not registered_\n" +
                                    "Platform: _Not registered_\n" +
                                    "Peripheral: _Not registered_\n";

                            if (Data.userDetails.containsKey(member.getUser())) {
                                ArrayList<String> userData = Data.userDetails.get(member.getUser());
                                details = "Epic Name: " + userData.get(0) + "\n" +
                                        "Platform: " + userData.get(1) + "\n" +
                                        "Peripheral: " + userData.get(2) + "\n";
                            }

                            if (Data.userDetails.containsKey(member1.getUser())) {
                                ArrayList<String> userData = Data.userDetails.get(member1.getUser());
                                details1 = "Epic Name: " + userData.get(0) + "\n" +
                                        "Platform: " + userData.get(1) + "\n" +
                                        "Peripheral: " + userData.get(2) + "\n";
                            }

                            if (!Data.usersElo.containsKey(member.getUser())) {
                                Data.usersElo.put(member.getUser(), 0);
                                Data.users.add(member.getUser());
                            }

                            if (!Data.usersElo.containsKey(member1.getUser())) {
                                Data.usersElo.put(member1.getUser(), 0);
                                Data.users.add(member1.getUser());
                            }

                            Data.voteCount.put(member, 0);
                            Data.voteCount.put(member1, 0);

                            embedBuilder.setDescription("**__Team 1__**\n" +
                                    "" +
                                    "Discord Name: " + member.getAsMention() + "\n" +
                                    details +
                                    "User's ELO: " + Data.usersElo.get(member.getUser()) + "\n\n" +
                                    "" +
                                    "**__Team 2__**\n" +
                                    "Discord Name: " + member1.getAsMention() + "\n" +
                                    details1 +
                                    "User's ELO: " + Data.usersElo.get(member1.getUser()) + "\n\n" +
                                    "" +
                                    "**__Rules__**\n" +
                                    "-First to 5 wins\n" +
                                    "-Contact a staff member if there are any issues __OR__ your opponent is AFK\n" +
                                    "-To view more detailed rules, go the the rules channel");


                            channel.sendMessage(embedBuilder.build()).queue();
                            embedBuilder.setDescription("When the game is over, use the reactions below to react who's the __**winner**__!\n\n" +
                                    member.getAsMention() + " won? 🐶\n" +
                                    member1.getAsMention() + " won? 🐔");

                            embedBuilder.setFooter("Need assistance? React with ❓");

                            Data.textChannelsToFirstMember.put(member, channel);

                            channel.sendMessage(embedBuilder.build()).queue((message -> {
                                message.addReaction("\uD83D\uDC36").queue();
                                message.addReaction("\uD83D\uDC14").queue();
                                message.addReaction("❓").queue();
                            }));

                            Data.firstEmojiMember1.add(member);
                            Data.secondEmojiMember1.add(member1);
                        });
    }

    public static void makeChannel2v2(GuildMessageReactionAddEvent event, Member member, Member member1, Member member2, Member member3) {

        event.getGuild().getCategoriesByName("queue", true).get(0).createTextChannel(member.getEffectiveName() + " and " + member1.getEffectiveName() + " vs " + member2.getEffectiveName() + " and " + member3.getEffectiveName())
                .addMemberPermissionOverride(member.getIdLong(), Collections.singletonList(Permission.VIEW_CHANNEL), Collections.emptyList())
                .addMemberPermissionOverride(member1.getIdLong(), Collections.singletonList(Permission.VIEW_CHANNEL), Collections.emptyList())
                .addMemberPermissionOverride(member2.getIdLong(), Collections.singletonList(Permission.VIEW_CHANNEL), Collections.emptyList())
                .addMemberPermissionOverride(member3.getIdLong(), Collections.singletonList(Permission.VIEW_CHANNEL), Collections.emptyList())
                .addMemberPermissionOverride(event.getGuild().getSelfMember().getIdLong(), Collections.singletonList(Permission.VIEW_CHANNEL), Collections.emptyList())
                .addRolePermissionOverride(event.getGuild().getIdLong(), Collections.emptyList(), Collections.singletonList(Permission.VIEW_CHANNEL)).setTopic("Run <help to show which commands you can use").queue(
                channel -> {
                    Data.secondGameMemberQueue.remove(0);
                    Data.secondGameMemberQueue.remove(0);
                    Data.secondGameMemberQueue.remove(0);
                    Data.secondGameMemberQueue.remove(0);

                    channel.sendMessage(member.getAsMention() + ", " + member1.getAsMention() + ", " + member2.getAsMention() + " and " +
                            member3.getAsMention() + " come here!").queue();

                    EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("A 2v2 realistic game has been found for " + member.getEffectiveName() + ", " + member1.getEffectiveName() + member2.getEffectiveName() + ", and " + member3.getEffectiveName()).setColor(SendQueueCommand.randomColor());
                    embedBuilder.setAuthor(event.getJDA().getSelfUser().getName(), null, event.getJDA().getSelfUser().getAvatarUrl());
                    //continue here
                    String details = "Epic Name: _Not registered_\n" +
                            "Platform: _Not registered_\n" +
                            "Peripheral: _Not registered_\n";

                    String details1 = "Epic Name: _Not registered_\n" +
                            "Platform: _Not registered_\n" +
                            "Peripheral: _Not registered_\n";

                    String details2 = "Epic Name: _Not registered_\n" +
                            "Platform: _Not registered_\n" +
                            "Peripheral: _Not registered_\n";

                    String details3 = "Epic Name: _Not registered_\n" +
                            "Platform: _Not registered_\n" +
                            "Peripheral: _Not registered_\n";

                    if (Data.userDetails.containsKey(member.getUser())) {
                        ArrayList<String> userData = Data.userDetails.get(member.getUser());
                        details = "Epic Name: " + userData.get(0) + "\n" +
                                "Platform: " + userData.get(1) + "\n" +
                                "Peripheral: " + userData.get(2) + "\n";
                    }

                    if (Data.userDetails.containsKey(member1.getUser())) {
                        ArrayList<String> userData = Data.userDetails.get(member1.getUser());
                        details1 = "Epic Name: " + userData.get(0) + "\n" +
                                "Platform: " + userData.get(1) + "\n" +
                                "Peripheral: " + userData.get(2) + "\n";
                    }

                    if (Data.userDetails.containsKey(member2.getUser())) {
                        ArrayList<String> userData = Data.userDetails.get(member1.getUser());
                        details1 = "Epic Name: " + userData.get(0) + "\n" +
                                "Platform: " + userData.get(1) + "\n" +
                                "Peripheral: " + userData.get(2) + "\n";
                    }

                    if (Data.userDetails.containsKey(member3.getUser())) {
                        ArrayList<String> userData = Data.userDetails.get(member1.getUser());
                        details1 = "Epic Name: " + userData.get(0) + "\n" +
                                "Platform: " + userData.get(1) + "\n" +
                                "Peripheral: " + userData.get(2) + "\n";
                    }

                    if (!Data.usersElo.containsKey(member.getUser())) {
                        Data.usersElo.put(member.getUser(), 0);
                        Data.users.add(member.getUser());
                    }

                    if (!Data.usersElo.containsKey(member1.getUser())) {
                        Data.usersElo.put(member1.getUser(), 0);
                        Data.users.add(member1.getUser());
                    }

                    if (!Data.usersElo.containsKey(member2.getUser())) {
                        Data.usersElo.put(member2.getUser(), 0);
                        Data.users.add(member1.getUser());
                    }

                    if (!Data.usersElo.containsKey(member3.getUser())) {
                        Data.usersElo.put(member3.getUser(), 0);
                        Data.users.add(member1.getUser());
                    }

                    Data.voteCount.put(member, 0);
                    Data.voteCount.put(member1, 0);
                    Data.voteCount.put(member2, 0);
                    Data.voteCount.put(member3, 0);

                    embedBuilder.setDescription("**__Team 1__**\n" +
                            "" +
                            "Discord Name: " + member.getAsMention() + "\n" +
                            details +
                            "User's ELO: " + Data.usersElo.get(member.getUser()) + "\n\n" +
                            "" +
                            "Discord Name: " + member1.getAsMention() + "\n" +
                            details1 +
                            "User's ELO: " + Data.usersElo.get(member1.getUser()) + "\n\n" +
                            "--------------------------------------------\n" +
                            "**__Team 2__**\n" +
                            "Discord Name: " + member2.getAsMention() + "\n" +
                            details2 +
                            "User's ELO: " + Data.usersElo.get(member2.getUser()) + "\n\n" +
                            "" +
                            "Discord Name: " + member3.getAsMention() + "\n" +
                            details3 +
                            "User's ELO: " + Data.usersElo.get(member3.getUser()) + "\n\n" +
                            "" +
                            "**__Rules__**\n" +
                            "-First to 5 wins\n" +
                            "-Contact a staff member if there are any issues __OR__ your opponent is AFK\n" +
                            "-To view more detailed rules, go the the rules channel");


                    channel.sendMessage(embedBuilder.build()).queue();
                    embedBuilder.setDescription("When the game is over, use the reactions below to react who's the __**winner**__!\n\n" +
                            member.getAsMention() + "'s Team won? 🐶\n" +
                            member2.getAsMention() + "'s Team won? 🐔");

                    embedBuilder.setFooter("Need assistance? React with ❓");

                    Data.textChannelsToFirstMember.put(member, channel);

                    channel.sendMessage(embedBuilder.build()).queue((message -> {
                        message.addReaction("\uD83D\uDC36").queue();
                        message.addReaction("\uD83D\uDC14").queue();
                        message.addReaction("❓").queue();
                    }));

                    Data.firstEmojiMember.add(member);
                    Data.secondEmojiMember.add(member1);
                    Data.thirdEmojiMember.add(member2);
                    Data.fourthEmojiMember.add(member3);
                });
    }

    public static void shutdown(GuildMessageReceivedEvent event, boolean isOwner) {
        event.getChannel().sendMessage("The bot " + event.getJDA().getSelfUser().getAsMention() + " is shutting down.\n" +
                "Thank you for using **General_Hello's** (**" + event.getJDA().getUserById(Config.get("owner_id_partner")).getAsTag() + "**) Code!!!").queue();

        event.getChannel().sendMessage("Shutting down...").queue();
        event.getChannel().sendMessage("Bot successfully shutdown!!!!").queue();
        EmbedBuilder em = new EmbedBuilder().setTitle("Shutdown details!!!!").setColor(Color.red).setFooter("Shutdown on ").setTimestamp(LocalDateTime.now());
        em.addField("Shutdown made by ", event.getAuthor().getName(), false);
        em.addField("Date", LocalDateTime.now().getMonthValue() + "/" + LocalDateTime.now().getDayOfMonth() + "/" + LocalDateTime.now().getYear(), false);
        em.addField("Total number of Commands used in this session....", CommandManager.commandNames.size() + " commands", false);
        em.addField("List of Commands used in this session....", commandsCount(), false);
        event.getAuthor().openPrivateChannel().complete().sendMessage(em.build()).queue();

        if (!isOwner) {
            User owner = event.getJDA().retrieveUserById(Config.get("owner_id")).complete();
            owner.openPrivateChannel().complete().sendMessage(em.build()).queue();
        }

        event.getJDA().shutdown();
        BotCommons.shutdown(event.getJDA());
    }
}