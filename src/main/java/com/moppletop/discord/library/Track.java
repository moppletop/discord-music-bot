package com.moppletop.discord.library;

import lombok.Value;
import org.jetbrains.annotations.NotNull;

@Value
public class Track implements Comparable<Track> {

    String name;
    String url;

    @Override
    public int compareTo(@NotNull Track o) {
        return name.compareTo(o.name);
    }
}
