import dev.opuslang.opus.api.plugin.spi.PluginLoader;

module dev.opuslang.opus.api {
    requires java.compiler;
    requires static com.google.auto.service;
    requires com.google.gson;

    uses PluginLoader;

    exports dev.opuslang.opus.api.plugin.annotation;
    exports dev.opuslang.opus.api.plugin.spi;
    exports dev.opuslang.opus.api.plugin to dev.opuslang.opus.core;
    opens dev.opuslang.opus.api.plugin to com.google.gson;

    exports dev.opuslang.opus.api.service;
}