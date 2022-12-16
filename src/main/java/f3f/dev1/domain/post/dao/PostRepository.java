package f3f.dev1.domain.post.dao;

import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.model.TradeStatus;
import f3f.dev1.domain.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findById(Long id);
    boolean existsById(Long id);
    boolean existsByAuthorId(Long authorId);
    List<Post> findByAuthorId(Long authorId);
    void deleteById(Long id);

}
