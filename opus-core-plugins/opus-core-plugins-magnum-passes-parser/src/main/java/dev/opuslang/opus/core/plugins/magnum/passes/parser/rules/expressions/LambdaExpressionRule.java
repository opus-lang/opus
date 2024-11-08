package dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.expressions;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.BlockExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.ExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.LambdaExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.Node;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPNUDRule;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPParserComponent;

import java.util.ArrayList;
import java.util.List;

public class LambdaExpressionRule extends TDOPNUDRule<ExpressionNode> {

    private final BlockExpressionRule blockExpressionRule;

    public LambdaExpressionRule(TDOPParserComponent<ExpressionNode> parserComponent, int precedence, BlockExpressionRule blockExpressionRule) {
        super(parserComponent, precedence);
        this.blockExpressionRule = blockExpressionRule;
    }

    @Override
    public LambdaExpressionNode nud() {
        Node.Position position = new Node.Position(this.parser.currentPosition());
        this.parser.startAnnotationCapture();

        this.parser
                .nextIfType(Token.Type.KEYWORD_BACKSLASH)
                .orElseThrow(() -> new IllegalStateException("Lambda must start with a '\\'."));


        List<String> argumentNames = new ArrayList<>();

        this.parser
                .nextIfType(Token.Type.IDENTIFIER)
                .ifPresentOrElse(
                        token -> argumentNames.add(token.value()),
                        () -> {
                            this.parser
                                    .nextIfType(Token.Type.LPARENTHESIS)
                                    .orElseThrow(() -> new IllegalStateException("Argument list opening parenthesis expected."));

                            if(this.parser.peek().type() != Token.Type.RPARENTHESIS) {
                                do{
                                    argumentNames.add(this.parser
                                            .nextIfType(Token.Type.IDENTIFIER)
                                            .orElseThrow(() -> new IllegalStateException("Argument name identifier expected."))
                                            .value()
                                    );
                                }while(this.parser.nextIfType(Token.Type.COMMA).isPresent());
                                this.parser
                                        .nextIfType(Token.Type.RPARENTHESIS)
                                        .orElseThrow(() -> new IllegalStateException("Argument list closing parenthesis expected."));
                            }
                        }
                );

        this.parser
                .nextIfType(Token.Type.ARROW)
                .orElseThrow(() -> new IllegalStateException("'->' expected."));

        BlockExpressionNode body = this.blockExpressionRule.nud(); // Consider allowing block-less functions.

        return this.parser.createNode((ignore, annotations) -> new LambdaExpressionNode(position, annotations, argumentNames.toArray(String[]::new), body));
    }
}
