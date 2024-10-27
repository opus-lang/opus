import dev.opuslang.opus.api.plugin.annotation.Plugin;
import dev.opuslang.opus.core.plugins.magnum.api.pipeline.PassService;
import dev.opuslang.opus.core.plugins.magnum.passes.lexer.LexerPassService;

@Plugin(
        version = "0.1",
        name = "Magnum Lexer Pass"
)
module dev.opuslang.opus.core.plugins.magnum.lexer {
    requires static dev.opuslang.opus.symphonia;
    requires static dev.opuslang.opus.core.plugins.magnum;
    requires static dev.opuslang.opus.api;

    exports dev.opuslang.opus.core.plugins.magnum.passes.lexer.api;
    provides PassService with LexerPassService;
}