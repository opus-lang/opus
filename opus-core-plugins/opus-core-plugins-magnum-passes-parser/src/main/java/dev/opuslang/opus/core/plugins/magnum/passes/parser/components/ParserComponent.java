package dev.opuslang.opus.core.plugins.magnum.passes.parser.components;

import dev.opuslang.opus.core.plugins.magnum.passes.parser.Parser;

public abstract class ParserComponent<T> {

    protected final Parser parser;

    protected ParserComponent(Parser parser) {
        this.parser = parser;
    }

    public final Parser parser(){
        return this.parser;
    }

    public abstract T parse();

}
