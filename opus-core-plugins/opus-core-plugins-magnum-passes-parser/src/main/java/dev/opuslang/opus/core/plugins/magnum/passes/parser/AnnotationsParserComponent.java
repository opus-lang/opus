package dev.opuslang.opus.core.plugins.magnum.passes.parser;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.ExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.Node;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.ParserComponent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AnnotationsParserComponent extends ParserComponent<Node.Annotation> {

    public AnnotationsParserComponent(Parser parser) {
        super(parser);
    }

    @Override
    public Node.Annotation parse() {
        this.parser
                .nextIfType(Token.Type.AT)
                .orElseThrow(() -> new IllegalStateException("Annotations must start with '@'."));

        String annotationName = this.parser
                .nextIfType(Token.Type.IDENTIFIER)
                .orElseThrow(() -> new IllegalStateException("Annotation name expected."))
                .value();

        Map<String, ExpressionNode> annotationArguments = new HashMap<>();
        this.parser
                .nextIfType(Token.Type.LPARENTHESIS)
                .ifPresent(ignore -> {
                    if(this.parser.peek().type() != Token.Type.RPARENTHESIS) {
                        do{
                            String argumentName = this.parser
                                    .nextIfType(Token.Type.IDENTIFIER)
                                    .orElseThrow(() -> new IllegalStateException("Annotation argument name expected."))
                                    .value();

                            this.parser
                                    .nextIfType(Token.Type.OPERATOR_WALRUS)
                                    .orElseThrow(() -> new IllegalStateException("Annotation argument must have a value."));

                            annotationArguments.put(argumentName, this.parser.parseExpression());
                        }while(this.parser.nextIfType(Token.Type.COMMA).isPresent());
                        this.parser
                                .nextIfType(Token.Type.RPARENTHESIS)
                                .orElseThrow(() -> new IllegalStateException("Annotation argument list closing parenthesis expected."));
                    }
                });

        return new Node.Annotation(annotationName, Collections.unmodifiableMap(annotationArguments));
    }

}
