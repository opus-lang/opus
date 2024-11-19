package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.Visitor.Visitable
@Symphonia.Builder.Buildable(IgnoredExpressionStatementNode.Builder.class)
public abstract non-sealed class IgnoredExpressionStatementNode extends StatementNode implements Visitable{

    public abstract ExpressionNode expression();

    @Override
    public Annotation[] annotations() {
        return new Annotation[0];
    }

    public static final class Builder extends IgnoredExpressionStatementNode_Builder {

        public Builder(Position position) {
            super(position);
        }
    }

}
