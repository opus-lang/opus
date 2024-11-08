package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.Visitor.Visitable(order = Integer.MAX_VALUE-20)
public non-sealed abstract class StatementNode extends Node implements Visitable{

    protected StatementNode(Position position, Annotation[] annotations) {
        super(position, annotations);
    }

    protected StatementNode(Position position) {
        super(position);
    }

}
