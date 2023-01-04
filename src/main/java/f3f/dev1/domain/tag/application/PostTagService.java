package f3f.dev1.domain.tag.application;

import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.tag.dao.PostTagRepository;
import f3f.dev1.domain.tag.dao.TagRepository;
import f3f.dev1.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
public class PostTagService {

    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;
    private final PostRepository postRepository;

    // 포스트 업데이트 할때 태그를 다시 설정해주기 위해 postTag를 날리는 로직
    @Transactional
    public String deletePostTagFromPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(NotFoundByIdException::new);
        postTagRepository.deletePostTagByPost(post);
//        postTagRepository.deleteByPost(post);
        return "DELETE";
    }

}
