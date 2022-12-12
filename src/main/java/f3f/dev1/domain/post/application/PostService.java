package f3f.dev1.domain.post.application;

import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.exception.NotFoundPostListByAuthor;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.user.dao.UserRepository;
import f3f.dev1.domain.user.model.User;
import f3f.dev1.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

import java.util.List;
import java.util.Optional;

import static f3f.dev1.domain.post.dto.PostDTO.*;

@Service
@Validated
@RequiredArgsConstructor
public class PostService {

    private PostRepository postRepository;
    private UserRepository userRepository;

    @Transactional
    public Long savePost(@Valid PostSaveRequest postSaveRequest) {

        // 유저 객체 받아와서 포스트 리스트에 추가해줘야 함
        // TODO Trade 객체를 어떻게 처리할지 아직 명확하지 않음
        User user = userRepository.findById(postSaveRequest.getAuthor().getId()).orElseThrow(NotFoundByIdException::new);

        /* TODO 카테고리 객체 받아와서 카테고리 리스트에 추가해줘야 함
            categoryRepository.findById(productCategory.getId()) ~
            해당 부분이 구현되면 추가하겠음
         */

        Post post = postSaveRequest.toEntity();
        user.getPosts().add(post);
        postRepository.save(post);
        return post.getId();
    }

    /* TODO
        R : 게시글은 조회가 좀 양이 많을 듯 하다
        카테고리, 태그 필터링은 Post에서 할 수 없다. 각각의 서비스에서 구현하겠다.
        title 별로 조회도 필요할까? 검색엔진이 필요한가? - 피드백 결과 일단은 제외하는 걸로.
     */

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

    @Transactional(readOnly = true)
    public FindByIdPostDTO findPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        return new FindByIdPostDTO(post);
    }

    /* TODO
        U : 게시글 업데이트
     */

    // TODO 피드백 받기 : 반환 타입을 Long (id)으로 하는 거랑 Post 객체 하나만 가지고 있는 DTO로 하는 것 중 뭐가 더 나은가
    @Transactional
    public Long updatePost(@Valid UpdatePostRequest updatePostRequest) {
        Post post = postRepository.findById(updatePostRequest.getId()).orElseThrow(NotFoundByIdException::new);
        /* TODO
            업데이트된 카테고리들이 유효한지 각각 Id로 확인한다.
            관련 기능이 구현되면 추가할 것이고, 지금은 유효하다고 가정하고 로직을 작성하겠음
         */
        post.updateTitle(updatePostRequest.getTitle());
        post.updateContent(updatePostRequest.getContent());
        post.updatePostTags(updatePostRequest.getPostTags());
        post.updateProductCategory(updatePostRequest.getProductCategory());
        post.updateWishCategory(updatePostRequest.getWishCategory());

        return post.getId();
    }

//    public

}
