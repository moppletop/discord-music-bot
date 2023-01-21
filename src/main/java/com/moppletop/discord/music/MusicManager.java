package com.moppletop.discord.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MusicManager {

    private final AudioPlayerManager playerManager;

    private final Map<Long, GuildMusicManager> musicManagers;

    public MusicManager() {
        this.playerManager = new DefaultAudioPlayerManager();
        playerManager.registerSourceManager(new YoutubeAudioSourceManager(false));
        AudioSourceManagers.registerLocalSource(playerManager);

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

    public void record(Guild guild) {
        if (!guild.getAudioManager().isConnected()) {
            log.info("We're not connected to a voice channel in {}", guild.getName());
            return;
        }

        GuildMusicManager musicManager = getGuildAudioPlayer(guild);

        log.info("Starting recording...");
        musicManager.record();
    }

    public void saveRecording(Guild guild, String name) {
        if (!guild.getAudioManager().isConnected()) {
            log.info("We're not connected to a voice channel in {}", guild.getName());
            return;
        }

        GuildMusicManager musicManager = getGuildAudioPlayer(guild);

        log.info("Saving recording to {}...", name);
        musicManager.saveRecording(name);
    }

    public GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
        guild.getAudioManager().setReceivingHandler(musicManager.getReceiveHandler());

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
