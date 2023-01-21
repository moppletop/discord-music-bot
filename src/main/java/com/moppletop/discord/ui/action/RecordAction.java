package com.moppletop.discord.ui.action;

import com.moppletop.discord.ui.menu.Menu;
import net.dv8tion.jda.api.entities.Guild;

public class RecordAction extends MenuAction {

    private final Guild guild;

    public RecordAction(Menu menu, Guild guild) {
        super(menu);

        this.guild = guild;
    }

    @Override
    public void selected(String[] args) {
        getMenu().getMenuManager().getDiscordBot().getMusicManager().record(guild);
    }

    @Override
    public String getDescription() {
        return "Record";
    }
}
