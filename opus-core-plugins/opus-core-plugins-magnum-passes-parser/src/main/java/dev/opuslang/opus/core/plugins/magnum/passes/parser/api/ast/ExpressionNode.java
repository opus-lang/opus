package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.Visitor.Visitable(order = Integer.MAX_VALUE-20)
public non-sealed abstract class ExpressionNode extends Node implements Visitable {

    public interface Type{}

}
