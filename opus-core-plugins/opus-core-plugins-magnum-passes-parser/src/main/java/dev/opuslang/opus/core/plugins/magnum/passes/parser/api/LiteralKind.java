package dev.opuslang.opus.core.plugins.magnum.passes.parser.api;

public enum LiteralKind {

    STRING,
    CHARACTER,
    INTEGER,
    FLOATING,
    BOOLEAN,
    VOID,
    NEVER, // In Lexer, "KEYWORD_NEVER" represents a type. Here -> it is a value.

}
