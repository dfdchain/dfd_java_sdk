package dfd.errors;

public class SignTransactionException extends Exception {
    public SignTransactionException() {
    }

    public SignTransactionException(String message) {
        super(message);
    }

    public SignTransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SignTransactionException(Throwable cause) {
        super(cause);
    }

    public SignTransactionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
