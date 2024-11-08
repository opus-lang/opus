import dev.opuslang.opus.api.plugin.annotation.Plugin;
import dev.opuslang.opus.api.service.SubcommandService;
import dev.opuslang.opus.core.plugins.magnum.MagnumCommand;
import dev.opuslang.opus.core.plugins.magnum.api.pipeline.PassService;

@Plugin(
        version = "0.1",
        name = "Magnum",
        description = "Compiler",
        modulePath = "libs"
)
module dev.opuslang.opus.core.plugins.magnum {
    requires static dev.opuslang.opus.api;

    requires java.compiler;
    requires dev.opuslang.opus.utils;
    requires info.picocli;

    exports dev.opuslang.opus.core.plugins.magnum.api.pipeline;
    exports dev.opuslang.opus.core.plugins.magnum.api.pipeline.annotation;

    opens dev.opuslang.opus.core.plugins.magnum to info.picocli;

    uses PassService;

    provides SubcommandService with MagnumCommand;
}