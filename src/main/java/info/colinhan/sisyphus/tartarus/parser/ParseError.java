package info.colinhan.sisyphus.tartarus.parser;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ParseError {
    private final int line;
    private final int column;
    private final int length;
    private final String message;
    @JsonIgnore
    private final Throwable cause;

    public ParseError(int line, int column, int length, String message, Throwable cause) {
        this.line = line;
        this.column = column;
        this.length = length;
        this.message = message;
        this.cause = cause;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public int getLength() {
        return length;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getCause() {
        return cause;
    }

    @Override
    public String toString() {
        return String.format("[%d,%d] %s", line, column, message);
    }
}
