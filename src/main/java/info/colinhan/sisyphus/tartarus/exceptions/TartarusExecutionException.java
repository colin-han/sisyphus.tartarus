package info.colinhan.sisyphus.tartarus.exceptions;

public class TartarusExecutionException extends RuntimeException {
    public TartarusExecutionException() {
        super();
    }

    public TartarusExecutionException(String message) {
        super(message);
    }

    public TartarusExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
