package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.Operator;
import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.Visitor.Visitable
@Symphonia.Builder.Buildable(UnaryExpressionNode.Builder.class)
public abstract non-sealed class UnaryExpressionNode extends ExpressionNode implements Visitable {

    public abstract Operator operator();
    public abstract ExpressionNode right();

    @Override
    public final Annotation[] annotations(){
        return new Annotation[0];
    }

    public static final class Builder extends UnaryExpressionNode_Builder{
        public Builder(Position position) {
            super(position);
        }
    }

}
