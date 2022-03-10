package com.moppletop.discord.ui;

import com.moppletop.discord.DiscordBot;
import com.moppletop.discord.ui.action.MenuAction;
import com.moppletop.discord.ui.menu.SelectGuildMenu;
import com.moppletop.discord.ui.menu.Menu;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MenuManager implements InputCallback {

    @Getter
    private final DiscordBot discordBot;

    private Menu activeMenu;

    public MenuManager(DiscordBot discordBot) {
        this.discordBot = discordBot;

        new InputHandler(this);
        setActiveMenu(new SelectGuildMenu(this));
    }

    @Override
    public boolean handleInput(String input) {
        if (activeMenu == null || input == null || input.isEmpty()) {
            return true;
        }

        if (input.equalsIgnoreCase("exit")) {
            discordBot.shutdown();
            return false;
        }

        String[] args = input.split("\\s");

        if (args.length == 0) {
            return true;
        }

        int option;

        try {
            option = Integer.parseInt(args[0]);

            if (option < 0 || option > activeMenu.getActions().size()) {
                log.info("Ensure your selected option is valid!");
                return true;
            }
        } catch (NumberFormatException ex) {
            log.info("Ensure your selected option is valid!");
            return true;
        }

        String[] newArgs = new String[args.length - 1];
        System.arraycopy(args, 1, newArgs, 0, newArgs.length);

        MenuAction action = activeMenu.getActions().get(option);

        if (action == null) {
            return true;
        }

        action.selected(newArgs);
        return true;
    }

    public void setActiveMenu(Menu menu) {
        this.activeMenu = menu;

        menu.refresh();
    }

}
