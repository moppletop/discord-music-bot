package com.moppletop.discord.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Getter
@Slf4j
public class Config {

    private final ConfigProperties properties;

    public Config(ObjectMapper objectMapper) {
        log.info("Loading config from file...");

        try {
            this.properties = objectMapper.readValue(new File("config.json"), ConfigProperties.class);
        } catch (IOException e) {
            log.error("Failed to read config.", e);
            throw new RuntimeException(e);
        }
    }
}
