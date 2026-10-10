package minurl;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app")
@Validated
public record ApplicationProperties(
        @NotBlank
        String baseUrl,

        @Min(1)
        @Max(365)
        int defaultExpiryInDays,

        boolean validateOriginalUrl
) {
}