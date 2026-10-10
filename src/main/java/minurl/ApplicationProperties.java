package minurl;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app")
@Validated
public record ApplicationProperties(
        @NotBlank
        @DefaultValue("https://special-parakeet-7699g47jv493ppjw-8080.app.github.dev")
        String baseUrl,
        @DefaultValue("30")
        @Min(1)
        @Max(365)
        int defaultExpiryInDays,
        @DefaultValue("true")
        boolean validateOriginalUrl
) {
}