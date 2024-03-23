package ru.tolstov.lab1.commands.time;

import lombok.Getter;
import picocli.CommandLine;
import ru.tolstov.lab1.banks.CentralBankService;

@CommandLine.Command(
        name = "time",
        description = "Provides interface to work with time",
        mixinStandardHelpOptions = true
)
@Getter
public class TimeCommand implements Runnable {
    private final CentralBankService centralBankService;

    public TimeCommand(CentralBankService centralBankService) {
        this.centralBankService = centralBankService;
    }

    @Override
    public void run() {
        System.out.println("[Time] Type -h or --help for help");
    }
}
