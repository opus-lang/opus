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
        ImportExpressionNode.Builder nodeBuilder = new ImportExpressionNode.Builder(this.parser.copyCurrentPosition());

        this.parser
                .nextIfType(Token.Type.KEYWORD_IMPORT)
                .orElseThrow(() -> new IllegalStateException("Keyword 'import' was expected."));

        nodeBuilder.path(
                this.parser
                    .nextIfType(Token.Type.STRING)
                    .map(Token::value)
                    .orElseThrow(() -> new IllegalStateException("Import path was expected."))
        );

        nodeBuilder.processor(ImportExpressionNode.OPUS_IMPORT_PROCESSOR);

//        // TODO: allow various preprocessors (e.g., from C, Rust, etc.)

        return nodeBuilder.build();
    }
}
