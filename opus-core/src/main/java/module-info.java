import dev.opuslang.opus.api.plugin.service.PluginCommandService;

module dev.opuslang.opus.core {
    requires com.google.gson;
    requires dev.opuslang.opus.api;
    requires dev.opuslang.opus.utils;
    requires info.picocli;

    uses PluginCommandService;
}