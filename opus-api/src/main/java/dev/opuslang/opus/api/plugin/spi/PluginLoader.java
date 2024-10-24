package dev.opuslang.opus.api.plugin.spi;

import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;

public interface PluginLoader {

    static PluginLoader getInstance(){
        Set<PluginLoader> pluginLoaders = ServiceLoader.load(ModuleLayer.boot(), PluginLoader.class).stream().map(ServiceLoader.Provider::get).collect(Collectors.toSet());
        if(pluginLoaders.size() > 1){
            throw new IllegalStateException("Multiple PluginLoaders found!");
        }
        return pluginLoaders.stream().findFirst().orElseThrow();
    }

    void loadPlugins(String pluginFolderPath);
    <T> List<T> loadServices(PluginServiceLoaderHandler<T> serviceLoaderHandler, Class<T> serviceClass);

}
