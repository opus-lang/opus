package dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.expressions;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.*;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPNUDRule;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPParserComponent;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.utils.Reference;

import java.util.Optional;

public class ConditionalExpressionRule extends TDOPNUDRule<ExpressionNode> {

    private final BlockExpressionRule blockExpressionRule;

    public ConditionalExpressionRule(TDOPParserComponent<ExpressionNode> parserComponent, int precedence, BlockExpressionRule blockExpressionRule) {
        super(parserComponent, precedence);
        this.blockExpressionRule = blockExpressionRule;
    }

    @Override
    public ConditionalExpressionNode nud() {
        Node.Position position = new Node.Position(this.parser.currentPosition());

        this.parser
                .nextIfType(Token.Type.KEYWORD_IF)
                .orElseThrow(() -> new IllegalStateException("Keyword 'if' was expected."));

        this.parser
                .nextIfType(Token.Type.LPARENTHESIS)
                .orElseThrow(() -> new IllegalStateException("Opening parenthesis for a conditional expression was expected."));

        ExpressionNode condition = this.parser.parseExpression();

        this.parser
                .nextIfType(Token.Type.RPARENTHESIS)
                .orElseThrow(() -> new IllegalStateException("Closing parenthesis for a conditional expression was expected."));

        BlockExpressionNode body = this.blockExpressionRule.nud();

        BlockExpressionNode elseExpression = this.parser
                .nextIfType(Token.Type.KEYWORD_ELSE)
                .map(tokenElse -> this.parser
                                    .nextIfType(Token.Type.KEYWORD_IF)
                                    .map(tokenElseIf ->
                                        this.parser.createNode((position1, annotations1) -> new BlockExpressionNode(
                                                position1,
                                                annotations1,
                                                new StatementNode[]{
                                                        this.parser.createNode((position2, annotations2) -> new YieldStatementNode(position2, annotations2, null, this.nud()))
                                                })
                                        )
                                    )
                                    .orElse(this.blockExpressionRule.nud())
                ).orElse(
                        this.parser.createNode((position1, annotations1) ->
                                new BlockExpressionNode(
                                        position1,
                                        annotations1,
                                        new StatementNode[]{
                                                this.parser.createNode((position2, annotations2) -> new YieldStatementNode(
                                                        position2,
                                                        annotations2,
                                                        null,
                                                        this.parser.createNode(VoidExpressionNode::new)
                                                ))
                                        }
                                )
                        )
                );

        return this.parser.createNode((ignore, annotations) -> new ConditionalExpressionNode(position, annotations, condition, body, elseExpression));
    }
}
