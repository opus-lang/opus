package dev.opuslang.opus.core.plugins.magnum.passes.lexer;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Cursor;

public class SourceScanner {

    // TODO: Migrate to lazy reading
    private final String source;
    private final Cursor cursor;

    public SourceScanner(String source, Cursor cursor) {
        this.source = source;
        this.cursor = cursor;
    }

    public char next() {
        char result = this.peek();
        if(result != '\0') this.cursor.advanceOffset();
        if(result == '\n') this.cursor.advanceLine();
        return result;
    }

    public char next(int offset){
        for(int i = 0; i < offset; i++){
            this.next();
        }
        return this.next();
    }

    public boolean nextIfMatch(char c){
        if(this.peek() == c){
            this.next();
            return true;
        }
        return false;
    }

    public char peek(){
        return this.peek(0);
    }

    public char peek(int offset){
        if(this.cursor.offset()+offset >= this.source.length()){
            return '\0';
        }
        return this.source.charAt(this.cursor.offset()+offset);
    }

    public String source() {
        return this.source;
    }
    public Cursor cursor(){
        return this.cursor;
    }

}
