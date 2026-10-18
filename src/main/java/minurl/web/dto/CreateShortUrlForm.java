package minurl.web.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record CreateShortUrlForm(
        @NotBlank(message = "Original URL is required")
        String originalUrl,
        Boolean generateQrCode,
        LocalDate expiresAt
) {}
