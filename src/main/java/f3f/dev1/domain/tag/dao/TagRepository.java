package f3f.dev1.domain.tag.dao;

import f3f.dev1.domain.tag.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findById(Long id);
    List<Tag> findByNameIn(List<String> name);
    boolean existsByName(String name);
    boolean existsById(Long id);
}
