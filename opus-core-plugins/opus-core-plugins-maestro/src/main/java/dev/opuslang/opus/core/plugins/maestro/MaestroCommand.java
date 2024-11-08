package dev.opuslang.opus.core.plugins.maestro;

import dev.opuslang.opus.api.service.SubcommandService;
import picocli.CommandLine;

@CommandLine.Command(
        name = "maestro"
)
public class MaestroCommand implements SubcommandService<Void> {
    @Override
    public Void call()  {
        return null;
    }
}
