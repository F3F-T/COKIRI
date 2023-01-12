package f3f.dev1.domain.post.dao;

import f3f.dev1.domain.post.dto.PostDTO;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.tag.model.PostTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static f3f.dev1.domain.post.dto.PostDTO.*;

public interface PostCustomRepository {
    Page<Post> findPostsByCondition(SearchPostRequestExcludeTag request, Pageable pageable);
    Page<PostInfoDtoForGET> findPostsByTags(List<String> tagNames, Pageable pageable);
}
