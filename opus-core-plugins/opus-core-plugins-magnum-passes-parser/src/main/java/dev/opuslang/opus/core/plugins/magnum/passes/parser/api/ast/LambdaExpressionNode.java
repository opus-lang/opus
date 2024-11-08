package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.Visitor.Visitable
public final class LambdaExpressionNode extends ExpressionNode implements Visitable{

    private final String[] argumentNames;
    private final BlockExpressionNode body;

    public LambdaExpressionNode(Position position, Annotation[] annotations, String[] argumentNames, BlockExpressionNode body) {
        super(position, annotations);
        this.argumentNames = argumentNames;
        this.body = body;
    }

    public String[] argumentNames() {
        return this.argumentNames;
    }

    public BlockExpressionNode body() {
        return this.body;
    }

}
