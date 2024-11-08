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
        Node.Position position = new Node.Position(this.parser.currentPosition());
        this.parser.startAnnotationCapture();

        this.parser
                .nextIfType(Token.Type.DOLLAR)
                .orElseThrow(() -> new IllegalStateException("Block expression must always start with a '$'."));

        Optional<String> label = this.parser
                .nextIfType(Token.Type.IDENTIFIER)
                .map(Token::value);

        this.parser
                .nextIfType(Token.Type.LCURLY)
                .orElseThrow(() -> new IllegalStateException("Start of a block expected."));

        List<StatementNode> statements = new ArrayList<>();
        while(this.parser.peek().type() != Token.Type.RCURLY){
            statements.add(this.parser.parseStatement());
        }
        this.parser.consume();

        return this.parser.createNode((ignore, annotations) -> new BlockExpressionNode(position, annotations, label, statements.toArray(StatementNode[]::new)));
    }

}
