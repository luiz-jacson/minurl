package minurl.web.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record CreateShortUrlForm(
        @NotBlank(message = "É obrigatório preencher esse campo.")
        String originalUrl,
        Boolean generateQrCode,
        LocalDate expiresAt
) {}
