import dev.opuslang.opus.api.plugin.service.PluginCommandService;
import dev.opuslang.opus.api.plugin.spi.PluginLoader;
import dev.opuslang.opus.core.plugin.OpusPluginLoader;


module dev.opuslang.opus.core {
    requires com.google.gson;
    requires dev.opuslang.opus.api;
    requires dev.opuslang.opus.utils;
    requires info.picocli;

    opens dev.opuslang.opus.core.cli to info.picocli;

    provides PluginLoader with OpusPluginLoader;

    uses PluginCommandService;
}