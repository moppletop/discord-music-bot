package com.moppletop.discord.ui.menu;

import com.moppletop.discord.ui.MenuManager;
import com.moppletop.discord.ui.action.BackAction;
import com.moppletop.discord.ui.action.MenuAction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Slf4j
public abstract class Menu {

    private final MenuManager menuManager;

    private final Menu parent;
    private final String name;

    private List<MenuAction> actions;

    public Menu(MenuManager menuManager, String name) {
        this(menuManager, null, name);
    }

    public Menu(Menu parent, String name) {
        this(parent.getMenuManager(), parent, name);
    }

    public void refresh() {
        this.actions = new ArrayList<>();
        this.actions.add(parent == null ? null : new BackAction(this));
        this.actions.addAll(generateActions());

        log.info("{}", name);

        for (int i = 0; i < actions.size(); i++) {
            MenuAction action = actions.get(i);

            if (action == null) {
                continue;
            }

            log.info("{} - {}", i, action.getDescription());
        }

        log.info("Select an option:");
    }

    protected abstract List<MenuAction> generateActions();

}
