package info.colinhan.sisyphus.tartarus.parser;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.ArrayList;
import java.util.List;

public class ErrorListener extends BaseErrorListener {
    private final List<String> errors = new ArrayList<>();
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        errors.add(String.format("[%d,%d] %s", line, charPositionInLine, msg));
        super.syntaxError(recognizer, offendingSymbol, line, charPositionInLine, msg, e);
    }

    public boolean hasError() {
        return !errors.isEmpty();
    }

    public List<String> getErrors() {
        return errors;
    }
}
