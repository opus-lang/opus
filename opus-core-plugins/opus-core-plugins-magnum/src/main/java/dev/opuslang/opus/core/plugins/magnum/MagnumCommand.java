package dev.opuslang.opus.core.plugins.magnum;

import dev.opuslang.opus.api.plugin.service.PluginCommandService;
import picocli.CommandLine;

@CommandLine.Command(
        name = "magnum"
)
public class MagnumCommand implements PluginCommandService<Void> {
    @Override
    public Void call() {
        return null;
    }
}
