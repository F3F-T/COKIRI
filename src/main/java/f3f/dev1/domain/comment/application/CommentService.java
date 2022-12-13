package f3f.dev1.domain.comment.application;

import f3f.dev1.domain.comment.dao.CommentRepository;
import f3f.dev1.domain.comment.dto.CommentDTO;
import f3f.dev1.domain.comment.model.Comment;
import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.exception.NotMatchingAuthorException;
import f3f.dev1.domain.post.exception.NotMatchingCommentException;
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
import static f3f.dev1.global.common.constants.ResponseConstants.*;

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

    /*
        R : Read
        댓글 조회
     */

    // id로 조회
    @Transactional(readOnly = false)
    public FindByIdCommentResponse findCommentById(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        @Valid FindByIdCommentResponse response = new FindByIdCommentResponse(comment);
        return response;
    }

    // post로 조회
    // TODO Repository 명명규칙 이상 없나?
    @Transactional(readOnly = true)
    public FindByPostIdCommentListResponse findCommentsByPostId(Long postId) {
        if(!commentRepository.existsByPostId(postId)) {
            throw new NotFoundByIdException();
        }
        List<Comment> byPostId = commentRepository.findByPostId(postId);
        @Valid FindByPostIdCommentListResponse response = new FindByPostIdCommentListResponse(byPostId);
        return response;
    }

    // 게시글 주인이 쓴 댓글 조회? (작성자 태그 등을 표시해주기 위해 필요할 수도 있다고 생각함)
    // 저자로 댓글 조회?
    // 근데 백에서 할 일이 아닌 것 같기도 하다
    // TODO 피드백 받기

    /*
        U: Update
        댓글 수정
     */

    @Transactional
    public String updateComment(@Valid UpdateCommentRequest updateCommentRequest) {
        Post post = postRepository.findById(updateCommentRequest.getId()).orElseThrow(NotFoundByIdException::new);
        Comment comment = commentRepository.findById(updateCommentRequest.getId()).orElseThrow(NotFoundByIdException::new);
        Comment commentInPost = commentRepository.findByPostIdAndId(post.getId(), comment.getId()).orElseThrow(NotFoundByIdException::new);
        if(!commentInPost.getId().equals(comment.getId())) {
            throw new NotMatchingCommentException("요청한 게시글에 수정하려는 댓글이 없습니다.");
        }
        commentInPost.updateContent(updateCommentRequest.getContent());
        return UPDATE;
    }

    @Transactional
    public String deleteComment(@Valid DeleteCommentRequest deleteCommentRequest) {
        Post post = postRepository.findById(deleteCommentRequest.getId()).orElseThrow(NotFoundByIdException::new);
        User user = userRepository.findById(deleteCommentRequest.getAuthor().getId()).orElseThrow(NotFoundByIdException::new);
        Comment comment = commentRepository.findById(deleteCommentRequest.getId()).orElseThrow(NotFoundByIdException::new);
        Comment commentInPost = commentRepository.findByPostIdAndId(post.getId(), comment.getId()).orElseThrow(NotFoundByIdException::new);
        if(commentInPost.getId() != comment.getId()) {
            throw new NotMatchingCommentException("요청한 게시글에 수정하려는 댓글이 없습니다.");
        }
        Comment authorComment = commentRepository.findByAuthorIdAndId(user.getId(), commentInPost.getId()).orElseThrow(NotFoundByIdException::new);
        if(authorComment.getId() != commentInPost.getId()) {
           throw new NotMatchingAuthorException("댓글 작성자가 아닙니다.");
        }
        commentRepository.delete(authorComment);
        return DELETE;
    }
}
