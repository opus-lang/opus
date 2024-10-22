import dev.opuslang.opus.api.plugin.annotation.Plugin;
import dev.opuslang.opus.api.plugin.service.PluginCommandService;
import dev.opuslang.opus.core.plugins.magnum.MagnumCommand;

@Plugin(
        version = "0.1",
        name = "Magnum",
        description = "Compiler",
        modulePath = "libs"
)
module dev.opuslang.opus.core.plugins.magnum {
    requires static dev.opuslang.opus.api;
    requires info.picocli;

    opens dev.opuslang.opus.core.plugins.magnum to info.picocli;
    provides PluginCommandService with MagnumCommand;
}