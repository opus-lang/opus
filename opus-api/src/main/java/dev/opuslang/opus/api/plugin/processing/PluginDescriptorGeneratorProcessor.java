package dev.opuslang.opus.api.plugin.processing;

import com.google.auto.service.AutoService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.opuslang.opus.api.plugin.PluginDescriptor;
import dev.opuslang.opus.api.plugin.PluginInternalDependency;
import dev.opuslang.opus.api.plugin.PluginVersionRange;
import dev.opuslang.opus.api.plugin.annotation.Plugin;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ModuleElement;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

@AutoService(Processor.class)
@SupportedAnnotationTypes("dev.opuslang.opus.api.plugin.annotation.Plugin")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
@SuppressWarnings("unused")
public class PluginDescriptorGeneratorProcessor extends AbstractProcessor {

    private Messager messager;
    private Filer filer;
    private Gson gson;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.filer = processingEnv.getFiler();
        this.gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if(!annotations.isEmpty()) this.messager.printNote("Generating Plugin Descriptor...");
        Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(Plugin.class);

        for (Element element : annotatedElements) {
            Plugin pluginAnnotation = element.getAnnotation(Plugin.class);
            if(pluginAnnotation == null) continue;

            Plugin.InternalDependencyConfiguration[] pluginInternalDependencyConfigs = pluginAnnotation.internalDependencyConfigurations();
            Map<String, Plugin.InternalDependencyConfiguration> pluginInternalDependencyConfigMap = new HashMap<>(pluginInternalDependencyConfigs.length);
            for (Plugin.InternalDependencyConfiguration pluginInternalDependencyConfig : pluginInternalDependencyConfigs) {
                pluginInternalDependencyConfigMap.put(pluginInternalDependencyConfig.id(), pluginInternalDependencyConfig);
            }

            if(element instanceof ModuleElement moduleElement) {
                String pluginId = moduleElement.getQualifiedName().toString();
                this.messager.printNote("Plugin Module: " + pluginId);

                List<PluginInternalDependency> internalDependencies = new ArrayList<>();

                for (ModuleElement.Directive directive : moduleElement.getDirectives()) {
                    if (directive instanceof ModuleElement.RequiresDirective requiresDirective) {
                        Plugin internalDependencyAnnotation = requiresDirective.getDependency().getAnnotation(Plugin.class);
                        if (internalDependencyAnnotation == null)
                            continue; // Required module is not an internal dependency.

                        String internalDependencyId = requiresDirective.getDependency().getQualifiedName().toString();
                        Plugin.InternalDependencyConfiguration pluginInternalDependencyConfig = pluginInternalDependencyConfigMap.get(internalDependencyId);
                        this.messager.printNote("Internal Dependency found: " + internalDependencyId);
//                        if (!requiresDirective.isStatic()){
//                            this.messager.printError("Internal Dependencies must be static!");
//                        }
                        internalDependencies.add(
                                new PluginInternalDependency(
                                        internalDependencyId,
                                        PluginVersionRange.valueOf(pluginInternalDependencyConfig != null ? pluginInternalDependencyConfig.versionRange() : internalDependencyAnnotation.version())
                                )
                        );
                    }
                }

                try {
                    FileObject fileObject = this.filer.createResource(
                            StandardLocation.CLASS_OUTPUT,
                            "",
                            PluginDescriptor.PATH
                    );

                    try (Writer writer = fileObject.openWriter()) {
                        writer.write(
                                this.gson.toJson(
                                        new PluginDescriptor(
                                                pluginId,
                                                pluginAnnotation,
                                                internalDependencies.toArray(new PluginInternalDependency[0])
                                        )
                                )
                        );
                    }
                } catch (IOException e) {
                    this.messager.printError("Error generating Plugin Descriptor: " + e.getMessage());
                }
            }
        }

        return true;
    }

}
