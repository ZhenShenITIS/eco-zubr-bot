package itis.ecozubrbot.exceptions;

public class NotEnoughPointsException extends RuntimeException {
    public NotEnoughPointsException(String message) {
        super(message);
    }
}
