package dev.opuslang.opus.core.plugins.magnum.pipeline;

import dev.opuslang.opus.core.plugins.magnum.api.pipeline.IndependentPassService;
import dev.opuslang.opus.core.plugins.magnum.api.pipeline.PassContext;
import dev.opuslang.opus.core.plugins.magnum.api.pipeline.SynchronizedPassService;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;

public class PipelineExecutor {

    private static final String[] EMPTY_ARGUMENTS = new String[0];

    private final String target;
    private final Pipeline pipeline;

    private record PipelineSector(PassData[] passes, boolean isIndependent){}
    private final List<PipelineSector> pipelineSectors;

    public PipelineExecutor(Pipeline pipeline, String target) {
        if(!pipeline.passes().containsKey(target)) {
            throw new IllegalArgumentException(String.format("Target Pass '%s' does not exist in the pipeline.", target));
        }

        this.pipeline = pipeline;
        this.target = target;

        Set<String> mask = this.mask(target);
        PassData[] masked = Arrays.stream(pipeline.sorted())
                .filter(mask::contains) // Skip irrelevant to the target passes.
                .map(passId -> pipeline.passes().get(passId))
                .toArray(PassData[]::new);

        this.pipelineSectors = PipelineExecutor.divideInSectors(masked);
    }

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

    // Divides the pipeline in sectors of consecutive tasks with the same type.
    private static List<PipelineSector> divideInSectors(PassData[] passes){
        List<PipelineSector> sectors = new ArrayList<>();
        if(passes.length <= 0)
            return sectors;

        List<PassData> buffer = new ArrayList<>();
        buffer.add(passes[0]);

        for(int i = 1; i < passes.length; i++) {
            if(passes[i].passService().isIndependent() == buffer.getLast().passService().isIndependent()){
                buffer.add(passes[i]);
            }else{
                sectors.add(new PipelineSector(buffer.toArray(PassData[]::new), buffer.getLast().passService().isIndependent()));
                buffer.clear();
                buffer.add(passes[i]);
            }
        }

        // Add last sector.
        sectors.add(new PipelineSector(buffer.toArray(PassData[]::new), buffer.getLast().passService().isIndependent()));
        return sectors;
    }


    public void execute(List<File> files, String[] args){
        try(ExecutorService executorService = Executors.newFixedThreadPool(files.size())){
            Map<File, PassContext> fullContextByFile = new ConcurrentHashMap<>();
            files.forEach(f -> fullContextByFile.put(f, new PassContext()));

            for(PipelineSector sector : this.pipelineSectors){
                if(sector.isIndependent()){
                    List<Future<PassContext>> futures = new ArrayList<>(files.size());

                    for (File file : files) {
                        Future<PassContext> future = executorService.submit(() -> {
                            PassContext fullContext = fullContextByFile.get(file);
                            for(PassData pass : sector.passes()){
                                PassContext isolatedContext = new PassContext();
                                // Provide only the required context.
                                for(String dependency : pass.configuration().dependencies()){
                                    isolatedContext.put(dependency, fullContext.rawGet(dependency));
                                }
                                Object result = ((IndependentPassService<?>) pass.passService()).execute(file, isolatedContext, pass.configuration().id().equals(this.target) ? args : EMPTY_ARGUMENTS);
                                fullContext.put(pass.configuration().id(), result);
                            }
                            return fullContext;
                        });
                        futures.add(future);
                    }

                    // Wait for all futures to complete.
                    for (int i = 0; i < files.size(); i++) {
                        File file = files.get(i);
                        PassContext results = futures.get(i).get();
                        fullContextByFile.put(file, results); // TODO: change to "putAll" if too slow.
                    }
                }else{
                    for (PassData pass : sector.passes()) {
                        Map<File, PassContext> isolatedContextByFile = new HashMap<>(fullContextByFile.size());

                        // Provide only the required context.
                        for(String dependency : pass.configuration().dependencies()){
                            fullContextByFile.forEach((key, value) -> isolatedContextByFile.computeIfAbsent(key, k -> new PassContext()).put(dependency, value.rawGet(dependency)));
                        }

                        Object result = ((SynchronizedPassService<?>) pass.passService()).execute(files, isolatedContextByFile, pass.configuration().id().equals(this.target) ? args : EMPTY_ARGUMENTS);
                        for(PassContext fullContext : fullContextByFile.values()){
                            fullContext.put(pass.configuration().id(), result); // Pass the result to all contexts.
                        }
                    }
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e); // TODO: better error handling.
        }
    }
}
