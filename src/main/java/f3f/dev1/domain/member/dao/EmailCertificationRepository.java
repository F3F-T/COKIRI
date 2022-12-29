package f3f.dev1.domain.member.dao;

import f3f.dev1.domain.member.model.EmailCertification;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EmailCertificationRepository extends CrudRepository<EmailCertification, String> {

    Optional<EmailCertification> findByEmail(String email);
    boolean existsByEmail(String email);

    boolean deleteByEmail(String email);
}
