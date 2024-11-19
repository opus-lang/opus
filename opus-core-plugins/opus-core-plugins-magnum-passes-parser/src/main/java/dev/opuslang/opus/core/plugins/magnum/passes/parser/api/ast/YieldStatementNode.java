package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.symphonia.annotation.Symphonia;

import java.util.Optional;

@Symphonia.Visitor.Visitable
@Symphonia.Builder.Buildable(YieldStatementNode.Builder.class)
public abstract non-sealed class YieldStatementNode extends StatementNode implements Visitable{

    @Symphonia.Builder.Optional
    public abstract String label();
    public abstract ExpressionNode value();

    public static final class Builder extends YieldStatementNode_Builder{
        public Builder(Position position, Annotation[] annotations) {
            super(position, annotations);
        }

        public Builder(Position position){
            this(position, new Annotation[0]);
        }

        public Builder defaultValue(){
            this.value = LiteralExpressionNode.Builder.VOID(new LiteralExpressionNode.Builder(this.position, new Annotation[0])).build();
            return this;
        }
    }

}
