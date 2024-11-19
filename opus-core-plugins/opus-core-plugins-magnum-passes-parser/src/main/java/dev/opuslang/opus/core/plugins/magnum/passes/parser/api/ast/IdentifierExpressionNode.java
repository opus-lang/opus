package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.Visitor.Visitable
@Symphonia.Builder.Buildable(IdentifierExpressionNode.Builder.class)
public abstract non-sealed class IdentifierExpressionNode extends ExpressionNode implements Visitable {

    public abstract String identifier();

    public static final class Builder extends IdentifierExpressionNode_Builder {

        public Builder(Position position, Annotation[] annotations) {
            super(position, annotations);
        }
    }

}
