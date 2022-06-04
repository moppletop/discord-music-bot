package com.moppletop.discord.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Getter
@Slf4j
public class TrackLibrary {

    private Track[] tracks;

    public TrackLibrary() {
       try {
           ObjectMapper objectMapper = new ObjectMapper();
           tracks = objectMapper.readValue(new File("preloaded.json"), Track[].class);
           log.info("Loaded {} tracks...", tracks.length);
       } catch (Exception ex) {
           tracks = new Track[0];
           log.error("Failed to load track library");
       }
    }
}
