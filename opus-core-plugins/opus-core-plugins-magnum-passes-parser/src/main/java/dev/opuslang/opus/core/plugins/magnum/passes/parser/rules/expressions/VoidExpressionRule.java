package dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.expressions;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.ExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.VoidExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPNUDRule;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPParserComponent;

public class VoidExpressionRule extends TDOPNUDRule<ExpressionNode> {

    public VoidExpressionRule(TDOPParserComponent<ExpressionNode> parserComponent, int precedence) {
        super(parserComponent, precedence);
    }

    @Override
    public VoidExpressionNode nud() {
        this.parser
                .nextIfType(Token.Type.VOID)
                .orElseThrow(() -> new IllegalStateException("Void expression was expected."));
        return this.parser.createNode(VoidExpressionNode::new);
    }

}
