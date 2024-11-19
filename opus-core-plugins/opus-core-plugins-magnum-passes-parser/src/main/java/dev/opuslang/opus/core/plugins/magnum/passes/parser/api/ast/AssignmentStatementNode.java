package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.Operator;
import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.Visitor.Visitable
@Symphonia.Builder.Buildable(AssignmentStatementNode.Builder.class)
public abstract non-sealed class AssignmentStatementNode extends StatementNode implements Visitable {

    public abstract ExpressionNode target();
    public abstract Operator operator();
    public abstract ExpressionNode value();

    @Override
    public Annotation[] annotations() {
        return new Annotation[0];
    }

    public static final class Builder extends AssignmentStatementNode_Builder{

        public Builder(Position position) {
            super(position);
        }

    }

}
