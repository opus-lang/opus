package dev.opuslang.opus.core.plugins.magnum.passes.lexer.utils;

public final class Characters{
    private Characters(){}

    public static boolean isNumber(char c, int radix){
        return (Character.digit(c, radix) != -1);
    }
    public static boolean isHexadecimal(char c){
        return Characters.isNumber(c, 16);
    }
    public static boolean isDecimal(char c){
        return Characters.isNumber(c, 10);
    }
    public static boolean isOctal(char c){
        return Characters.isNumber(c, 8);
    }
    public static boolean isBinary(char c){
        return Characters.isNumber(c, 2);
    }

    public static boolean isLineEnd(char c){
        return c == '\n' || c == '\0';
    }
    public static boolean isWhitespace(char c){
        return c == ' ' || c == '\t' || c == '\n';
    }

    public static boolean isIdentifierStart(char c){
        return c == '_' || Character.isAlphabetic(c);
    }

    public static boolean isIdentifierPart(char c){
        return Characters.isIdentifierStart(c) || Character.isDigit(c);
    }
}