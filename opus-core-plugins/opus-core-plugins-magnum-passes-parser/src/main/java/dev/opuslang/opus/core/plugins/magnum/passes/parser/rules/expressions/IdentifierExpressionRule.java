package dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.expressions;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.ExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.IdentifierExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.Node;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPNUDRule;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPParserComponent;

// TODO: Merge with the Literals
public class IdentifierExpressionRule extends TDOPNUDRule<ExpressionNode> {

    public IdentifierExpressionRule(TDOPParserComponent<ExpressionNode> parserComponent, int precedence) {
        super(parserComponent, precedence);
    }

    @Override
    public IdentifierExpressionNode nud() {
        IdentifierExpressionNode.Builder nodeBuilder = new IdentifierExpressionNode.Builder(this.parser.copyCurrentPosition(), new Node.Annotation[0]);
        this.parser.startAnnotationCapture();

        nodeBuilder.identifier(
                this.parser
                        .nextIfType(Token.Type.IDENTIFIER)
                        .orElseThrow(() -> new IllegalStateException("Identifier expected."))
                        .value()
        );

        return nodeBuilder.build();
    }

}
