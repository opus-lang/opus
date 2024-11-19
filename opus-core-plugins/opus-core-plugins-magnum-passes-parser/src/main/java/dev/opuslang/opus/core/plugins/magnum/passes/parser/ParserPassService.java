package dev.opuslang.opus.core.plugins.magnum.passes.parser;

import dev.opuslang.opus.core.plugins.magnum.api.pipeline.IndependentPassService;
import dev.opuslang.opus.core.plugins.magnum.api.pipeline.PassContext;
import dev.opuslang.opus.core.plugins.magnum.api.pipeline.annotation.PassConfiguration;
import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.TokenStream;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.SourceFile;

import java.io.File;

@PassConfiguration(
        id = "parse",
        dependencies = { "lex" }
)
public class ParserPassService extends IndependentPassService<SourceFile> {
    @Override
    public SourceFile execute(File file, PassContext context, String[] args) {
        TokenStream tokenStream = context.get("lex", TokenStream.class);
        Parser parser = new Parser(tokenStream);
        SourceFile parsedFile = parser.parse();
//        YieldRepairVisitor yieldRepairVisitor = new YieldRepairVisitor();
//        yieldRepairVisitor.visit(parsedFile);
        return parsedFile;
    }
}
