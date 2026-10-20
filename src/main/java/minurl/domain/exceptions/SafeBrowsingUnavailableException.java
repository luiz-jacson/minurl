package minurl.domain.exceptions;

public class SafeBrowsingUnavailableException extends RuntimeException {
  public SafeBrowsingUnavailableException(String message) {
    super(message);
  }

  public SafeBrowsingUnavailableException(
          String message,
          Throwable cause) {
    super(message, cause);
  }
}
