package dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.expressions;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.Operator;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.*;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPNUDRule;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPParserComponent;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.utils.Reference;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WhileLoopExpressionRule extends TDOPNUDRule<ExpressionNode> {

    private final BlockExpressionRule blockExpressionRule;

    public WhileLoopExpressionRule(TDOPParserComponent<ExpressionNode> parserComponent, int precedence, BlockExpressionRule blockExpressionRule) {
        super(parserComponent, precedence);
        this.blockExpressionRule = blockExpressionRule;
    }

    @Override
    public LoopExpressionNode nud() {
        Node.Position position = new Node.Position(this.parser.currentPosition());

        this.parser
                .nextIfType(Token.Type.KEYWORD_WHILE)
                .orElseThrow(() -> new IllegalStateException("Keyword 'while' was expected."));

        this.parser
                .nextIfType(Token.Type.LPARENTHESIS)
                .orElseThrow(() -> new IllegalStateException("Opening parenthesis for a conditional expression was expected."));

        ExpressionNode condition = this.parser.parseExpression();

        this.parser
                .nextIfType(Token.Type.RPARENTHESIS)
                .orElseThrow(() -> new IllegalStateException("Closing parenthesis for a conditional expression was expected."));

        BlockExpressionNode body = this.blockExpressionRule.nud();
        List<StatementNode> modifiedStatementList = new ArrayList<>();
        modifiedStatementList.add(
                this.parser.createNode((ignore) -> new IgnoredExpressionStatementNode(
                        position,
                        this.parser.createNode((ignore1, annotations1) -> new ConditionalExpressionNode(
                                position,
                                annotations1,
                                this.parser.createNode((ignore2) -> new UnaryExpressionNode(position, Operator.LOGIC_INVERT, condition)),
                                this.parser.createNode((ignore3, annotations3) -> new BlockExpressionNode(
                                        position,
                                        annotations3,
                                        new StatementNode[]{
                                                this.parser.createNode((ignore4, annotations4) -> new YieldStatementNode(
                                                        position,
                                                        annotations4,
                                                        body.label(),
                                                        this.parser.createNode((ignore5, annotations5) -> new VoidExpressionNode(position, annotations5))
                                                ))
                                        })
                                ), this.parser.createNode((ignore4, annotations4) -> new BlockExpressionNode(
                                        position,
                                        annotations4,
                                        new StatementNode[]{}
                                )))
                        ))
                )
        );
        modifiedStatementList.addAll(List.of(body.statements()));
        BlockExpressionNode modifiedBody = this.parser.createNode(ignore -> new BlockExpressionNode(body.position(), body.annotations(), modifiedStatementList.toArray(StatementNode[]::new)));
        modifiedBody.setLabel(body.label());

        return this.parser.createNode((ignore, annotations) -> new LoopExpressionNode(position, annotations, modifiedBody));
    }
}
