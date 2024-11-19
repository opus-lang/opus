package dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.statements;

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
        Node.Position position = this.parser.copyCurrentPosition();
        ExpressionNode expression = this.parser.parseExpression();

        try {
            this.end.expect();
            return new IgnoredExpressionStatementNode.Builder(position)
                    .expression(expression)
                    .build();
        }catch (EndStatementRule.MissingSemicolonException e){
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

            return new AssignmentStatementNode.Builder(position)
                    .target(expression)
                    .operator(operator)
                    .value(value)
                    .build();
        }
    }

}