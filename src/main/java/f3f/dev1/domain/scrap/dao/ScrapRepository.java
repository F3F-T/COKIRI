package f3f.dev1.domain.scrap.dao;

import f3f.dev1.domain.scrap.model.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    Optional<Scrap> findScrapByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
