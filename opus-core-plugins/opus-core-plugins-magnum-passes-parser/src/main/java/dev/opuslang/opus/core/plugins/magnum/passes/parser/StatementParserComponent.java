package dev.opuslang.opus.core.plugins.magnum.passes.parser;

import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.StatementNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.rd.RDParserComponent;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.statements.StatementsRuleset;

public class StatementParserComponent extends RDParserComponent<StatementNode> {

    private final StatementsRuleset statementsRuleset;

    public StatementParserComponent(Parser parser) {
        super(parser);
        this.statementsRuleset = new StatementsRuleset(parser);
    }

    @Override
    public StatementNode parse() {
        return this.statementsRuleset.parse();
    }

}
