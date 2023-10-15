package info.colinhan.sisyphus.tartarus.exceptions;

import info.colinhan.sisyphus.tartarus.parser.ParseError;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TartarusParserException extends Exception {
    private final List<ParseError> errors;

    public TartarusParserException() {
        super();
        errors = Collections.emptyList();
    }

    public TartarusParserException(String message) {
        super(message);
        errors = Collections.singletonList(new ParseError(0, 0, 0, message, null));
    }

    public TartarusParserException(String message, Throwable cause) {
        super(message, cause);
        errors = Collections.singletonList(new ParseError(0, 0, 0, message, cause));
    }

    public TartarusParserException(List<ParseError> errors) {
        super(errors.stream().map(ParseError::toString).collect(Collectors.joining("\n")));
        this.errors = errors;
    }

    public List<ParseError> getErrors() {
        return errors;
    }

    public static RuntimeException withWrapper(String message) {
        return new RuntimeException(new TartarusParserException(message));
    }
    public static RuntimeException withWrapper(String message, Throwable cause) {
        return new RuntimeException(new TartarusParserException(message, cause));
    }

    public static TartarusParserException unwrap(RuntimeException e) {
        if (e.getCause() instanceof TartarusParserException) {
            return (TartarusParserException) e.getCause();
        } else {
            throw e;
        }
    }
}
