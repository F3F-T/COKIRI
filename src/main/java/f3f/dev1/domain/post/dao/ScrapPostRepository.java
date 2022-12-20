package f3f.dev1.domain.post.dao;

import f3f.dev1.domain.post.model.ScrapPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScrapPostRepository extends JpaRepository<ScrapPost, Long> {
    Optional<ScrapPost> findById(Long aLong);

    List<ScrapPost> findByScrapId(Long scrapId);

    Optional<ScrapPost> findByScrapIdAndPostId(Long scrapId, Long postId);
}
