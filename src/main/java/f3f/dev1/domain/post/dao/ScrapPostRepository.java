package f3f.dev1.domain.post.dao;

import f3f.dev1.domain.post.model.ScrapPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ScrapPostRepository extends JpaRepository<ScrapPost, Long> {
    Optional<ScrapPost> findById(Long aLong);

    List<ScrapPost> findByScrapId(Long scrapId);

    List<ScrapPost> findByPostId(Long postId);

    Optional<ScrapPost> findByScrapIdAndPostId(Long scrapId, Long postId);

    boolean existsByScrapIdAndPostId(Long scrapId, Long postId);

    boolean existsByScrapId(Long scrapId);
}
