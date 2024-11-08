package dev.opuslang.opus.core.plugins.magnum.pipeline;

import dev.opuslang.opus.core.plugins.magnum.api.pipeline.PassService;
import dev.opuslang.opus.core.plugins.magnum.api.pipeline.annotation.PassConfiguration;

public record PassData(PassService<?> passService, Configuration configuration) {

    public record Configuration(String name, String id, String[] dependencies){
        public Configuration(PassConfiguration configurationAnnotation){
            this(configurationAnnotation.name().isBlank() ? configurationAnnotation.id() : configurationAnnotation.name(), configurationAnnotation.id(), configurationAnnotation.dependencies());
        }
    }

}
