package com.general_hello.commands;

import com.general_hello.commands.Database.DatabaseManager;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.awt.*;

public class Bot {

    private Bot() throws LoginException {
        DatabaseManager.INSTANCE.getPrefix(-1);
        WebUtils.setUserAgent("Zoid");
        EmbedUtils.setEmbedBuilder(
                () -> new EmbedBuilder()
                        .setColor(Color.cyan)
                        .setFooter("<help")
        );

        EventWaiter waiter = new EventWaiter();

        JDABuilder.createDefault(Config.get("token"),
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.DIRECT_MESSAGE_TYPING,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_EMOJIS,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.GUILD_INVITES
        )
                .enableCache(CacheFlag.VOICE_STATE)
                .addEventListeners(new Listener(waiter), waiter)
                .setActivity(Activity.watching("DM \uD835\uDD0A\uD835\uDD22\uD835\uDD2B\uD835\uDD22\uD835\uDD2F\uD835\uDD1E\uD835\uDD29 ℌ\uD835\uDD22\uD835\uDD29\uD835\uDD29\uD835\uDD2C#3153 for a custom discord bot!"))
                .setStatus(OnlineStatus.ONLINE)
                .setChunkingFilter(ChunkingFilter.ALL) // enable member chunking for all guilds
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .build();
    }
    public static void main(String[] args) throws LoginException {
        new Bot();
    }
}
