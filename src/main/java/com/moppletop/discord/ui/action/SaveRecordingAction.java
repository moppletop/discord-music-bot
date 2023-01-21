package com.moppletop.discord.ui.action;

import com.moppletop.discord.ui.menu.Menu;
import net.dv8tion.jda.api.entities.Guild;

public class SaveRecordingAction extends MenuAction {

    private final Guild guild;

    public SaveRecordingAction(Menu menu, Guild guild) {
        super(menu);

        this.guild = guild;
    }

    @Override
    public void selected(String[] args) {
        getMenu().getMenuManager().getDiscordBot().getMusicManager().saveRecording(guild, args[0]);
    }

    @Override
    public String getDescription() {
        return "Save Record";
    }
}
