package minurl.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record CreateShortUrlForm(
        @NotBlank(message = "É obrigatório preencher esse campo.")
        @Pattern(regexp = "^https?://.+", message = "A URL deve começar com http:// ou https://")
        String originalUrl,
        Boolean generateQrCode,
        LocalDate expiresAt
) {}
