package minurl.domain.exceptions;

public class UnsafeUrlException extends RuntimeException {
    public UnsafeUrlException(String message) {
        super(message);
    }
}
