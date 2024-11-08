package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.Visitor.Visitable
public final class ImportExpressionNode extends ExpressionNode implements Visitable{

    public static final String OPUS_IMPORT_PROCESSOR = "opus";

    private final String path;
    private final String processor;

    public ImportExpressionNode(Position position, String path, String processor) {
        super(position);
        this.path = path;
        this.processor = processor;
    }

    public String path() {
        return this.path;
    }

    public String processor() {
        return this.processor;
    }
}
