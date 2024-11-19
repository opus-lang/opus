package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.Visitor.Visitable
@Symphonia.Builder.Buildable(FunctionTypeExpressionNode.Builder.class)
public abstract non-sealed class FunctionTypeExpressionNode extends ExpressionNode implements Visitable{

    public abstract ExpressionNode[] argumentTypes();
    public abstract ExpressionNode returnType();

    public static final class Builder extends FunctionTypeExpressionNode_Builder{

        public Builder(Position position, Annotation[] annotations) {
            super(position, annotations);
        }
    }

}