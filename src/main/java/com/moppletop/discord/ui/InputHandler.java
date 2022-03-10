package com.moppletop.discord.ui;

import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

@Slf4j
public class InputHandler implements Runnable {

    private final InputCallback callback;
    private final Scanner scanner;

    public InputHandler(InputCallback callback) {
        this.callback = callback;
        this.scanner = new Scanner(System.in);

        Thread thread = new Thread(this);
        thread.setName("input");
        thread.setDaemon(false);
        thread.start();
    }

    @Override
    public void run() {
        String line;

        while ((line = pollInput()) != null) {
            try {
                if (!callback.handleInput(line)) {
                    return;
                }
            } catch (Exception ex) {
                log.error("An error occurred when processing input", ex);
            }
        }
    }

    public String pollInput() {
        return scanner.nextLine();
    }
}
