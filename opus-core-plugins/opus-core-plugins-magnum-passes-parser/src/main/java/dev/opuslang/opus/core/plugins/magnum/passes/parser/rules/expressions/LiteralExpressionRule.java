package dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.expressions;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.LiteralKind;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.Operator;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.ExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.LiteralExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.Node;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPNUDRule;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPParserComponent;

public class LiteralExpressionRule extends TDOPNUDRule<ExpressionNode> {

    private final Token.Type tokenLiteral;
    private final LiteralKind kind;

    public LiteralExpressionRule(TDOPParserComponent<ExpressionNode> parserComponent, int precedence, Token.Type tokenLiteral, LiteralKind kind) {
        super(parserComponent, precedence);
        this.tokenLiteral = tokenLiteral;
        this.kind = kind;
    }

    @Override
    public LiteralExpressionNode nud() {
        LiteralExpressionNode.Builder nodeBuilder = new LiteralExpressionNode.Builder(this.parser.copyCurrentPosition(), new Node.Annotation[0]).kind(this.kind);

        nodeBuilder.value(this.parser
                .nextIfType(this.tokenLiteral)
                .orElseThrow(() -> new IllegalStateException("Literal expected."))
                .value()
        );
        return nodeBuilder.build();
    }

}
