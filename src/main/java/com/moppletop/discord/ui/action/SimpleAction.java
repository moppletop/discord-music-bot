package com.moppletop.discord.ui.action;

import com.moppletop.discord.ui.menu.Menu;

public class SimpleAction extends MenuAction {

    private final String description;
    private final Runnable onSelected;

    public SimpleAction(Menu menu, String description, Runnable onSelected) {
        super(menu);

        this.description = description;
        this.onSelected = onSelected;
    }

    @Override
    public void selected(String[] args) {
        onSelected.run();
    }

    @Override
    public String getDescription() {
        return description;
    }
}
