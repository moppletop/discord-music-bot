package com.moppletop.discord.ui.action;

import com.moppletop.discord.ui.menu.Menu;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class MenuAction {

    private final Menu menu;

    public abstract void selected(String[] args);

    public abstract String getDescription();

}
