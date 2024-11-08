package dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.statements;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.Parser;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.DefinitionStatementNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.ExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.InferTypeExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.Node;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.utils.Reference;
import dev.opuslang.opus.symphonia.annotation.Symphonia;

import java.util.*;

@Symphonia.DI.Component(name = "definition")
public class DefinitionStatementRule {

    @Symphonia.DI.Inject
    Parser parser;

    @Symphonia.DI.Inject
    EndStatementRule end;

    public DefinitionStatementNode parse(){
        Node.Position position = this.parser.currentPosition();
        this.parser.startAnnotationCapture();

        this.parser
                .nextIfType(Token.Type.KEYWORD_DEF)
                .orElseThrow(() -> new IllegalStateException("Keyword 'def' expected."));

        EnumSet<DefinitionStatementNode.Modifier> modifiers = EnumSet.noneOf(DefinitionStatementNode.Modifier.class);
        this.parser
                .nextIfType(Token.Type.KEYWORD_CONST)
                .ifPresent((ignore) -> modifiers.add(DefinitionStatementNode.Modifier.CONST));
        this.parser
                .nextIfType(Token.Type.KEYWORD_MUT)
                .ifPresent((ignore) -> modifiers.add(DefinitionStatementNode.Modifier.MUTABLE));

        String name = this.parser
                .nextIfType(Token.Type.IDENTIFIER)
                .orElseThrow(
                        () -> new IllegalStateException("Variable identifier expected.")
                ).value();

        ExpressionNode type = this.parser
                .nextIfType(Token.Type.COLON)
                .map(ignore -> this.parser.parseExpression())
                .orElse(this.parser.createNode(InferTypeExpressionNode::new));

        Optional<ExpressionNode> assignedValue = this.parser
                .nextIfType(Token.Type.OPERATOR_WALRUS)
                .map(ignore -> this.parser.parseExpression());
        this.end.expect();

        return this.parser.createNode((ignore, annotations) -> new DefinitionStatementNode(position, annotations, modifiers, name, type, assignedValue));
    }

}
