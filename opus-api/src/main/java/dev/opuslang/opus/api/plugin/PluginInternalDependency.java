package dev.opuslang.opus.api.plugin;

import dev.opuslang.opus.api.plugin.annotation.Plugin;

public record PluginInternalDependency(String id, PluginVersionRange versionRange) {
    public PluginInternalDependency(String id, Plugin.InternalDependencyConfiguration configurationAnnotation) {
        this(id, PluginVersionRange.valueOf(configurationAnnotation.versionRange()));
    }
}
