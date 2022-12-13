package f3f.dev1.domain.tag.dao;

import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.tag.model.PostTag;
import f3f.dev1.domain.tag.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostTagRepository extends JpaRepository<PostTag, Long> {

    Optional<PostTag> findById(Long id);
    Optional<PostTag> findByPostAndTag(Post post, Tag tag);
    List<PostTag> findByTagId(Long tagId);
    boolean existsByPostAndTag(Post post, Tag tag);
}
