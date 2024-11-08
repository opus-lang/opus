package dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.expressions;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.BlockExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.ExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.LoopExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.Node;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPNUDRule;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPParserComponent;

public class LoopExpressionRule extends TDOPNUDRule<ExpressionNode> {

    private final BlockExpressionRule blockExpressionRule;

    public LoopExpressionRule(TDOPParserComponent<ExpressionNode> parserComponent, int precedence, BlockExpressionRule blockExpressionRule) {
        super(parserComponent, precedence);
        this.blockExpressionRule = blockExpressionRule;
    }

    @Override
    public LoopExpressionNode nud() {
        Node.Position position = new Node.Position(this.parser.currentPosition());
        this.parser.startAnnotationCapture();

        this.parser
                .nextIfType(Token.Type.KEYWORD_LOOP)
                .orElseThrow(() -> new IllegalStateException("Keyword 'loop' expected."));

        BlockExpressionNode body = this.blockExpressionRule.nud();

        return this.parser.createNode((ignore, annotations) -> new LoopExpressionNode(position, annotations, body));
    }
}
