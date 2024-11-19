package dev.opuslang.opus.core.plugins.magnum.passes.analyzer.importgraph;

import dev.opuslang.opus.core.plugins.magnum.api.pipeline.PassContext;
import dev.opuslang.opus.core.plugins.magnum.api.pipeline.SynchronizedPassService;
import dev.opuslang.opus.core.plugins.magnum.api.pipeline.annotation.PassConfiguration;
import dev.opuslang.opus.utils.TopologicalSort;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@PassConfiguration(
        id = "analyzer-importgraph",
        dependencies = { "import-collect" }
)
public class AnalyzerImportGraphPassService extends SynchronizedPassService<String[]> {
    @Override
    public Map<File, String[]> execute(List<File> files, Map<File, PassContext> contexts, String[] args) {
        TopologicalSort<String> importGraphSorter = new TopologicalSort<>();
        for(Map.Entry<File, PassContext> ctx : contexts.entrySet()) {
            File[] dependencies = ctx.getValue().get("import-collect", File[].class);
            Set<String> dependenciesSet = new HashSet<>(Arrays.stream(dependencies).map(AnalyzerImportGraphPassService::normalizePath).toList());
            importGraphSorter.addVertex(normalizePath(ctx.getKey()), dependenciesSet);
        }
        Map<File, String[]> result = new HashMap<>(contexts.size());
        String[] sorted = importGraphSorter.sort().toArray(String[]::new);
        for(File file : files){
            result.put(file, sorted);
        }
        return result;
    }

    public static String normalizePath(File file){
        return Paths.get(file.getAbsolutePath()).normalize().toString();
    }

}
