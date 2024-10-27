package com.moppletop.discord;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moppletop.discord.config.Config;
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
        new DiscordBot();
    }

    private final Config config;
    private final JDA jda;
    private final MusicManager musicManager;
    private final TrackLibrary trackLibrary;

    public DiscordBot() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        this.config = new Config(objectMapper);
        this.jda = JDABuilder.createDefault(config.getProperties().getDiscordToken())
                .disableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_TYPING)
                .setActivity(Activity.listening("tunes"))
                .build()
                .awaitReady();
        this.musicManager = new MusicManager(this);
        this.trackLibrary = new TrackLibrary(objectMapper);
        new MenuManager(this);
    }

    public void shutdown() {
        jda.shutdown();
        System.exit(0);
    }
}
