package f3f.dev1.domain.post.dao;

import f3f.dev1.domain.post.dto.PostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static f3f.dev1.domain.post.dto.PostDTO.*;

public interface PostCustomRepository {
    Page<PostInfoDtoWithTag> findByProductCategory(SearchPostRequest request, Pageable pageable);
}
