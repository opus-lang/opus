package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.Visitor.Visitable
public final class NeverTypeExpressionNode extends ExpressionNode implements Visitable{

    public NeverTypeExpressionNode(Position position) {
        super(position);
    }

}
