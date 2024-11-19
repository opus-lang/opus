package dev.opuslang.opus.core.plugins.magnum.passes.importcollector;

import dev.opuslang.opus.core.plugins.magnum.api.pipeline.IndependentPassService;
import dev.opuslang.opus.core.plugins.magnum.api.pipeline.PassContext;
import dev.opuslang.opus.core.plugins.magnum.api.pipeline.annotation.PassConfiguration;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.SourceFile;

import java.io.File;


@PassConfiguration(
        id = "import-collect",
        dependencies = { "parse" }
)
public class ImportCollectorPassService extends IndependentPassService<File[]> {
    @Override
    public File[] execute(File file, PassContext context, String[] args) {
        SourceFile sourceFile = context.get("parse",  SourceFile.class);
        ImportCollectorVisitor importCollectorVisitor = new ImportCollectorVisitor(file);
        importCollectorVisitor.visit(sourceFile);
        return importCollectorVisitor.collectedImports().toArray(File[]::new);
    }
}
