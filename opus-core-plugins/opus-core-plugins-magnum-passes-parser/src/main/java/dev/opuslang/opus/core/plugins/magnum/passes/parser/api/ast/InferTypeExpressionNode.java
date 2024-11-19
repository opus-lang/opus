package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.Visitor.Visitable
@Symphonia.Builder.Buildable(InferTypeExpressionNode.Builder.class)
public abstract non-sealed class InferTypeExpressionNode extends ExpressionNode implements Visitable{

    @Override
    public Annotation[] annotations() {
        return new Annotation[0];
    }

    public static final class Builder extends InferTypeExpressionNode_Builder {
        public Builder(Position position) {
            super(position);
        }
    }

}
