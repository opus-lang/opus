package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.Visitor.Visitable
@Symphonia.Builder.Buildable(LambdaExpressionNode.Builder.class)
public abstract non-sealed class LambdaExpressionNode extends ExpressionNode implements Visitable{

    public abstract String[] argumentNames();
    public abstract BlockExpressionNode body();

    public static final class Builder extends LambdaExpressionNode_Builder{

        public Builder(Position position, Annotation[] annotations) {
            super(position, annotations);
        }
    }

}
