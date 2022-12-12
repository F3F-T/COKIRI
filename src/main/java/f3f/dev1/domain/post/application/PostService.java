package f3f.dev1.domain.post.application;

import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.exception.NotFoundPostListByAuthor;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

import java.util.List;

import static f3f.dev1.domain.post.dto.PostDTO.*;

@Service
@Validated
@RequiredArgsConstructor
public class PostService {

    private PostRepository postRepository;

    @Transactional
    public Long savePost(@Valid PostSaveRequest postSaveRequest) {

        // TODO 검증로직

        Post post = postSaveRequest.toEntity();
        postRepository.save(post);
        return post.getId();
    }

    /* TODO
        R : 게시글은 조회가 좀 양이 많을 듯 하다
        productCategory, wishCategory, 작성자, 태그
        title 별로 조회도 필요할까?
     */

    // 먼저 내부적으로 사용될 id 조회
    // findByIdPostListDTO는 검색된 포스트 리스트를 가지고 있는 DTO이다.
    @Transactional(readOnly = true)
    public FindByAuthorPostListDTO findPostByAuthor(User author) {
        if(!postRepository.existsByAuthor(author)) {
            throw new NotFoundPostListByAuthor("해당 작성자의 게시글이 없습니다.");
        }
        List<Post> byAuthor = postRepository.findByAuthor(author);
        // DTO로 반환
        return new FindByAuthorPostListDTO(byAuthor);
    }
}
