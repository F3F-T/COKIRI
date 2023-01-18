package f3f.dev1.domain.scrap.dao;

import f3f.dev1.domain.post.model.ScrapPost;
import f3f.dev1.domain.scrap.dao.ScrapPostCustomRepository;
import f3f.dev1.domain.scrap.model.Scrap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScrapPostRepository extends JpaRepository<ScrapPost, Long>, ScrapPostCustomRepository {
    Optional<ScrapPost> findById(Long aLong);

    List<ScrapPost> findByScrapId(Long scrapId);

    Page<ScrapPost> findByScrapId(Long scrapId, Pageable pageable);

    List<ScrapPost> findByPostId(Long postId);

    Optional<ScrapPost> findByScrapIdAndPostId(Long scrapId, Long postId);

    boolean existsByScrapIdAndPostId(Long scrapId, Long postId);

    boolean existsByScrapId(Long scrapId);
}
