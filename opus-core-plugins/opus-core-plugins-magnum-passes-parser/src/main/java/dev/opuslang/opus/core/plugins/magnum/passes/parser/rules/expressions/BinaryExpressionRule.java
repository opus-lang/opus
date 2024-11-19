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
        BinaryExpressionNode.Builder nodeBuilder = new BinaryExpressionNode.Builder(this.parser.copyCurrentPosition())
                .left(left)
                .operator(operator);

        this.parser
                .nextIfType(tokenOperator)
                .orElseThrow(() -> new IllegalStateException("Operator " + tokenOperator.name() + " was expected."));

        nodeBuilder.right(this.parseWithAssociativity());

        return nodeBuilder.build();
    }

}
