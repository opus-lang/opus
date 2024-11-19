package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.symphonia.annotation.Symphonia;

import java.util.EnumSet;
import java.util.Optional;

@Symphonia.Visitor.Visitable
@Symphonia.Builder.Buildable(DefinitionStatementNode.Builder.class)
public abstract non-sealed class DefinitionStatementNode extends StatementNode implements Visitable{

    public enum Modifier{
        CONST, MUTABLE,
    }

    public abstract EnumSet<Modifier> modifiers();
    public abstract String name();
    public abstract ExpressionNode type();
    @Symphonia.Builder.Optional
    public abstract ExpressionNode assignedValue();

    public static final class Builder extends DefinitionStatementNode_Builder{

        public Builder(Position position, Annotation[] annotations) {
            super(position, annotations);
        }

    }

}
