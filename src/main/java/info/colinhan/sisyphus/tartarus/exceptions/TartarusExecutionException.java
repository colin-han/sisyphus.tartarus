package info.colinhan.sisyphus.tartarus.exceptions;

public class TartarusExecutionException extends Exception {
    public TartarusExecutionException() {
        super();
    }

    public TartarusExecutionException(String message) {
        super(message);
    }

    public TartarusExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public static RuntimeException withWrapper(String message) {
        return new RuntimeException(new TartarusExecutionException(message));
    }
    public static RuntimeException withWrapper(String message, Throwable cause) {
        return new RuntimeException(new TartarusExecutionException(message, cause));
    }
}
