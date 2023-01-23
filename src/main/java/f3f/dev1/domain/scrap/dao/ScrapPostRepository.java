package f3f.dev1.domain.scrap.dao;

import f3f.dev1.domain.post.model.ScrapPost;
import f3f.dev1.domain.scrap.dao.ScrapPostCustomRepository;
import f3f.dev1.domain.scrap.dto.ScrapPostDTO;
import f3f.dev1.domain.scrap.model.Scrap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static f3f.dev1.domain.scrap.dto.ScrapPostDTO.*;

@Repository
public interface ScrapPostRepository extends JpaRepository<ScrapPost, Long>, ScrapPostCustomRepository {
    Optional<ScrapPost> findById(Long aLong);

    List<ScrapPost> findByScrapId(Long scrapId);

    Page<ScrapPost> findByScrapId(Long scrapId, Pageable pageable);

    List<ScrapPost> findByPostId(Long postId);

    Optional<ScrapPost> findByScrapIdAndPostId(Long scrapId, Long postId);

    boolean existsByScrapIdAndPostId(Long scrapId, Long postId);

    boolean existsByScrapId(Long scrapId);

//    @Query(value = "SELECT p.post_id AS postId, p.title, p.thumbnail_img_path AS thumbnail, t.trade_status AS tradeStatus, c.name, COUNT(scrap_id) AS likes " +
//            "FROM scrap_post sp " +
//            "         JOIN post p on p.post_id = sp.post_id " +
//            "         JOIN trade t on p.post_id = t.post_id " +
//            "         JOIN category c on c.category_id = p.wish_category_id " +
//            "WHERE sp.post_id in (SELECT post_id FROM scrap_post WHERE scrap_id = :scrapId) " +
//            "GROUP BY sp.post_id;",countQuery = "SELECT count(*) FROM scrap_post sp WHERE sp.post_id IN (SELECT  post_id FROM scrap_post WHERE scrap_id = :scrapId)",nativeQuery = true)
//    Page<GetUserScrapPostInterface> findUserScrapPostByNativeQuery(@Param(value = "scrapId") Long scrapId, Pageable pageable);
//
//    interface GetUserScrapPostInterface{
//        Long getPostId();
//
//        String getTitle();
//
//        String getThumbnail();
//
//        String getTradeStatus();
//
//        String getName();
//
//        Long getLikes();
//    }
}
