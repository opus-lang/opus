package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.Visitor.Visitable
@Symphonia.Builder.Buildable(EmptyStatementNode.Builder.class)
public abstract non-sealed class EmptyStatementNode extends StatementNode implements Visitable{

    @Override
    public Annotation[] annotations() {
        return new Annotation[0];
    }

    public static final class Builder extends EmptyStatementNode_Builder{

        public Builder(Position position) {
            super(position);
        }
    }

}
