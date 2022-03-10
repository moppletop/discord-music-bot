package com.moppletop.discord.ui.menu;

import com.moppletop.discord.ui.MenuManager;
import com.moppletop.discord.ui.action.MenuAction;
import com.moppletop.discord.ui.action.SimpleAction;

import java.util.List;
import java.util.stream.Collectors;

public class SelectGuildMenu extends Menu {

    public SelectGuildMenu(MenuManager menuManager) {
        super(menuManager, "Guilds");
    }

    @Override
    public List<MenuAction> generateActions() {
        return getMenuManager().getDiscordBot().getJda().getGuilds().stream()
                .map(guild -> new SimpleAction(this, guild.getName(), () -> getMenuManager().setActiveMenu(new GuildMenu(this, guild))))
                .collect(Collectors.toList());
    }
}
