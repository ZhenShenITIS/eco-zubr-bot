package itis.ecozubrbot.exceptions;

public class RewardSoldOutException extends RuntimeException {
    public RewardSoldOutException(String message) {
        super(message);
    }
}
