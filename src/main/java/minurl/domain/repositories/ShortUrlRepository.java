package minurl.domain.repositories;

import io.lettuce.core.dynamic.annotation.Param;
import minurl.domain.entities.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ShortUrlRepository  extends JpaRepository<ShortUrl, Long> {
    @Query("select su from ShortUrl su left join fetch su.createdBy where su.isPrivate = false order by su.createdAt desc")
    List<ShortUrl> findPublicShortUrls();

    @Query("select su from ShortUrl su where su.shortKey = :shortKey")
    Optional<ShortUrl> findOriginalUrl(@Param("shortKey") String shortKey);
}
