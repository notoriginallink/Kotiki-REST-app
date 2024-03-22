package ru.tolstov.commands.time;

import picocli.CommandLine;

@CommandLine.Command(
        name = "Current",
        aliases = {"now", "cur"},
        description = "Print current time",
        mixinStandardHelpOptions = true
)
public class TimeCurrentCommand implements Runnable {
    @CommandLine.ParentCommand
    private TimeCommand mainCmd;

    @Override
    public void run() {
        System.out.println(mainCmd.getCentralBankService().getCurrentDate().getTime());
    }
}
