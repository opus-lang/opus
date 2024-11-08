package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.core.plugins.magnum.passes.parser.Parser;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.utils.Either;
import dev.opuslang.opus.symphonia.annotation.Symphonia;

import java.util.Optional;

@Symphonia.Visitor.Visitable
public final class ConditionalExpressionNode extends ExpressionNode implements Visitable{

    private final ExpressionNode condition;
    private final BlockExpressionNode body;
    private final BlockExpressionNode elseExpression;

    public ConditionalExpressionNode(Position position, Annotation[] annotations, ExpressionNode condition, BlockExpressionNode body, BlockExpressionNode elseExpression) {
        super(position, annotations);
        this.condition = condition;
        this.body = body;
        this.elseExpression = elseExpression;
    }

    public ExpressionNode condition() {
        return this.condition;
    }

    public BlockExpressionNode body() {
        return this.body;
    }

    public BlockExpressionNode elseExpression() {
        return this.elseExpression;
    }

}
