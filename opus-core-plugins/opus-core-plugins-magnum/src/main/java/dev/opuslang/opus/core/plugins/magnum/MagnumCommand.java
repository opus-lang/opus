package dev.opuslang.opus.core.plugins.magnum;

import dev.opuslang.opus.api.service.SubcommandService;
import picocli.CommandLine;

@CommandLine.Command(
        name = "magnum"
)
public class MagnumCommand implements SubcommandService<Void> {
    @Override
    public Void call() {
        return null;
    }
}
