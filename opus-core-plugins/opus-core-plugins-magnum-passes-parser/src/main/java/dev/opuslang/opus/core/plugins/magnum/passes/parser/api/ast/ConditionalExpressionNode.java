package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.core.plugins.magnum.passes.parser.Parser;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.utils.Either;
import dev.opuslang.opus.symphonia.annotation.Symphonia;

import java.util.Optional;

@Symphonia.Visitor.Visitable
@Symphonia.Builder.Buildable(ConditionalExpressionNode.Builder.class)
public abstract non-sealed class ConditionalExpressionNode extends ExpressionNode implements Visitable{

    public abstract ExpressionNode condition();
    public abstract BlockExpressionNode body();
    public abstract BlockExpressionNode elseExpression();

    public static final class Builder extends ConditionalExpressionNode_Builder{

        public Builder(Position position, Annotation[] annotations) {
            super(position, annotations);
        }

        public Builder(Position position){
            this(position, new Annotation[0]);
        }

        public Builder defaultElseExpression(){
            BlockExpressionNode.Builder bodyBuilder = new BlockExpressionNode.Builder(position).generatedLabel();
            bodyBuilder.statements(new StatementNode[]{
                    new YieldStatementNode.Builder(position)
                            .label(bodyBuilder.label())
                            .defaultValue()
                            .build()
            });
            this.elseExpression = bodyBuilder.build();
            return this;
        }
    }

}
