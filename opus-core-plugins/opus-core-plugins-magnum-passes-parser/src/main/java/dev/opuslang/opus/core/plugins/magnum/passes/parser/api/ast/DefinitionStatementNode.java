package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.symphonia.annotation.Symphonia;

import java.util.EnumSet;
import java.util.Optional;

@Symphonia.Visitor.Visitable
public final class DefinitionStatementNode extends StatementNode implements Visitable{

    public enum Modifier{
        CONST, MUTABLE,
    }

    private final EnumSet<Modifier> modifiers;
    private final String name;
    private final ExpressionNode type;
    private final Optional<ExpressionNode> assignedValue;

    public DefinitionStatementNode(Position position, Annotation[] annotations, EnumSet<Modifier> modifiers, String name, ExpressionNode type, Optional<ExpressionNode> assignedValue) {
        super(position, annotations);
        this.modifiers = modifiers;
        this.name = name;
        this.type = type;
        this.assignedValue = assignedValue;
    }

    public EnumSet<Modifier> modifiers() {
        return this.modifiers;
    }

    public String name() {
        return this.name;
    }

    public ExpressionNode type() {
        return this.type;
    }

    public Optional<ExpressionNode> assignedValue() {
        return this.assignedValue;
    }

}
