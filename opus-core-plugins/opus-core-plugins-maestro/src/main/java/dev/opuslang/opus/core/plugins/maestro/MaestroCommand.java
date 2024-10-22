package dev.opuslang.opus.core.plugins.maestro;

import dev.opuslang.opus.api.plugin.service.PluginCommandService;
import picocli.CommandLine;

@CommandLine.Command(
        name = "maestro"
)
public class MaestroCommand implements PluginCommandService<Void> {
    @Override
    public Void call()  {
        return null;
    }
}
