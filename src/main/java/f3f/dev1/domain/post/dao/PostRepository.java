package f3f.dev1.domain.post.dao;

import f3f.dev1.domain.category.model.Category;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.model.TradeStatus;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.tag.model.PostTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {

    List<Post> findAll();
    boolean existsById(Long id);
    Optional<Post> findById(Long id);
    List<Post> findByPrice(Long price);
    boolean existsByAuthorId(Long authorId);
    List<Post> findByAuthorId(Long authorId);
    List<Post> findByPriceLessThanEqual(Long maxPrice);
    List<Post> findByPostTagsIn(List<PostTag> postTags);
    List<Post> findByPriceGreaterThanEqual(Long minPrice);
    List<Post> findByWishCategoryName(String wishCategoryName);
    List<Post> findByPriceBetween(Long minPrice, Long maxPrice);
    List<Post> findByProductCategoryName(String productCategoryName);
    List<Post> findByWishCategoryNameAndPostTagsIn(String wishCategoryName, List<PostTag> postTags);
    List<Post> findByProductCategoryNameAndPostTagsIn(String productCategoryName, List<PostTag> postTags);
    List<Post> findByProductCategoryNameAndWishCategoryName(String productCategoryName, String wishCategoryName);
    List<Post> findByProductCategoryNameAndWishCategoryNameAndPostTagsIn(String productCategoryName, String wishCategoryName, List<PostTag> postTags);
    void deleteById(Long id);

    Page<Post> findAll(Pageable pageable);
    Page<Post> findByPostTagsIn(List<PostTag> postTags, Pageable pageable);

    @Query(nativeQuery = true,value = "select p.post_id , p.title , t.name\n" +
            "from post p \n" +
            "join post_tag pt on pt.post_id = p.post_id\n" +
            "join tag t on t.tag_id = pt.tag_id  \n" +
            "where t.name in (${tagNameList}) and count(p.post_id) = 3\n" +
            "group by p.post_id , t.name\n")
    List<Post> findByPostInTagName(String tagNameList);

}
