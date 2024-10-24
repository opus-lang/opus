package dev.opuslang.opus.core.plugins.magnum.pipeline;

import dev.opuslang.opus.core.plugins.magnum.api.pipeline.PassContext;

import java.util.*;

public record PipelineExecutor(Pipeline pipeline) {

    private static final String[] EMPTY_ARGUMENTS = new String[0];

    private Set<String> mask(String target){
        Set<String> mask = new HashSet<>();
        mask.add(target);

        Queue<String> processingQueue = new LinkedList<>();
        processingQueue.add(target);

        while (!processingQueue.isEmpty()) {
            String current = processingQueue.poll();
            for (String dependency : this.pipeline.passes().get(current).configuration().dependencies()) {
                if (mask.add(dependency)) { // If was not processed yet.
                    processingQueue.add(dependency);
                }
            }
        }

        return mask;
    }

    public void execute(String target, String[] args){
        if(!this.pipeline.passes().containsKey(target)) {
            throw new IllegalArgumentException(String.format("Pass '%s' does not exist.", target));
        }
        PassContext fullContext = new PassContext();
        Set<String> mask = this.mask(target);
        for(String passId : this.pipeline.sorted()){
            if(!mask.contains(passId)) continue; // Skip irrelevant to the target passes.
            PassContext isolatedContext = new PassContext();
            PassData pass = this.pipeline.passes().get(passId);
            // Provide only the required context.
            for(String dependency : pass.configuration().dependencies()){
                isolatedContext.put(dependency, fullContext.rawGet(dependency));
            }
            Object result = pass.passService().execute(isolatedContext, passId.equals(target) ? args : EMPTY_ARGUMENTS);
            fullContext.put(pass.configuration().id(), result);
        }
    }
}
