import dev.opuslang.opus.api.plugin.annotation.Plugin;
import dev.opuslang.opus.api.plugin.service.PluginCommandService;
import dev.opuslang.opus.core.plugins.maestro.MaestroCommand;

@Plugin(
        version = "0.1",
        name = "Maestro",
        description = "Project Manager",
        modulePath = "libs"
)
module dev.opuslang.opus.core.plugins.maestro {
    requires static dev.opuslang.opus.api;
    requires info.picocli;

    opens dev.opuslang.opus.core.plugins.maestro to info.picocli;
    provides PluginCommandService with MaestroCommand;
}