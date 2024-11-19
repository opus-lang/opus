package dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.statements;

import dev.opuslang.opus.core.plugins.magnum.passes.parser.Parser;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.EmptyStatementNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.Node;
import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.DI.Component(name = "empty")
public class EmptyStatementRule {

    @Symphonia.DI.Inject
    Parser parser;

    @Symphonia.DI.Inject
    EndStatementRule end;

    public EmptyStatementNode parse(){
        EmptyStatementNode.Builder nodeBuilder = new EmptyStatementNode.Builder(this.parser.copyCurrentPosition());

        this.end.expect();
        return nodeBuilder.build();
    }

}
