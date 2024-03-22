package ru.tolstov.commands;

import picocli.CommandLine;

@CommandLine.Command(
        name = "exit",
        description = "Stops application",
        mixinStandardHelpOptions = true
)
public class ExitCommand implements Runnable {
    @Override
    public void run() {
        System.exit(0);
    }
}
