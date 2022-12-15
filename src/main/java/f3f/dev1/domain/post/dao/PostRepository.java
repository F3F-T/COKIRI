package f3f.dev1.domain.post.dao;

import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findById(Long id);
    boolean existsById(Long id);
    boolean existsByAuthor(User author);
    List<Post> findByAuthor(User author);
    void deleteById(Long id);

}
