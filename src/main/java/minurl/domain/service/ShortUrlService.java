package minurl.domain.service;

import minurl.ApplicationProperties;
import minurl.domain.entities.ShortUrl;
import minurl.domain.models.CreateShortUrlCmd;
import minurl.domain.models.ShortUrlDTO;
import minurl.domain.repositories.ShortUrlRepository;
import minurl.domain.util.Base62;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ShortUrlService {

    private final ShortUrlRepository shortUrlRepository;
    private final EntityMapper entityMapper;
    private final ApplicationProperties properties;

    public ShortUrlService(ShortUrlRepository shortUrlRepository, EntityMapper entityMapper, ApplicationProperties properties){
        this.shortUrlRepository = shortUrlRepository;
        this.entityMapper = entityMapper;
        this.properties = properties;
    }

    public List<ShortUrlDTO> findAllPublicShortUrls(){
        return shortUrlRepository.findPublicShortUrls().stream().map(entityMapper::toShortUrlDto).toList();
    }

    @Transactional
    public Optional<ShortUrlDTO> findByShortKey(String shortKey){
        Optional<ShortUrl> shortUrlOptional = shortUrlRepository.findOriginalUrl(shortKey);
        if(shortUrlOptional.isEmpty()) {
            return Optional.empty();
        }
        ShortUrl shortUrl = shortUrlOptional.get();
        if(shortUrl.getExpiresAt() != null && shortUrl.getExpiresAt().isBefore(Instant.now())) {
            return Optional.empty();
        }
        shortUrl.setClickCount(shortUrl.getClickCount()+1);
        shortUrlRepository.save(shortUrl);
        return shortUrlOptional.map(entityMapper::toShortUrlDto);
    }

    @Transactional
    public ShortUrlDTO createShortUrl(CreateShortUrlCmd cmd) {
        var shortUrl = new ShortUrl();
        shortUrl.setOriginalUrl(cmd.originalUrl());
        shortUrl.setCreatedBy(null);
        shortUrl.setIsPrivate(false);
        shortUrl.setClickCount(0L);
        shortUrl.setExpiresAt(Instant.now().plus(properties.defaultExpiryInDays(), ChronoUnit.DAYS));
        shortUrl.setCreatedAt(Instant.now());
        shortUrl.setShortKey("TEMP_");
        shortUrl = shortUrlRepository.saveAndFlush(shortUrl);

        String shortKey = Base62.encode(shortUrl.getId());

        shortUrl.setShortKey(shortKey);

        return entityMapper.toShortUrlDto(shortUrl);
    }



}
