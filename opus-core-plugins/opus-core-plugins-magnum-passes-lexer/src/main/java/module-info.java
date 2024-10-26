import dev.opuslang.opus.api.plugin.annotation.Plugin;

@Plugin(
        version = "0.1",
        name = "Magnum Lexer Pass"
)
module dev.opuslang.opus.core.plugins.magnum.lexer {
    requires static dev.opuslang.opus.symphonia;
    requires static dev.opuslang.opus.core.plugins.magnum;
    requires dev.opuslang.opus.api;
}