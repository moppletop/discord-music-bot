package com.moppletop.discord.ui.menu;

import com.moppletop.discord.ui.action.MenuAction;
import com.moppletop.discord.ui.action.SimpleAction;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PlayTrackLibraryMenu extends Menu{

    private final Guild guild;

    public PlayTrackLibraryMenu(Menu parent, Guild guild) {
        super(parent, "Track Library");

        this.guild = guild;
    }

    @Override
    protected List<MenuAction> generateActions() {
        return Arrays.stream(getMenuManager().getDiscordBot().getTrackLibrary().getTracks())
                .map(track -> new SimpleAction(this, track.getName(), () -> {
                    getMenuManager().getDiscordBot().getMusicManager().play(guild, track.getUrl());
                    refresh();
                }))
                .collect(Collectors.toList());
    }

}
