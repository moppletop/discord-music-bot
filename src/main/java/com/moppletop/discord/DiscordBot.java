package com.moppletop.discord;

import com.moppletop.discord.library.TrackLibrary;
import com.moppletop.discord.music.MusicManager;
import com.moppletop.discord.ui.MenuManager;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

@Getter
public class DiscordBot {

    public static void main(String[] args) throws Exception {
        new DiscordBot(args[0]);
    }

    private final JDA jda;
    private final MusicManager musicManager;
    private final TrackLibrary trackLibrary;

    public DiscordBot(String token) throws Exception {
        this.jda = JDABuilder.createDefault(token)
                .disableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_TYPING)
                .setActivity(Activity.listening("tunes"))
                .build()
                .awaitReady();

        this.musicManager = new MusicManager();
        this.trackLibrary = new TrackLibrary();
        new MenuManager(this);
    }

    public void shutdown() {
        jda.shutdown();
        System.exit(0);
    }
}
