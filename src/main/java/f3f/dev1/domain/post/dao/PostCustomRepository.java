package f3f.dev1.domain.post.dao;

import f3f.dev1.domain.model.TradeStatus;
import f3f.dev1.domain.post.dto.PostDTO;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.tag.model.PostTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static f3f.dev1.domain.post.dto.PostDTO.*;

public interface PostCustomRepository {
    Page<Post> findPostDTOByConditions(SearchPostRequestExcludeTag requestExcludeTag, Pageable pageable);
    Page<Post> findPostsByTags(List<String> tagNames, TradeStatus tradeStatus, Pageable pageable);
    Page<Post> findPostsWithTradeStatus(TradeStatus tradeStatus, Pageable pageable);
//    Page<PostSearchResponseDto> findPostDTOByConditionsWIthQ(SearchPostRequestExcludeTag requestExcludeTag, Long currentMemberId, Pageable pageable);
//    Page<PostSearchResponseDto> findPostsByTags(List<String> tagNames, Long currentMemberId, Pageable pageable);
}
