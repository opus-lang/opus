package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;


import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.LiteralKind;
import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.Visitor.Visitable
@Symphonia.Builder.Buildable(LiteralExpressionNode.Builder.class)
public abstract non-sealed class LiteralExpressionNode extends ExpressionNode implements Visitable{

    public abstract LiteralKind kind();
    public abstract String value();

    public static final class Builder extends LiteralExpressionNode_Builder{

        public Builder(Position position, Annotation[] annotations) {
            super(position, annotations);
        }

        public static Builder VOID(Builder builder){
            return builder.kind(LiteralKind.VOID).value("#");
        }

    }

}
