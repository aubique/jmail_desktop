package dev.aubique.bquiz;

public class NotSelectedException extends Exception {

    public NotSelectedException(String message) {
        super("Choose an item" + message);
    }

    public NotSelectedException(String message, Exception exception) {
        super(message, exception);
    }
}