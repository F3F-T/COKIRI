package f3f.dev1.domain.post.dao;

import f3f.dev1.domain.post.dto.PostDTO;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.tag.model.PostTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static f3f.dev1.domain.post.dto.PostDTO.*;

public interface PostCustomRepository {
    Page<Post> findPostDTOByConditions(SearchPostRequestExcludeTag requestExcludeTag, Pageable pageable);
//    Page<PostInfoDtoForGET_PreProcessor> findPostDTOByConditions(SearchPostRequestExcludeTag requestExcludeTag, Pageable pageable);
    Page<Post> findPostsByTags(List<String> tagNames, Pageable pageable);

    Page<PostSearchResponseDto> findPostDTOByConditions(SearchPostRequestExcludeTag requestExcludeTag, Long currentMemberId, Pageable pageable);
    Page<PostSearchResponseDto> findPostsByTags(List<String> tagNames, Long currentMemberId, Pageable pageable);
}
