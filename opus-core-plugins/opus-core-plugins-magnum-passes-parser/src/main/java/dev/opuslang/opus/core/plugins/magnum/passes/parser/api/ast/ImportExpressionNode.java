package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.Visitor.Visitable
@Symphonia.Builder.Buildable(ImportExpressionNode.Builder.class)
public abstract non-sealed class ImportExpressionNode extends ExpressionNode implements Visitable{

    public static final String OPUS_IMPORT_PROCESSOR = "opus";

    public abstract String path();
    public abstract String processor();

    @Override
    public Annotation[] annotations() {
        return new Annotation[0];
    }

    public static final class Builder extends ImportExpressionNode_Builder {

        public Builder(Position position) {
            super(position);
        }
    }

}
