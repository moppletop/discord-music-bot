package com.moppletop.discord.ui.action;

import com.moppletop.discord.ui.menu.Menu;

public class BackAction extends MenuAction {

    public BackAction(Menu menu) {
        super(menu);
    }

    @Override
    public void selected(String[] args) {
        Menu menu = getMenu();

        if (menu.getParent() == null) {
            menu.getMenuManager().getDiscordBot().shutdown();
        } else {
            menu.getMenuManager().setActiveMenu(menu.getParent());
        }
    }

    @Override
    public String getDescription() {
        return "Go Back";
    }
}
