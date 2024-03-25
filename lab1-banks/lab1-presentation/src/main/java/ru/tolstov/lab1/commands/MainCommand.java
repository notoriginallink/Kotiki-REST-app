package ru.tolstov.lab1.commands;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "main",
        description = "Main command to interact with application",
        mixinStandardHelpOptions = true
)
public class MainCommand implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        System.out.println("[Main]: Empty input. type -h or --help for help");
        return 0;
    }
}
