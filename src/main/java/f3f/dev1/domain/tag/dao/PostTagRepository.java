package f3f.dev1.domain.tag.dao;

import f3f.dev1.domain.tag.model.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostTagRepository extends JpaRepository<PostTag, Long> {

    Optional<PostTag> findById(Long id);
}
