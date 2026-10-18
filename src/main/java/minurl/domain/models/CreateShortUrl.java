package minurl.domain.models;

import java.time.LocalDate;

public record CreateShortUrl(String originalUrl, LocalDate expiresAt) {
}
