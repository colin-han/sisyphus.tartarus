package info.colinhan.sisyphus.tartarus.exceptions;

public class TartarusValidationException extends Exception {
    public TartarusValidationException() {
        super();
    }

    public TartarusValidationException(String message) {
        super(message);
    }

    public TartarusValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public static RuntimeException withWrapper(String message) {
        return new RuntimeException(new TartarusValidationException(message));
    }
    public static RuntimeException withWrapper(String message, Throwable cause) {
        return new RuntimeException(new TartarusValidationException(message, cause));
    }
}
