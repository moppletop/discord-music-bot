package com.moppletop.discord.ui.menu;

import com.moppletop.discord.ui.action.MenuAction;
import com.moppletop.discord.ui.action.SimpleAction;
import net.dv8tion.jda.api.entities.Guild;

import java.util.List;
import java.util.stream.Collectors;

public class SelectChannelMenu extends Menu {

    private final Guild guild;

    public SelectChannelMenu(Menu parent, Guild guild) {
        super(parent, "Select Channel");

        this.guild = guild;
    }

    @Override
    protected List<MenuAction> generateActions() {
        return guild.getVoiceChannels().stream()
                .map(voiceChannel -> new SimpleAction(this, voiceChannel.getName(), () -> {
                    guild.getAudioManager().openAudioConnection(voiceChannel);
                    getMenuManager().setActiveMenu(getParent());
                }))
                .collect(Collectors.toList());
    }
}
