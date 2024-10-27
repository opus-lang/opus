package dev.opuslang.opus.core.plugins.magnum.passes.lexer.api;

public class Cursor {
    private static final int DEFAULT_OFFSET = 0;
    private static final int DEFAULT_LINE = 1;
    private static final int DEFAULT_COLUMN = 1;

    private int offset;
    private int line;
    private int column;

    public Cursor(){
        this(Cursor.DEFAULT_OFFSET, Cursor.DEFAULT_LINE, Cursor.DEFAULT_COLUMN);
    }

    public Cursor(Cursor other){
        this.restore(other);
    }

    public Cursor(int offset, int line, int column) {
        this.offset = offset;
        this.line = line;
        this.column = column;
    }

    public int offset() {
        return this.offset;
    }

    public int line() {
        return this.line;
    }

    public int column() {
        return this.column;
    }

    public void restore(Cursor checkpoint){
        this.offset = checkpoint.offset;
        this.line = checkpoint.line;
        this.column = checkpoint.column;
    }

    public Cursor advanceOffset(){
        this.offset++;
        this.column++;
        return this;
    }

    public Cursor advanceLine(){
        this.line++;
        this.column = Cursor.DEFAULT_COLUMN;
        return this;
    }

    @Override
    public String toString() {
        return "Cursor{" +
                "offset=" + offset +
                ", line=" + line +
                ", column=" + column +
                '}';
    }
}