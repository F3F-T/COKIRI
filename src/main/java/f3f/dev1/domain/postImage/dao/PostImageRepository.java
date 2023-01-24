package f3f.dev1.domain.postImage.dao;

import f3f.dev1.domain.postImage.model.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long>, PostImageCustomRepository {
    List<PostImage> findByPostId(Long postId);
}
