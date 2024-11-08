package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.Operator;
import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.Visitor.Visitable
public final class BinaryExpressionNode extends ExpressionNode implements Visitable{

    private final ExpressionNode left;
    private final Operator operator;
    private final ExpressionNode right;

    public BinaryExpressionNode(Position position, ExpressionNode left, Operator operator, ExpressionNode right) {
        super(position);
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public ExpressionNode left() {
        return this.left;
    }

    public Operator operator() {
        return this.operator;
    }

    public ExpressionNode right() {
        return this.right;
    }
}
