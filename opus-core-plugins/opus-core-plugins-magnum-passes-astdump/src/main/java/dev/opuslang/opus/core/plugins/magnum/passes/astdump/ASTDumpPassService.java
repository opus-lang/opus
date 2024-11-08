package dev.opuslang.opus.core.plugins.magnum.passes.astdump;

import dev.opuslang.opus.core.plugins.magnum.api.pipeline.IndependentPassService;
import dev.opuslang.opus.core.plugins.magnum.api.pipeline.PassContext;
import dev.opuslang.opus.core.plugins.magnum.api.pipeline.annotation.PassConfiguration;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.SourceFile;

import java.io.File;

@PassConfiguration(
        id = "astdump",
        dependencies = { "parse" }
)
public class ASTDumpPassService extends IndependentPassService<Void> {
    @Override
    public Void execute(File file, PassContext context, String[] args) {
        SourceFile ast = context.get("parse", SourceFile.class);
        ASTDumpVisitor astDumpVisitor = new ASTDumpVisitor(file);
        System.out.println(astDumpVisitor.visit(ast));
        return null;
    }
}
