package f3f.dev1.domain.comment.application;

import f3f.dev1.domain.comment.dao.CommentRepository;
import f3f.dev1.domain.comment.dto.CommentDTO;
import f3f.dev1.domain.comment.exception.DuplicateCommentException;
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
        -> 취소, 하나로 통일하고 builder 활용
     */


    // 부모 자식 대통합
    @Transactional
    public Long createComment(CreateCommentRequest createCommentRequest) {
        User user = userRepository.findById(createCommentRequest.getAuthor().getId()).orElseThrow(NotFoundByIdException::new);
        Post post = postRepository.findById(createCommentRequest.getPost().getId()).orElseThrow(NotFoundByIdException::new);
        // 유저, 포스트 존재 확인
        if(commentRepository.existsById(createCommentRequest.getId())) {
            throw new DuplicateCommentException("이미 존재하는 댓글입니다.");
        }
        if(createCommentRequest.getParentComment() == null) {
            // 부모 댓글이 null이라면 부모 댓글로 처리
            Comment parentComment = createCommentRequest.toEntity();
            commentRepository.save(parentComment);
            return parentComment.getId();
        } else {
            // 부모 댓글이 존재한다면 자식 댓글로 처리
            Comment parentComment = createCommentRequest.getParentComment();
            Comment comment = commentRepository.findById(createCommentRequest.getId()).orElseThrow(NotFoundByIdException::new);
            // TODO 피드백 부분, 일단은 아래와 같이 작성해두고 나중에 다시 생각해보기로 하자
            parentComment.getChilds().add(comment);
            return comment.getId();
        }
    }

    /*
        R : Read
        댓글 조회
     */

    // id로 조회
    @Transactional(readOnly = true)
    public FindByIdCommentResponse findCommentById(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        FindByIdCommentResponse response = new FindByIdCommentResponse(comment);
        return response;
    }

    // post로 조회
    @Transactional(readOnly = true)
    public FindByPostIdCommentListResponse findCommentsByPostId(Long postId) {
        if(!commentRepository.existsByPostId(postId)) {
            throw new NotFoundByIdException();
        }
        List<Comment> byPostId = commentRepository.findByPostId(postId);
        FindByPostIdCommentListResponse response = new FindByPostIdCommentListResponse(byPostId);
        return response;
    }

    /*
        U: Update
        댓글 수정
     */

    @Transactional
    public String updateComment(UpdateCommentRequest updateCommentRequest) {
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
    public String deleteComment(DeleteCommentRequest deleteCommentRequest) {
        Post post = postRepository.findById(deleteCommentRequest.getId()).orElseThrow(NotFoundByIdException::new);
        User user = userRepository.findById(deleteCommentRequest.getAuthor().getId()).orElseThrow(NotFoundByIdException::new);
        Comment comment = commentRepository.findById(deleteCommentRequest.getId()).orElseThrow(NotFoundByIdException::new);
        Comment commentInPost = commentRepository.findByPostIdAndId(post.getId(), comment.getId()).orElseThrow(NotFoundByIdException::new);
        if(commentInPost.getId().equals(comment.getId())) {
            throw new NotMatchingCommentException("요청한 게시글에 수정하려는 댓글이 없습니다.");
        }
        if(commentInPost.getAuthor().getId().equals(deleteCommentRequest.getAuthor().getId())) {
            throw new NotMatchingAuthorException("댓글 작성자가 아닙니다");
        }
        commentRepository.delete(commentInPost);
        return DELETE;
    }
}
