package dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.expressions;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.Operator;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.BinaryExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.ExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.Node;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPLEDRule;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPParserComponent;

public class BinaryExpressionRule extends TDOPLEDRule<ExpressionNode> {

    private final Token.Type tokenOperator;
    private final Operator operator;

    public BinaryExpressionRule(TDOPParserComponent<ExpressionNode> parserComponent, int lbp, boolean rightAssociative, Token.Type tokenOperator, Operator operator) {
        super(parserComponent, lbp, rightAssociative);
        this.tokenOperator = tokenOperator;
        this.operator = operator;
    }

    @Override
    public BinaryExpressionNode led(ExpressionNode left) {
        Node.Position position = new Node.Position(this.parser.currentPosition());
        this.parser
                .nextIfType(tokenOperator)
                .orElseThrow(() -> new IllegalStateException("Operator " + tokenOperator.name() + " was expected."));

        ExpressionNode right = this.parser.parseExpression();

        return this.parser.createNode(ignore -> new BinaryExpressionNode(position, left, this.operator, right));
    }

}
