package dev.opuslang.opus.core.cli;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "opus"
)
public class OpusCommand implements Callable<Void> {

    @CommandLine.Option(names = {"-d", "--debug"}, description = "Run in debug mode.")
    boolean debug;

    @Override
    public Void call() {
        return null;
    }

}
