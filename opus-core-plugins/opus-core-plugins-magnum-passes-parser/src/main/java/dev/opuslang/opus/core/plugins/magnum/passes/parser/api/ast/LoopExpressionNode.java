package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.Visitor.Visitable
public final class LoopExpressionNode extends ExpressionNode implements Visitable {

    private final BlockExpressionNode body;

    public LoopExpressionNode(Position position, Annotation[] annotations, BlockExpressionNode body) {
        super(position, annotations);
        this.body = body;
    }

    public BlockExpressionNode body() {
        return this.body;
    }

}
