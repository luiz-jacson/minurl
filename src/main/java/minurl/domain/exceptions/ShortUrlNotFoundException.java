package minurl.domain.exceptions;

public class ShortUrlNotFoundException extends RuntimeException {
    public ShortUrlNotFoundException(String message) {
        super(message);
    }
}
