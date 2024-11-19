package dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.expressions;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.Operator;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.ExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.Node;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.UnaryExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPNUDRule;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPParserComponent;

public class UnaryExpressionRule extends TDOPNUDRule<ExpressionNode> {

    private final Token.Type tokenOperator;
    private final Operator operator;

    public UnaryExpressionRule(TDOPParserComponent<ExpressionNode> parserComponent, int precedence, Token.Type tokenOperator, Operator operator) {
        super(parserComponent, precedence);
        this.tokenOperator = tokenOperator;
        this.operator = operator;
    }

    @Override
    public UnaryExpressionNode nud() {
        UnaryExpressionNode.Builder nodeBuilder = new UnaryExpressionNode.Builder(this.parser.copyCurrentPosition())
                .operator(this.operator);

        this.parser
                .nextIfType(tokenOperator)
                .orElseThrow(() -> new IllegalStateException("Operator " + tokenOperator.name() + " was expected."));

        nodeBuilder.right(this.parseWithAssociativity());
        return nodeBuilder.build();
    }

}
