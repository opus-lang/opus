import dev.opuslang.opus.api.plugin.annotation.Plugin;
import dev.opuslang.opus.core.plugins.magnum.api.pipeline.PassService;
import dev.opuslang.opus.core.plugins.magnum.passes.importcollector.ImportCollectorPassService;

@Plugin(
        version = "0.1",
        name = "Magnum Import Collector Pass"
)
module dev.opuslang.opus.core.plugins.magnum.passes.importcollector {
    requires static dev.opuslang.opus.api;
    requires static dev.opuslang.opus.core.plugins.magnum;
    requires static dev.opuslang.opus.core.plugins.magnum.passes.parser;

    provides PassService with ImportCollectorPassService;
}