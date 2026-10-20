package minurl.domain.service;

import minurl.domain.entities.ShortUrl;
import minurl.domain.exceptions.UnsafeUrlException;
import minurl.domain.models.CreateShortUrl;
import minurl.domain.models.ShortUrlDTO;
import minurl.domain.repositories.ShortUrlRepository;
import minurl.domain.util.Base62;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;


@Service
public class ShortUrlService {

    private final ShortUrlRepository shortUrlRepository;
    private final EntityMapper entityMapper;
    private final GoogleSafeBrowsingService googleSafeBrowsingService;

    public ShortUrlService(ShortUrlRepository shortUrlRepository, EntityMapper entityMapper, GoogleSafeBrowsingService googleSafeBrowsingService){
        this.shortUrlRepository = shortUrlRepository;
        this.entityMapper = entityMapper;
        this.googleSafeBrowsingService = googleSafeBrowsingService;
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
        shortUrl.setClickCount(shortUrl.getClickCount()+1);
        shortUrlRepository.save(shortUrl);
        return shortUrlOptional.map(entityMapper::toShortUrlDto);
    }

    @Transactional
    public ShortUrlDTO createShortUrl(CreateShortUrl cmd) {
        var shortUrl = new ShortUrl();
        shortUrl.setOriginalUrl(cmd.originalUrl());
        shortUrl.setCreatedBy(null);
        shortUrl.setIsPrivate(false);
        shortUrl.setClickCount(0L);
        shortUrl.setExpiresAt(cmd.expiresAt() != null ? Instant.now().plus(ChronoUnit.DAYS.between(LocalDate.now(), cmd.expiresAt()), ChronoUnit.DAYS) : null);
        shortUrl.setCreatedAt(Instant.now());
        shortUrl.setShortKey("TEMP_");
        if (googleSafeBrowsingService.isMalicious(cmd.originalUrl())) {
            throw new UnsafeUrlException(
                    "Esta URL foi identificada como potencialmente perigosa e não pode ser encurtada."
            );
        }
        shortUrl = shortUrlRepository.saveAndFlush(shortUrl);

        String shortKey = Base62.encode(shortUrl.getId());

        shortUrl.setShortKey(shortKey);

        return entityMapper.toShortUrlDto(shortUrl);
    }



}
