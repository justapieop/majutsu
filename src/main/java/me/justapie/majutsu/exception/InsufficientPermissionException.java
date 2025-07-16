package me.justapie.majutsu.exception;

public class InsufficientPermissionException extends Exception {
    public InsufficientPermissionException() {
        super();
    }

    public InsufficientPermissionException(String message) {
        super(message);
    }
}
