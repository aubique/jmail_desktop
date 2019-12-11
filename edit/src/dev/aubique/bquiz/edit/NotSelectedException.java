package dev.aubique.bquiz.edit;

public class NotSelectedException extends Exception {

    public NotSelectedException(Exception exception) {
        super("Item is not selected", exception);
    }

    public NotSelectedException(String message, Exception exception) {
        super(message, exception);
    }
}
