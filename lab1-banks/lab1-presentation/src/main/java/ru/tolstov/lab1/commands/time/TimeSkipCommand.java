package ru.tolstov.lab1.commands.time;

import picocli.CommandLine;

@CommandLine.Command(
        name = "skip",
        description = "Skips time, all account get their balances re-evaluated",
        mixinStandardHelpOptions = true
)
public class TimeSkipCommand implements Runnable {
    @CommandLine.ParentCommand
    private TimeCommand mainCmd;

    @CommandLine.Option(
            names = {"-d", "--days"},
            description = "Amount of days to skip",
            required = true
    )
    private int days;

    @Override
    public void run() {
        mainCmd.getCentralBankService().skipDays(days);
        System.out.printf("%s Days skipped%n", days);
    }
}
