package ru.tolstov.commands.create;

import picocli.CommandLine;

@CommandLine.Command(
        name = "create",
        description = "Allows to create new banks, clients and accounts",
        mixinStandardHelpOptions = true
)
public class CreateCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("[Create]: Type -h or --help for help");
    }
}
