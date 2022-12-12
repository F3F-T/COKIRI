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
        productCategory, wishCategory, 작성자, 태그
        title 별로 조회도 필요할까?
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

}
