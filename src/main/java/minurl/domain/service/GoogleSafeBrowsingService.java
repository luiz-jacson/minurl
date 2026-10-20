package minurl.domain.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import minurl.ApplicationProperties;
import org.springframework.beans.factory.annotation.Value;
import minurl.domain.exceptions.SafeBrowsingUnavailableException;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;

@Service
public class GoogleSafeBrowsingService {

    private final RestClient restClient;
    private final String apiKey;

    public GoogleSafeBrowsingService(ApplicationProperties properties) {

        JdkClientHttpRequestFactory requestFactory =
                new JdkClientHttpRequestFactory(
                        HttpClient.newBuilder()
                                .connectTimeout(Duration.ofSeconds(3))
                                .build()
                );

        requestFactory.setReadTimeout(Duration.ofSeconds(5));

        this.restClient = RestClient.builder()
                .baseUrl("https://safebrowsing.googleapis.com")
                .requestFactory(requestFactory)
                .build();

        this.apiKey = properties.safeBrowsingApiKey();
    }

    public boolean isMalicious(String url) {

        try {
            SafeBrowsingRequest request = new SafeBrowsingRequest(
                    new ClientInfo("minurl", "1.0.0"),
                    new ThreatInfo(
                            List.of("MALWARE", "SOCIAL_ENGINEERING"),
                            List.of("ANY_PLATFORM"),
                            List.of("URL"),
                            List.of(new ThreatEntry(url))
                    )
            );

            SafeBrowsingResponse response = restClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v4/threatMatches:find")
                            .queryParam("key", apiKey)
                            .build())
                    .contentType(
                            org.springframework.http.MediaType.APPLICATION_JSON
                    )
                    .body(request)
                    .retrieve()
                    .body(SafeBrowsingResponse.class);

            return response != null
                    && response.matches() != null
                    && !response.matches().isEmpty();


        } catch (RestClientException e) {
            throw new SafeBrowsingUnavailableException(
                    "Não foi possível verificar a segurança da URL. Tente novamente.",
                    e
            );
        }
    }

    public record ClientInfo(
            String clientId,
            String clientVersion
    ) {}

    public record ThreatInfo(
            List<String> threatTypes,
            List<String> platformTypes,
            List<String> threatEntryTypes,
            List<ThreatEntry> threatEntries
    ) {}

    public record ThreatEntry(String url) {}

    public record SafeBrowsingRequest(
            ClientInfo client,
            ThreatInfo threatInfo
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SafeBrowsingResponse(
            List<ThreatMatch> matches
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ThreatMatch(
            String threatType,
            String platformType,
            String threatEntryType,
            ThreatEntry threat
    ) {}
}
