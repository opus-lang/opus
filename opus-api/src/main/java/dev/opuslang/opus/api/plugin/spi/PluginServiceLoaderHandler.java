package dev.opuslang.opus.api.plugin.spi;

import java.util.ServiceLoader;

@FunctionalInterface
public interface PluginServiceLoaderHandler<T> {

    ServiceLoader<T> get(ModuleLayer layer, Class<T> clazz);

}
