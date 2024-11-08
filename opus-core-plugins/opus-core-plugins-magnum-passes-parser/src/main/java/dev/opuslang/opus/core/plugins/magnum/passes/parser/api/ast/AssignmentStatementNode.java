package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.Operator;
import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.Visitor.Visitable
public final class AssignmentStatementNode extends StatementNode implements Visitable {

    private final ExpressionNode target;
    private final Operator operator;
    private final ExpressionNode value;

    public AssignmentStatementNode(Position position, ExpressionNode target, Operator operator, ExpressionNode value){
        super(position);
        this.target = target;
        this.operator = operator;
        this.value = value;
    }

    public ExpressionNode target() {
        return this.target;
    }

    public Operator operator(){
        return this.operator;
    }

    public ExpressionNode value() {
        return this.value;
    }
}
