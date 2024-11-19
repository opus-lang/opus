package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.core.plugins.magnum.passes.parser.utils.Utils;
import dev.opuslang.opus.symphonia.annotation.Symphonia;

import java.util.Optional;

@Symphonia.Visitor.Visitable
@Symphonia.Builder.Buildable(BlockExpressionNode.Builder.class)
public abstract non-sealed class BlockExpressionNode extends ExpressionNode implements Visitable{

    public abstract String label();
    public abstract StatementNode[] statements();

    public static final class Builder extends BlockExpressionNode_Builder {

        public Builder(Position position, Annotation[] annotations) {
            super(position, annotations);
        }

        public Builder(Position position){
            this(position, new Annotation[0]);
        }

        public Builder(BlockExpressionNode other) {
            super(other);
        }

        public Builder generatedLabel(){
            this.label = Utils.generateUniqueIdentifier();
            return this;
        }

    }

}
