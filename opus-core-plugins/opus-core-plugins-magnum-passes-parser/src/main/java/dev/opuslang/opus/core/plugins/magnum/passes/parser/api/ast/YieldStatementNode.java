package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.symphonia.annotation.Symphonia;

import java.util.Optional;

@Symphonia.Visitor.Visitable
public final class YieldStatementNode extends StatementNode implements Visitable{

    private String label;
    private final ExpressionNode value;

    public YieldStatementNode(Position position, Annotation[] annotations, String label, ExpressionNode value) {
        super(position, annotations);
        this.label = label;
        this.value = value;
    }

    public String label() {
        return this.label;
    }

    public YieldStatementNode setLabel(String label){
        this.label = label;
        return this;
    }

    public ExpressionNode value() {
        return this.value;
    }

}
