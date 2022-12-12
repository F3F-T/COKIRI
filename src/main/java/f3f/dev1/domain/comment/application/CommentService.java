package f3f.dev1.domain.comment.application;

import f3f.dev1.domain.comment.dao.CommentRepository;
import f3f.dev1.domain.comment.dto.CommentDTO;
import f3f.dev1.domain.comment.model.Comment;
import f3f.dev1.domain.post.dao.PostRepository;
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

import static f3f.dev1.domain.comment.dto.CommentDTO.*;

@Service
@Validated
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /*
        C : Create
        2가지 경우(Parent Comment와 Child Comment를 생성하는 경우)로 나누겠음
     */

    // 부모 댓글 생성 로직
    @Transactional
    public Long createParentComment(@Valid CreateParentCommentRequest parentCommentRequest) {
        User user = userRepository.findById(parentCommentRequest.getAuthor().getId()).orElseThrow(NotFoundByIdException::new);
        Post post = postRepository.findById(parentCommentRequest.getPost().getId()).orElseThrow(NotFoundByIdException::new);
        Comment comment = parentCommentRequest.ToParentEntity();
        Comment save = commentRepository.save(comment);
        user.getComments().add(save);
        return save.getId();
    }

    @Transactional
    public Long createChildComment(@Valid CreateChildCommentRequest childCommentRequest) {
        User user = userRepository.findById(childCommentRequest.getAuthor().getId()).orElseThrow(NotFoundByIdException::new);
        Post post = postRepository.findById(childCommentRequest.getPost().getId()).orElseThrow(NotFoundByIdException::new);
        Comment parent = commentRepository.findById(childCommentRequest.getParent().getId()).orElseThrow(NotFoundByIdException::new);
        Comment child = childCommentRequest.ToChildEntity();
        parent.getChilds().add(child);
        return child.getId();
    }

    /* TODO
        R : Read
        댓글 조회
     */

    // id로 조회
    public FindByIdCommentResponse findCommentById(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        return new FindByIdCommentResponse(comment);
    }

    // post로 조회
    // TODO Repository 명명규칙 이상 없나?
    public FindByPostIdCommentListResponse findCommentsByPostId(Long postId) {
        if(!commentRepository.existsByPostId(postId)) {
            throw new NotFoundByIdException();
        }
        List<Comment> byPostId = commentRepository.findByPostId(postId);
        return new FindByPostIdCommentListResponse(byPostId);
    }

    // 게시글 주인이 쓴 댓글 조회? (작성자 태그 등을 표시해주기 위해 필요할 수도 있다고 생각함)
    // 근데 백에서 할 일이 아닌 것 같기도 하다
}
