package com.moppletop.discord.ui.action;

import com.moppletop.discord.ui.menu.Menu;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;

@Slf4j
public class VolumeAction extends MenuAction {

    private final Guild guild;

    public VolumeAction(Menu menu, Guild guild) {
        super(menu);

        this.guild = guild;
    }

    @Override
    public void selected(String[] args) {
        if (args.length == 0) {
            log.info("No volume specified");
            return;
        }

        int volume;

        try {
            volume = Integer.parseInt(args[0]);

            if (volume <= 0 || volume > 100) {
                log.info("Volume must be between 1 and 100");
                return;
            }
        } catch (NumberFormatException ex) {
            log.info("Volume must be a number between 1 and 100");
            return;
        }

        getMenu().getMenuManager().getDiscordBot().getMusicManager().getGuildAudioPlayer(guild).setVolume(volume);

        log.info("Set volume to {}", volume);

        getMenu().refresh();
    }

    @Override
    public String getDescription() {
        return "Set Volume";
    }
}
