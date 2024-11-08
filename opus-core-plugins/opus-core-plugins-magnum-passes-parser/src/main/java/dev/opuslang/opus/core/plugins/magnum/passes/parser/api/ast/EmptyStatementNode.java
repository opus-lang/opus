package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.Visitor.Visitable
public final class EmptyStatementNode extends StatementNode implements Visitable{

    public EmptyStatementNode(Position position) {
        super(position);
    }

}
