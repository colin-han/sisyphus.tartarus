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
}
