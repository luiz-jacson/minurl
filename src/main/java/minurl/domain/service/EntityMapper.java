package minurl.domain.service;

import minurl.domain.entities.ShortUrl;
import minurl.domain.entities.User;
import minurl.domain.models.ShortUrlDTO;
import minurl.domain.models.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class EntityMapper {

    public ShortUrlDTO toShortUrlDto(ShortUrl shortUrl) {
        UserDTO userDto = null;
        if(shortUrl.getCreatedBy() != null) {
            userDto = toUserDTO(shortUrl.getCreatedBy());
        }

        return new ShortUrlDTO(
                shortUrl.getId(),
                shortUrl.getShortKey(),
                shortUrl.getOriginalUrl(),
                shortUrl.getIsPrivate(),
                shortUrl.getExpiresAt(),
                userDto,
                shortUrl.getClickCount(),
                shortUrl.getCreatedAt()
        );
    }

    public UserDTO toUserDTO(User user) {
        return new UserDTO(user.getId(), user.getName());
    }
}
