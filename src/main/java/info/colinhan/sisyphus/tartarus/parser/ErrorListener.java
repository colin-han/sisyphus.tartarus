package info.colinhan.sisyphus.tartarus.parser;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.List;

public class ErrorListener extends BaseErrorListener {
    private final List<ParseError> errors = new ArrayList<>();
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        Token token = (Token) offendingSymbol;

        int startCharIndex = token.getStartIndex();
        int stopCharIndex = token.getStopIndex();
        int length = stopCharIndex - startCharIndex + 1;

        errors.add(new ParseError(line, charPositionInLine, length, msg, e));
        super.syntaxError(recognizer, offendingSymbol, line, charPositionInLine, msg, e);
    }

    public boolean hasError() {
        return !errors.isEmpty();
    }

    public List<ParseError> getErrors() {
        return errors;
    }
}
