package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.Operator;
import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.Visitor.Visitable
public final class UnaryExpressionNode extends ExpressionNode implements Visitable {

    private final Operator operator;
    private final ExpressionNode right;

    public UnaryExpressionNode(Position position, Operator operator, ExpressionNode right) {
        super(position);
        this.operator = operator;
        this.right = right;
    }

    public Operator operator() {
        return this.operator;
    }

    public ExpressionNode right() {
        return this.right;
    }

}
