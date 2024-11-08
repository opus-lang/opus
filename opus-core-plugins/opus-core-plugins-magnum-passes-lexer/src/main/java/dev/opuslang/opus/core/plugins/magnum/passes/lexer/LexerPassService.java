package dev.opuslang.opus.core.plugins.magnum.passes.lexer;

import dev.opuslang.opus.core.plugins.magnum.api.pipeline.IndependentPassService;
import dev.opuslang.opus.core.plugins.magnum.api.pipeline.PassContext;
import dev.opuslang.opus.core.plugins.magnum.api.pipeline.annotation.PassConfiguration;
import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Cursor;
import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.TokenStream;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@PassConfiguration(
        id = "lex"
)
public class LexerPassService extends IndependentPassService<TokenStream> {

    @Override
    public TokenStream execute(File file, PassContext context, String[] args) {
        try {
            return new LexerTokenStream(new Lexer(new SourceScanner(
                    Files.readString(file.toPath(), StandardCharsets.UTF_8),
                    new Cursor()
            )));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
