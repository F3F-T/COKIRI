package f3f.dev1.domain.comment.application;

import f3f.dev1.domain.comment.dao.CommentRepository;
import f3f.dev1.domain.comment.exception.DeletedCommentException;
import f3f.dev1.domain.comment.model.Comment;
import f3f.dev1.domain.member.exception.NotAuthorizedException;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.exception.NotMatchingAuthorException;
import f3f.dev1.domain.post.exception.NotMatchingCommentException;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

import static f3f.dev1.domain.comment.dto.CommentDTO.*;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    /*
        C : Create
        2가지 경우(Parent Comment와 Child Comment를 생성하는 경우)로 나누겠음
        -> 취소, 하나로 통일하고 builder 활용
     */


    // TODO : 부모 댓글이 null인데 depth가 1 이상인 요청들 잡아내야함, 비슷한 맥락으로 부모 있는데 depth 0인 요청 잡아내야 함
    @Transactional
    public CommentInfoDto saveComment(CreateCommentRequest createCommentRequest, Long currentMemberId) {
        Member user = memberRepository.findById(createCommentRequest.getAuthorId()).orElseThrow(NotFoundByIdException::new);
        Post post = postRepository.findById(createCommentRequest.getPostId()).orElseThrow(NotFoundByIdException::new);
        if(!currentMemberId.equals(user.getId())) {
            throw new NotAuthorizedException("요청자가 현재 로그인한 유저가 아닙니다");
        }
        if(createCommentRequest.getParentCommentId() == null) {
            // 부모 댓글이 null이라면 부모 댓글로 처리
            Comment parentComment = createCommentRequest.toEntity(post, user, null);
            commentRepository.save(parentComment);
            CommentInfoDto commentInfoDto = parentComment.toInfoDto();
            return commentInfoDto;
        } else {
            // 부모 댓글이 존재한다면 자식 댓글로 처리
            Comment parentComment = commentRepository.findById(createCommentRequest.getParentCommentId()).orElseThrow(NotFoundByIdException::new);
            Comment comment = createCommentRequest.toEntity(post, user, parentComment);
            commentRepository.save(comment);
            CommentInfoDto commentInfoDto = comment.toInfoDto();
            return commentInfoDto;
        }
    }

    /*
        R : Read
        댓글 조회
     */

    // id로 조회
    // 현재는 사용하지 않는 로직
    @Transactional(readOnly = true)
    public CommentInfoDto findCommentById(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(NotFoundByIdException::new);
//        FindByIdCommentResponse response = new FindByIdCommentResponse(comment);
        CommentInfoDto commentInfoDto = comment.toInfoDto();
        return commentInfoDto;
    }

    // DTO의 리스트로 수정하기
    // post로 조회
    @Transactional(readOnly = true)
    public List<CommentInfoDto> findCommentsByPostId(Long postId) {
        List<CommentInfoDto> commentInfoDtoList = new ArrayList<>();
        List<Comment> comments = commentRepository.findByPostId(postId);
        for (Comment comment : comments) {
            CommentInfoDto commentInfoDto = comment.toInfoDto();
            commentInfoDtoList.add(commentInfoDto);
        }
        return commentInfoDtoList;
    }

    /*
        U: Update
        댓글 수정
     */

    // TODO 요청으로 들어온 애는 부모가 없는데 부모가 지정돼서 오거나 그 반대 경우로 들어오는 요청들 커트하기
    @Transactional
    public CommentInfoDto updateComment(UpdateCommentRequest updateCommentRequest, Long currentMemberId) {
        Post post = postRepository.findById(updateCommentRequest.getPostId()).orElseThrow(NotFoundByIdException::new);
        Comment comment = commentRepository.findById(updateCommentRequest.getId()).orElseThrow(NotFoundByIdException::new);
        Member user = memberRepository.findById(updateCommentRequest.getAuthorId()).orElseThrow(NotFoundByIdException::new);
        Comment commentInPost = commentRepository.findByPostIdAndId(post.getId(), comment.getId()).orElseThrow(NotFoundByIdException::new);
        // 상위 댓글이 없는 경우까지 고려해주겠다.
        if(updateCommentRequest.getParentId() != null) {
            if(!commentRepository.existsById(updateCommentRequest.getParentId())) {
                throw new DeletedCommentException();
            }
        }
        if(!commentInPost.getId().equals(comment.getId())) {
            throw new NotMatchingCommentException("요청한 게시글에 수정하려는 댓글이 없습니다.");
        }
        if(!currentMemberId.equals(user.getId())) {
            throw new NotAuthorizedException("요청자가 현재 로그인한 유저가 아닙니다");
        }
        commentInPost.updateContent(updateCommentRequest.getContent());
        CommentInfoDto commentInfoDto = comment.toInfoDto();
        return commentInfoDto;
    }

    @Transactional
    public String deleteComment(DeleteCommentRequest deleteCommentRequest, Long currentMemberId) {
        Post post = postRepository.findById(deleteCommentRequest.getPostId()).orElseThrow(NotFoundByIdException::new);
        Member user = memberRepository.findById(deleteCommentRequest.getAuthorId()).orElseThrow(NotFoundByIdException::new);
        Comment comment = commentRepository.findById(deleteCommentRequest.getId()).orElseThrow(NotFoundByIdException::new);
        Comment commentInPost = commentRepository.findByPostIdAndId(post.getId(), comment.getId()).orElseThrow(NotFoundByIdException::new);
        if(!commentInPost.getId().equals(comment.getId())) {
            throw new NotMatchingCommentException("요청한 게시글에 삭제하려는 댓글이 없습니다.");
        }
        if(!commentInPost.getAuthor().getId().equals(deleteCommentRequest.getAuthorId())) {
            throw new NotMatchingAuthorException("댓글 작성자가 아닙니다");
        }
        if(!currentMemberId.equals(user.getId())) {
            throw new NotAuthorizedException("요청자가 현재 로그인한 유저가 아닙니다");
        }
        // 하위 댓글들 다 삭제
        if(commentInPost.getParent() != null) {
            commentRepository.delete(commentInPost);
        } else {
            for (Comment childComment : commentInPost.getChilds()) {
                commentRepository.delete(childComment);
            }
            commentRepository.delete(commentInPost);
        }
        return "DELETE";
    }
}
