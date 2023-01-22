package f3f.dev1.domain.postImage.dao;

import java.util.List;

import static f3f.dev1.domain.postImage.dto.PostImageDTO.*;

public interface PostImageCustomRepository {
    List<postImageInfoDto> findByPostId(Long postId);
}
