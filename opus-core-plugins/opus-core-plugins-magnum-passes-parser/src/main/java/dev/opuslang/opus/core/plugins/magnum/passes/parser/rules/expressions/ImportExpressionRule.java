package dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.expressions;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.ExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.ImportExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.Node;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPNUDRule;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPParserComponent;

public class ImportExpressionRule extends TDOPNUDRule<ExpressionNode> {

    public ImportExpressionRule(TDOPParserComponent<ExpressionNode> parserComponent, int precedence) {
        super(parserComponent, precedence);
    }

    @Override
    public ImportExpressionNode nud() {
        Node.Position position = new Node.Position(this.parser.currentPosition());
        this.parser
                .nextIfType(Token.Type.KEYWORD_IMPORT)
                .orElseThrow(() -> new IllegalStateException("Keyword 'import' was expected."));

        String path = this.parser
                .nextIfType(Token.Type.STRING)
                .map(Token::value)
                .orElseThrow(() -> new IllegalStateException("Import path was expected."));

        // TODO: allow various preprocessors (e.g., from C, Rust, etc.)

        return this.parser.createNode(ignore -> new ImportExpressionNode(position, path, ImportExpressionNode.OPUS_IMPORT_PROCESSOR));
    }
}
