package f3f.dev1.domain.member.model;

import f3f.dev1.global.common.constants.EmailConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

import static f3f.dev1.global.common.constants.EmailConstants.EMIAL_CERTIFICATION_TIME;

@Getter
@Builder
@AllArgsConstructor
@RedisHash(value = "emailCertification")
public class EmailCertification {
    @Id
    private String email;

    private String code;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long expired;
}
