package com.general_hello.commands.commands.DefaultCommands;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.*;

public class LeaderboardCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws InterruptedException, IOException {
        HashMap<User, Integer> usersElo = Data.usersElo;
        HashMap<Integer, User> eloToUser = new HashMap<>();
        ArrayList<User> users = Data.users;
        ArrayList<Integer> eloPts = new ArrayList<>();

        int x = 0;

        while (x < users.size()) {
            User user = users.get(x);
            eloPts.add(usersElo.get(user));
            eloToUser.put(usersElo.get(user), user);
            x++;
        }

        Collections.sort(eloPts);
        Collections.reverse(eloPts);

        StringBuilder leaderboard = new StringBuilder();
        x = 0;

        while (x < eloPts.size()) {
            Integer elo = eloPts.get(x);
            User user = eloToUser.get(elo);
            leaderboard.append(x+1).append(".) ").append(user.getAsTag()).append(" -> ").append(elo).append(" ELO").append("\n");
            x++;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder().setColor(SendQueueCommand.randomColor()).setTitle("Leaderboard");
        embedBuilder.setFooter("Congratulations to the top 1").setTimestamp(OffsetDateTime.now());
        embedBuilder.setDescription(leaderboard.toString());
        ctx.getChannel().sendMessage(embedBuilder.build()).queue();
    }

    @Override
    public String getName() {
        return "leaderboard";
    }

    @Override
    public String getHelp(String prefix) {
        return "Shows the leaderboard\n" +
                "Usage: `" + prefix + getName() + "`";
    }
}
