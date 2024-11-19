package dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.expressions;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.BlockExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.ExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.Node;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.StatementNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPNUDRule;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPParserComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BlockExpressionRule extends TDOPNUDRule<ExpressionNode> {

    public BlockExpressionRule(TDOPParserComponent<ExpressionNode> parserComponent, int precedence) {
        super(parserComponent, precedence);
    }

    @Override
    public BlockExpressionNode nud() {
        BlockExpressionNode.Builder nodeBuilder = new BlockExpressionNode.Builder(this.parser.copyCurrentPosition(), new Node.Annotation[0]);

        this.parser
                .nextIfType(Token.Type.DOLLAR)
                .orElseThrow(() -> new IllegalStateException("Block expression must always start with a '$'."));

        this.parser
                .nextIfType(Token.Type.IDENTIFIER)
                .ifPresentOrElse(
                        label -> nodeBuilder.label(label.value()),
                        nodeBuilder::generatedLabel
                );

        this.parser.labelsStack().push(nodeBuilder.label());

        this.parser
                .nextIfType(Token.Type.LCURLY)
                .orElseThrow(() -> new IllegalStateException("Start of a block expected."));

        List<StatementNode> statements = new ArrayList<>();
        while(this.parser.peek().type() != Token.Type.RCURLY){
            statements.add(this.parser.parseStatement());
        }
        this.parser.consume();

        nodeBuilder.statements(statements.toArray(StatementNode[]::new));

        this.parser.labelsStack().pop();

        return nodeBuilder.build();
    }

}
