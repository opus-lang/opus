package dev.opuslang.opus.core.plugins.magnum.passes.parser.components.rd;

import dev.opuslang.opus.core.plugins.magnum.passes.parser.Parser;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.ParserComponent;

public abstract class RDParserComponent<T> extends ParserComponent<T> {
    protected RDParserComponent(Parser parser) {
        super(parser);
    }
}
