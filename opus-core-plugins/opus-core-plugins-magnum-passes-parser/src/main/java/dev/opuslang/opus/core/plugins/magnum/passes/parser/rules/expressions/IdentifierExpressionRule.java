package dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.expressions;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.ExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.IdentifierExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPNUDRule;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPParserComponent;

public class IdentifierExpressionRule extends TDOPNUDRule<ExpressionNode> {

    public IdentifierExpressionRule(TDOPParserComponent<ExpressionNode> parserComponent, int precedence) {
        super(parserComponent, precedence);
    }

    @Override
    public IdentifierExpressionNode nud() {
        this.parser.startAnnotationCapture();
        return this.parser.createNode((position, annotations) -> new IdentifierExpressionNode(position, annotations, this.parser
                .nextIfType(Token.Type.IDENTIFIER)
                .orElseThrow(() -> new IllegalStateException("Identifier expected."))
                .value()));
    }

}
