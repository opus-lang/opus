package dev.opuslang.opus.api.plugin.processing;

import com.google.auto.service.AutoService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.opuslang.opus.api.plugin.PluginMetadata;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes("dev.opuslang.opus.api.plugin.annotation.Plugin")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class PluginMetadataGeneratorProcessor extends AbstractProcessor {

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
        this.messager.printNote("Generating Plugin Metadata...");
        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);

            for (Element element : annotatedElements) {
                Plugin pluginAnnotation = element.getAnnotation(Plugin.class);
                if(pluginAnnotation == null) continue;

                if(element instanceof ModuleElement moduleElement) {
                    String pluginId = moduleElement.getQualifiedName().toString();
                    this.messager.printNote("Plugin Module: " + pluginId);

                    List<String> internalDependencies = new ArrayList<>();

                    for (ModuleElement.Directive directive : moduleElement.getDirectives()) {
                        if (directive instanceof ModuleElement.RequiresDirective requiresDirective) {
                            if (requiresDirective.getDependency().getAnnotation(Plugin.class) == null)
                                continue; // Required module is not an internal dependency.

                            String internalDependencyId = requiresDirective.getDependency().getQualifiedName().toString();
                            this.messager.printNote("Internal Dependency found: " + internalDependencyId);
                            internalDependencies.add(internalDependencyId);
                        }
                    }

                    try {
                        FileObject fileObject = this.filer.createResource(
                                StandardLocation.CLASS_OUTPUT,
                                "",
                                "META-INF/PLUGIN.json"
                        );

                        try (Writer writer = fileObject.openWriter()) {
                            writer.write(
                                    this.gson.toJson(
                                            new PluginMetadata(
                                                    pluginId,
                                                    pluginAnnotation.name(),
                                                    pluginAnnotation.description(),
                                                    internalDependencies.toArray(new String[0])
                                            )
                                    )
                            );
                        }
                    } catch (IOException e) {
                        this.messager.printError("Error generating Plugin Metadata: " + e.getMessage());
                    }
                }
            }
        }

        return true;
    }

}
