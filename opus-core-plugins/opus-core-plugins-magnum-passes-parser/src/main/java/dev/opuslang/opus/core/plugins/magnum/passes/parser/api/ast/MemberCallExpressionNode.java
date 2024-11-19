package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.symphonia.annotation.Symphonia;

import java.util.List;

@Symphonia.Visitor.Visitable
@Symphonia.Builder.Buildable(MemberCallExpressionNode.Builder.class)
public abstract non-sealed class MemberCallExpressionNode extends ExpressionNode implements Visitable{

    public abstract ExpressionNode callable();
    public abstract ExpressionNode[] arguments();

    @Override
    public Annotation[] annotations() {
        return new Annotation[0];
    }

    public static final class Builder extends MemberCallExpressionNode_Builder{

        public Builder(Position position) {
            super(position);
        }

    }

}
