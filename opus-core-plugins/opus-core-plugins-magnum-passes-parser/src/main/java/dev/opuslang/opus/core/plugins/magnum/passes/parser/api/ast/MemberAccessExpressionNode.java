package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.Visitor.Visitable
@Symphonia.Builder.Buildable(MemberAccessExpressionNode.Builder.class)
public abstract non-sealed class MemberAccessExpressionNode extends ExpressionNode implements Visitable{

    public abstract ExpressionNode parent();
    public abstract String member();
    @Symphonia.Builder.Optional
    public abstract boolean isStatic();

    @Override
    public Annotation[] annotations() {
        return new Annotation[0];
    }

    public static final class Builder extends MemberAccessExpressionNode_Builder{

        public Builder(Position position) {
            super(position);
        }

    }

}
