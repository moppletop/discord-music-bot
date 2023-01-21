package com.moppletop.discord.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

public class GuildMusicManager extends AudioEventAdapter {

    private final AudioPlayer player;
    private final LinkedList<AudioTrack> queue;
    @Getter
    private final AudioPlayerReceiveHandler receiveHandler;

    public GuildMusicManager(AudioPlayerManager manager) {
        player = manager.createPlayer();
        player.setVolume(5);
        player.addListener(this);

        receiveHandler = new AudioPlayerReceiveHandler();

        queue = new LinkedList<>();
    }

    public void play(AudioTrack track) {
        queue.clear();

        player.playTrack(track);
    }

    public void playMultiple(Collection<AudioTrack> tracks) {
        queue.clear();

        if (!tracks.isEmpty()) {
            queue.addAll(tracks);
            player.playTrack(queue.poll());
        }
    }

    public void resume() {
        if (player.isPaused()) {
            player.setPaused(false);
        }
    }

    public void pause() {
        if (!player.isPaused()) {
            player.setPaused(true);
        }
    }

    public void setVolume(int volume) {
        player.setVolume(volume);
    }

    public void record() {
        receiveHandler.setRecording(true);
    }

    public void saveRecording(String name) {
        File file = new File(name + ".wav");

        if (file.exists()) {
            file.delete();
        }

        try {
            file.createNewFile();

            receiveHandler.writeTo(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void destroy() {
        queue.clear();
        player.removeListener(this);
        player.destroy();
        receiveHandler.setRecording(false);
    }

    public AudioPlayerSendHandler getSendHandler() {
        return new AudioPlayerSendHandler(player);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason == AudioTrackEndReason.FINISHED && !queue.isEmpty()) {
            AudioTrack nextTrack = queue.poll();
            player.playTrack(nextTrack);
        }
    }
}