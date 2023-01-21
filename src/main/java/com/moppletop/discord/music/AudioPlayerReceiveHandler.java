package com.moppletop.discord.music;

import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.CombinedAudio;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class AudioPlayerReceiveHandler implements AudioReceiveHandler {

    private final List<byte[]> bytes = new LinkedList<>();
    private boolean recording;

    @Override
    public boolean canReceiveCombined() {
        return true;
    }

    @Override
    public void handleCombinedAudio(CombinedAudio combinedAudio) {
        if (recording) {
            bytes.add(combinedAudio.getAudioData(1));
        }
    }

    public void setRecording(boolean recording) {
        bytes.clear();
        this.recording = recording;
    }

    public void writeTo(File file) throws IOException {
        recording = false;

        System.out.println("Byte groups to write " + bytes.size());

        int size = 0;
        for (byte[] bs : bytes) {
            size += bs.length;
        }
        byte[] decodedData = new byte[size];
        int i = 0;
        for (byte[] bs : bytes) {
            for (byte b : bs) {
                decodedData[i++] = b;
            }
        }

        AudioSystem.write(new AudioInputStream(new ByteArrayInputStream(decodedData), OUTPUT_FORMAT, decodedData.length), AudioFileFormat.Type.WAVE, file);

        bytes.clear();
    }
}
