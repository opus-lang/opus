package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.Visitor.Visitable
public final class IgnoredExpressionStatementNode extends StatementNode implements Visitable{

    private final ExpressionNode expression;

    public IgnoredExpressionStatementNode(Position position, ExpressionNode expression) {
        super(position); // should not have annotations, because these should belong to expression itself.
        this.expression = expression;
    }

    public ExpressionNode expression() {
        return this.expression;
    }

}
