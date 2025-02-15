package com.moppletop.discord.music;

import com.moppletop.discord.DiscordBot;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MusicManager {

    private static YoutubeAudioSourceManager loadYoutubeSource(String refreshToken) {
        YoutubeAudioSourceManager manager = new YoutubeAudioSourceManager();
        manager.useOauth2(refreshToken, false);
        return manager;
    }

    private final AudioPlayerManager playerManager;

    private final Map<Long, GuildMusicManager> musicManagers;

    public MusicManager(DiscordBot discordBot) {
        this.playerManager = new DefaultAudioPlayerManager();
        playerManager.registerSourceManager(loadYoutubeSource(discordBot.getConfig().getProperties().getYoutubeToken()));

        this.musicManagers = Collections.synchronizedMap(new HashMap<>());
    }

    public void play(Guild guild, String url) {
        if (!guild.getAudioManager().isConnected()) {
            log.info("We're not connected to a voice channel in {}", guild.getName());
            return;
        }

        GuildMusicManager musicManager = getGuildAudioPlayer(guild);

        playerManager.loadItemOrdered(musicManager, url, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                log.info("Playing {}...", track.getInfo().title);

                musicManager.play(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                log.info("Playing playlist {}...", playlist.getName());

                musicManager.playMultiple(playlist.getTracks());
            }

            @Override
            public void noMatches() {
                log.info("Nothing found: {}", url);
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                log.error("Could not play {} for reason {}", url, exception.getMessage());
            }
        });
    }

    public GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    public void destroyGuildAudioPlayer(Guild guild) {
        long guildId = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.remove(guildId);

        if (musicManager != null) {
            musicManager.destroy();
        }

        guild.getAudioManager().closeAudioConnection();
    }
}
