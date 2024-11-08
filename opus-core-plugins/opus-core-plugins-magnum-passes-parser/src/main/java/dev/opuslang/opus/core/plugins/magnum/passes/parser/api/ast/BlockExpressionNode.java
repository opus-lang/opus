package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.symphonia.annotation.Symphonia;

import java.util.Optional;

@Symphonia.Visitor.Visitable
public final class BlockExpressionNode extends ExpressionNode implements Visitable{

    private String label;
    private final StatementNode[] statements;

    public BlockExpressionNode(Position position, Annotation[] annotations, StatementNode[] statements){
        this(position, annotations, Optional.empty(), statements);
    }

    public BlockExpressionNode(Position position, Annotation[] annotations, Optional<String> label, StatementNode[] statements) {
        super(position, annotations);
        this.label = label.orElse(this.id().irIdentifier());
        this.statements = statements;
    }

    public String label() {
        return this.label;
    }
    public BlockExpressionNode setLabel(String label){
        this.label = label;
        return this;

    }

    public StatementNode[] statements() {
        return this.statements;
    }

}
