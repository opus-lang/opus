package dev.opuslang.opus.core.plugins.magnum.pipeline;

import dev.opuslang.opus.core.plugins.magnum.api.pipeline.PassService;
import dev.opuslang.opus.core.plugins.magnum.api.pipeline.annotation.PassConfiguration;
import dev.opuslang.opus.utils.TopologicalSort;

import java.util.*;

public record Pipeline(String[] sorted, Map<String, PassData> passes) {

    public static class Builder{

        private final Map<String, PassData> passes = new HashMap<>();

        public Builder(){}

        public Builder registerPass(PassService<?> passService){
            PassConfiguration configurationAnnotation = passService.getClass().getAnnotation(PassConfiguration.class);
            if(configurationAnnotation == null) throw new IllegalArgumentException("Pass should have a configuration!");
            PassData.Configuration configuration = new PassData.Configuration(configurationAnnotation);
            this.passes.put(configuration.id(), new PassData(passService, configuration));
            return this;
        }

        public Pipeline build(){
            TopologicalSort<String> sorter = new TopologicalSort<>(this.passes.size());
            for(Map.Entry<String, PassData> entry :  this.passes.entrySet()){
                sorter.addVertex(entry.getKey(), Set.of(entry.getValue().configuration().dependencies()));
            }
            return new Pipeline(sorter.sort().toArray(String[]::new), Collections.unmodifiableMap(this.passes));
        }

    }

}
