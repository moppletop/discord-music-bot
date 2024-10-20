package com.moppletop.discord.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Arrays;

@Getter
@Slf4j
public class TrackLibrary {

    private Track[] tracks;

    public TrackLibrary() {
        ObjectMapper objectMapper = new ObjectMapper();

       try {
           tracks = objectMapper.readValue(new File("preloaded.json"), Track[].class);
       } catch (Exception ex) {
           tracks = new Track[0];
           log.error("Failed to load track library");
           return;
       }

        Arrays.sort(tracks);
        log.info("Loaded {} tracks...", tracks.length);
    }
}
