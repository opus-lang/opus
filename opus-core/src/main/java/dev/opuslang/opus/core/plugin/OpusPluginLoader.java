package dev.opuslang.opus.core.plugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.opuslang.opus.api.plugin.PluginDescriptor;
import dev.opuslang.opus.api.plugin.PluginInternalDependency;
import dev.opuslang.opus.api.plugin.spi.PluginLoader;
import dev.opuslang.opus.api.plugin.spi.PluginServiceLoaderHandler;
import dev.opuslang.opus.utils.TopologicalSort;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.module.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public enum OpusPluginLoader implements PluginLoader {
    INSTANCE();

    public static PluginLoader provider() { // Special method in JPMS. Takes precedence over non-argument constructor during loading.
        return OpusPluginLoader.INSTANCE;
    }

    private final Gson gson;
    private Collection<ModuleLayer> plugins;

    OpusPluginLoader(){
        this.gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create();
    }

    public void loadPlugins(String pluginFolderPath){
        if(this.plugins != null){
            throw new IllegalStateException("Plugins can only be loaded once!");
        }
        Path[] pluginDirectories = Arrays.stream(
                Optional.ofNullable(new File(pluginFolderPath).listFiles(File::isDirectory)).orElse(new File[0])
        ).map(File::toPath).toArray(Path[]::new);

        // General plugin finder
        ModuleFinder pluginFinder = ModuleFinder.of(pluginDirectories);
        Set<ModuleReference> pluginReferences = pluginFinder.findAll();

        // An empty layer that is a child of the boot layer
        ModuleLayer isolationLayer = ModuleLayer.boot();

        record IndexedPlugin(ModuleReference reference, PluginDescriptor descriptor, Path folder){}
        Map<String, IndexedPlugin> indexedPluginById = new HashMap<>(pluginReferences.size());
        TopologicalSort<String> pluginSorter = new TopologicalSort<>(pluginReferences.size());

        for(ModuleReference pluginReference : pluginReferences){
            assert pluginReference.location().isPresent(); // must never happen
            this.readPluginDescriptor(pluginReference).ifPresentOrElse(pluginDescriptor -> {
                indexedPluginById.put(pluginDescriptor.id(), new IndexedPlugin(pluginReference, pluginDescriptor, Path.of(pluginReference.location().get()).getParent()));
                pluginSorter.addVertex(pluginDescriptor.id(), Arrays.stream(pluginDescriptor.internalDependencies()).map(PluginInternalDependency::id).collect(Collectors.toSet()));
            }, () -> {
                throw new IllegalStateException("Unable to load plugin: " + pluginReference);
            });
        }

        Map<String, ModuleLayer> pluginLayerById = new HashMap<>(pluginReferences.size());
        for(String pluginId : pluginSorter.sort()){
            IndexedPlugin indexedPlugin = indexedPluginById.get(pluginId);

            // Find all the external dependencies for the plugin (dependencies that are bundled with the plugin)
            ModuleFinder pluginExternalDependenciesFinder = ModuleFinder.of(Arrays.stream(indexedPlugin.descriptor().modulePath()).map(subPath -> indexedPlugin.folder().resolve(subPath)).toArray(Path[]::new));
            List<String> pluginExternalDependencies = pluginExternalDependenciesFinder.findAll()
                    .stream()
                    .filter(descriptor -> !descriptor.equals(indexedPlugin.reference())) // exclude the current module if exists.
                    .map(ModuleReference::descriptor)
                    .map(ModuleDescriptor::name)
                    .toList();
            Configuration pluginExternalDependenciesConfiguration = isolationLayer.configuration().resolve(pluginExternalDependenciesFinder, ModuleFinder.of(), pluginExternalDependencies);
            ModuleLayer pluginExternalDependenciesLayer = isolationLayer.defineModulesWithOneLoader(pluginExternalDependenciesConfiguration, ClassLoader.getSystemClassLoader());

            // Find all the internal dependencies for the plugin (dependencies on the other plugins)
            List<ModuleLayer> layers = Arrays.stream(indexedPlugin.descriptor().internalDependencies()).map(internalDependency -> {
                IndexedPlugin internalIndexedPlugin = indexedPluginById.get(internalDependency.id());
                if (!internalDependency.versionRange().contains(internalIndexedPlugin.descriptor().version())) {
                    throw new IllegalStateException("Incompatible plugin versions.");
                }
                return pluginLayerById.get(internalDependency.id());
            }).collect(Collectors.toList());
            // Remember to add the external dependencies layer.
            layers.add(pluginExternalDependenciesLayer);
            List<Configuration> configurations = layers.stream().map(ModuleLayer::configuration).toList();

            // Create a new configuration that is a child of all the required dependencies
            Configuration pluginConfiguration = Configuration.resolve(pluginFinder, configurations, ModuleFinder.of(), Collections.singleton(pluginId));
            ModuleLayer pluginLayer = ModuleLayer.defineModulesWithOneLoader(pluginConfiguration, layers, ClassLoader.getSystemClassLoader()).layer();
            pluginLayerById.put(pluginId, pluginLayer);
        }

        this.plugins = pluginLayerById.values();
    }

    public <T> List<T> loadServices(PluginServiceLoaderHandler<T> serviceLoaderHandler, Class<T> serviceClass){
        List<T> result = new ArrayList<>(this.plugins.size());
        for(ModuleLayer layer : this.plugins){
            result.addAll(loadModulesFromLayer(serviceLoaderHandler, layer, serviceClass));
        }
        return result;
    }

    private Optional<PluginDescriptor> readPluginDescriptor(ModuleReference pluginReference){
        try(ModuleReader pluginResourceReader = pluginReference.open()){
            try(InputStream pluginDescriptorInputStream = pluginResourceReader.open(PluginDescriptor.PATH).orElse(InputStream.nullInputStream())){
                try{
                    return Optional.of(this.gson.fromJson(new String(pluginDescriptorInputStream.readAllBytes(), StandardCharsets.UTF_8), PluginDescriptor.class));
                }catch (Exception e){
                    return Optional.empty();
                }
            }
        }catch (IOException e){
            return Optional.empty();
        }
    }

    private static <T> List<T> loadModulesFromLayer(PluginServiceLoaderHandler<T> serviceLoaderQuery, ModuleLayer layer, Class<T> clazz){
        List<T> services = new ArrayList<>();
        Iterable<T> a = serviceLoaderQuery.get(layer, clazz);
        for(T service : a){
            if(!service.getClass().getModule().getLayer().equals(layer)) break;
            services.add(service);
        }
        return services;
    }

}
