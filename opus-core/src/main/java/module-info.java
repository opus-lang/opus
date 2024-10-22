import dev.opuslang.opus.api.plugin.service.PluginCommandService;

module dev.opuslang.opus.core {
    requires com.google.gson;
    requires dev.opuslang.opus.api;
    requires dev.opuslang.opus.utils;
    requires info.picocli;

    opens dev.opuslang.opus.core.cli to info.picocli;

    uses PluginCommandService;
}