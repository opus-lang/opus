package dev.opuslang.opus.api.plugin;

import dev.opuslang.opus.api.plugin.annotation.Plugin;

public record PluginDescriptor(String id, PluginVersion version, String name, String description, String[] modulePath, PluginInternalDependency[] internalDependencies) {

    public static final String PATH = "META-INF/PLUGIN.json";

    public PluginDescriptor(String id, Plugin pluginAnnotation, PluginInternalDependency[] internalDependencies){
        this(id, PluginVersion.valueOf(pluginAnnotation.version()), pluginAnnotation.name(), pluginAnnotation.description(), pluginAnnotation.modulePath(), internalDependencies);
    }
}
