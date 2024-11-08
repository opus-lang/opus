package dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.statements;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.Parser;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.Operator;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.*;
import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.DI.Component(name = "ignoredExpressionOrAssignment")
public class IgnoredExpressionOrAssignmentStatementRule {

    @Symphonia.DI.Inject
    Parser parser;

    @Symphonia.DI.Inject
    EndStatementRule end;

    public StatementNode parse(){
        Node.Position position = new Node.Position(this.parser.currentPosition());
        ExpressionNode expression = this.parser.parseExpression();

        try {
            this.end.expect();
            return this.parser.createNode(ignore -> new IgnoredExpressionStatementNode(position, expression));
        }catch (Exception e){
            Operator operator = switch (this.parser.next().type()){
                case OPERATOR_WALRUS -> Operator.WALRUS;
                case OPERATOR_PLUSEQUALS -> Operator.PLUSEQUALS;
                case OPERATOR_MINUSEQUALS -> Operator.MINUSEQUALS;
                case OPERATOR_MULTIPLYEQUALS -> Operator.MULTIPLYEQUALS;
                case OPERATOR_DIVIDEEQUALS -> Operator.DIVIDEEQUALS;
                case OPERATOR_INTEGERDIVIDEEQUALS -> Operator.INTEGERDIVIDEEQUALS;
                case OPERATOR_POWEREQUALS -> Operator.POWEREQUALS;
                default -> throw new IllegalStateException("Illegal assignment operator.");
            };
            ExpressionNode value = this.parser.parseExpression();

            this.end.expect();
            return this.parser.createNode(ignore -> new AssignmentStatementNode(position, expression, operator, value));
        }
    }

}