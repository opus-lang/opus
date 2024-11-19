import dev.opuslang.opus.api.plugin.annotation.Plugin;
import dev.opuslang.opus.core.plugins.magnum.api.pipeline.PassService;
import dev.opuslang.opus.core.plugins.magnum.passes.analyzer.importgraph.AnalyzerImportGraphPassService;

@Plugin(
        version = "0.1",
        name = "Magnum Analyzer for Imports Pass"
)
module dev.opuslang.opus.core.plugins.magnum.passes.analyzer.importgraph {
    requires static dev.opuslang.opus.api;
    requires static dev.opuslang.opus.core.plugins.magnum;
    requires static dev.opuslang.opus.core.plugins.magnum.passes.parser;

    requires dev.opuslang.opus.utils;

    provides PassService with AnalyzerImportGraphPassService;
}