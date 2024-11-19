package dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.expressions;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.BinaryExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.ExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.MemberAccessExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPLEDRule;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPParserComponent;

public class MemberAccessExpressionRule extends TDOPLEDRule<ExpressionNode> {

    private final boolean isStatic;

    public MemberAccessExpressionRule(TDOPParserComponent<ExpressionNode> parserComponent, int lbp, boolean isStatic) {
        super(parserComponent, lbp, false);
        this.isStatic = isStatic;
    }

    @Override
    public MemberAccessExpressionNode led(ExpressionNode left) {
        MemberAccessExpressionNode.Builder nodeBuilder = new MemberAccessExpressionNode.Builder(this.parser.copyCurrentPosition())
                .isStatic(this.isStatic)
                .parent(left);

        this.parser
                .nextIfType(this.isStatic ? Token.Type.DOUBLECOLON : Token.Type.DOT)
                .orElseThrow(() -> new IllegalStateException("'.' or '::' was expected."));

        nodeBuilder.member(
                this.parser
                    .nextIfType(Token.Type.IDENTIFIER)
                    .orElseThrow(() -> new IllegalStateException("Identifier was expected."))
                    .value()
        );

        return nodeBuilder.build();
    }
}
