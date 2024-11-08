import dev.opuslang.opus.api.plugin.annotation.Plugin;
import dev.opuslang.opus.core.plugins.magnum.api.pipeline.PassService;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.ParserPassService;

@Plugin(
        version = "0.1",
        name = "Magnum Parser Pass"
)
module dev.opuslang.opus.core.plugins.magnum.passes.parser {
    requires static dev.opuslang.opus.api;
    requires static dev.opuslang.opus.core.plugins.magnum;
    requires static dev.opuslang.opus.core.plugins.magnum.passes.lexer;
    requires static dev.opuslang.opus.symphonia;

    exports dev.opuslang.opus.core.plugins.magnum.passes.parser.api;
    exports dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

    provides PassService with ParserPassService;
}