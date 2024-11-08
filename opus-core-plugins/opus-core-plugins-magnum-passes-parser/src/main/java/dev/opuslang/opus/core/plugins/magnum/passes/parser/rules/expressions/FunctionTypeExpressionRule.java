package dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.expressions;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.ExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.FunctionTypeExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.Node;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPNUDRule;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPParserComponent;

import java.util.ArrayList;
import java.util.List;

public class FunctionTypeExpressionRule extends TDOPNUDRule<ExpressionNode> {

    public FunctionTypeExpressionRule(TDOPParserComponent<ExpressionNode> parserComponent, int precedence) {
        super(parserComponent, precedence);
    }

    @Override
    public FunctionTypeExpressionNode nud() {
        Node.Position position = new Node.Position(this.parser.currentPosition());
        this.parser.startAnnotationCapture();

        this.parser
                .nextIfType(Token.Type.KEYWORD_FN)
                .orElseThrow(() -> new IllegalStateException("Function type definition must always start with a 'fn'."));

        this.parser
                .nextIfType(Token.Type.LPARENTHESIS)
                .orElseThrow(() -> new IllegalStateException("Argument list opening parenthesis expected."));

        List<ExpressionNode> argumentTypes = new ArrayList<>();
        if(this.parser.peek().type() != Token.Type.RPARENTHESIS) {
            do{
                argumentTypes.add(this.parser.parseExpression());
            }while(this.parser.nextIfType(Token.Type.COMMA).isPresent());
            this.parser
                    .nextIfType(Token.Type.RPARENTHESIS)
                    .orElseThrow(() -> new IllegalStateException("Argument list closing parenthesis expected."));
        }

        this.parser
                .nextIfType(Token.Type.ARROW)
                .orElseThrow(() -> new IllegalStateException("'->' expected."));

        ExpressionNode returnType = this.parser.parseExpression();

        return this.parser.createNode((ignore, annotations) -> new FunctionTypeExpressionNode(position, annotations, argumentTypes.toArray(ExpressionNode[]::new), returnType));
    }
}
