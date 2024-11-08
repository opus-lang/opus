package dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.expressions;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.ExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.NeverTypeExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPNUDRule;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPParserComponent;

public class NeverExpressionRule extends TDOPNUDRule<ExpressionNode> {

    public NeverExpressionRule(TDOPParserComponent<ExpressionNode> parserComponent, int precedence) {
        super(parserComponent, precedence);
    }

    @Override
    public ExpressionNode nud() {
        this.parser
                .nextIfType(Token.Type.KEYWORD_NEVER)
                .orElseThrow(() -> new IllegalStateException("Keyword 'never' was expected."));
        return this.parser.createNode(NeverTypeExpressionNode::new);
    }

}
