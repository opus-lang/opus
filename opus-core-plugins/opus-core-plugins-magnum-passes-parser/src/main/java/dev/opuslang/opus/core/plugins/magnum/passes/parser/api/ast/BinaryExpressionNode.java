package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.Operator;
import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.Visitor.Visitable
@Symphonia.Builder.Buildable(BinaryExpressionNode.Builder.class)
public abstract non-sealed class BinaryExpressionNode extends ExpressionNode implements Visitable{

    public abstract ExpressionNode left();
    public abstract Operator operator();
    public abstract ExpressionNode right();

    @Override
    public Annotation[] annotations() {
        return new Annotation[0];
    }

    public static final class Builder extends BinaryExpressionNode_Builder {

        public Builder(Position position) {
            super(position);
        }
    }

}
