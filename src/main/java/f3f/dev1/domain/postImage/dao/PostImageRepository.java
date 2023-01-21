package f3f.dev1.domain.postImage.dao;

import f3f.dev1.domain.postImage.model.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    List<String> findByPostId(Long postId);
}
