package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.Visitor.Visitable
public final class VoidExpressionNode extends ExpressionNode implements Visitable{

    public VoidExpressionNode(Position position, Annotation[] annotations) {
        super(position, annotations);
    }

}
