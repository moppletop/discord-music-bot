package com.moppletop.discord.ui.action;

import com.moppletop.discord.ui.menu.Menu;
import net.dv8tion.jda.api.entities.Guild;

public class PlayURLAction extends MenuAction {

    private final Guild guild;

    public PlayURLAction(Menu menu, Guild guild) {
        super(menu);

        this.guild = guild;
    }

    @Override
    public void selected(String[] args) {
        getMenu().getMenuManager().getDiscordBot().getMusicManager().play(guild, args[0]);
    }

    @Override
    public String getDescription() {
        return "Play URL";
    }
}
