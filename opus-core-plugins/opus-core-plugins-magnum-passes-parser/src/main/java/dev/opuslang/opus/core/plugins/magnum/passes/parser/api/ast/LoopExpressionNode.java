package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.Visitor.Visitable
@Symphonia.Builder.Buildable(LoopExpressionNode.Builder.class)
public abstract non-sealed class LoopExpressionNode extends ExpressionNode implements Visitable {

    public abstract BlockExpressionNode body();

    public static final class Builder extends LoopExpressionNode_Builder{

        public Builder(Position position, Annotation[] annotations) {
            super(position, annotations);
        }
    }

}
