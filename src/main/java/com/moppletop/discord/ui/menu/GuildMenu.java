package com.moppletop.discord.ui.menu;

import com.moppletop.discord.ui.action.*;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Arrays;
import java.util.List;

public class GuildMenu extends Menu {

    private final Guild guild;

    public GuildMenu(Menu parent, Guild guild) {
        super(parent, guild.getName());

        this.guild = guild;
    }

    @Override
    protected List<MenuAction> generateActions() {
        return Arrays.asList(
                resumeAction(),
                pauseAction(),
                disconnectAction(),
                selectChannelAction(),
                new VolumeAction(this, guild),
                new PlayURLAction(this, guild),
                selectTrackAction(),
                new RecordAction(this, guild),
                new SaveRecordingAction(this, guild)
        );
    }

    private MenuAction resumeAction() {
        return new SimpleAction(this, "Resume", () -> getMenuManager().getDiscordBot().getMusicManager().getGuildAudioPlayer(guild).resume());
    }

    private MenuAction pauseAction() {
        return new SimpleAction(this, "Pause", () -> getMenuManager().getDiscordBot().getMusicManager().getGuildAudioPlayer(guild).pause());
    }

    private MenuAction disconnectAction() {
        return new SimpleAction(this, "Disconnect", () -> getMenuManager().getDiscordBot().getMusicManager().destroyGuildAudioPlayer(guild));
    }

    private MenuAction selectChannelAction() {
        return new SimpleAction(this, "Select Channel", () -> getMenuManager().setActiveMenu(new SelectChannelMenu(this, guild)));
    }

    private MenuAction selectTrackAction() {
        return new SimpleAction(this, "Play Track", () -> getMenuManager().setActiveMenu(new PlayTrackLibraryMenu(this, guild)));
    }

}
