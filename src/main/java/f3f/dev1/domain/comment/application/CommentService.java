package f3f.dev1.domain.comment.application;

import f3f.dev1.domain.comment.dao.CommentRepository;
import f3f.dev1.domain.comment.dto.CommentDTO;
import f3f.dev1.domain.comment.exception.DuplicateCommentException;
import f3f.dev1.domain.comment.model.Comment;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.exception.NotMatchingAuthorException;
import f3f.dev1.domain.post.exception.NotMatchingCommentException;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;

import static f3f.dev1.domain.comment.dto.CommentDTO.*;
import static f3f.dev1.global.common.constants.ResponseConstants.*;

@Service
@Validated
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


    // 부모 자식 대통합
    @Transactional
    public CommentInfoDto createComment(CreateCommentRequest createCommentRequest) {
        Member user = memberRepository.findById(createCommentRequest.getAuthor().getId()).orElseThrow(NotFoundByIdException::new);
        Post post = postRepository.findById(createCommentRequest.getPost().getId()).orElseThrow(NotFoundByIdException::new);
        // 유저, 포스트 존재 확인
        if(commentRepository.existsById(createCommentRequest.getId())) {
            throw new DuplicateCommentException("이미 존재하는 댓글입니다.");
        }
        if(createCommentRequest.getParentComment() == null) {
            // 부모 댓글이 null이라면 부모 댓글로 처리
            Comment parentComment = createCommentRequest.toEntity();
            commentRepository.save(parentComment);
            CommentInfoDto commentInfoDto = CommentInfoDto.builder()
                    .memberId(parentComment.getAuthor().getId())
                    .postId(parentComment.getPost().getId())
                    .content(parentComment.getContent())
                    .depth(parentComment.getDepth())
                    .build();
            return commentInfoDto;
        } else {
            // 부모 댓글이 존재한다면 자식 댓글로 처리
            Comment parentComment = createCommentRequest.getParentComment();
            Comment comment = commentRepository.findById(createCommentRequest.getId()).orElseThrow(NotFoundByIdException::new);
            // TODO 피드백 부분, 일단은 아래와 같이 작성해두고 나중에 다시 생각해보기로 하자
            parentComment.getChilds().add(comment);
            CommentInfoDto commentInfoDto = CommentInfoDto.builder()
                    .memberId(comment.getAuthor().getId())
                    .postId(comment.getPost().getId())
                    .content(comment.getContent())
                    .depth(comment.getDepth())
                    .build();
            return commentInfoDto;
        }
    }

    /*
        R : Read
        댓글 조회
     */

    // id로 조회
    @Transactional(readOnly = true)
    public CommentInfoDto findCommentById(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(NotFoundByIdException::new);
//        FindByIdCommentResponse response = new FindByIdCommentResponse(comment);
        CommentInfoDto commentInfoDto = CommentInfoDto.builder()
                .memberId(comment.getAuthor().getId())
                .content(comment.getContent())
                .depth(comment.getDepth())
                .postId(id)
                .build();
        return commentInfoDto;
    }

    // DTO의 리스트로 수정하기
    // post로 조회
    @Transactional(readOnly = true)
    public List<CommentInfoDto> findCommentsByPostId(Long postId) {
        if(!commentRepository.existsByPostId(postId)) {
            throw new NotFoundByIdException();
        }
        List<CommentInfoDto> commentInfoDtoList = new ArrayList<>();
        List<Comment> comments = commentRepository.findByPostId(postId);
        for (Comment comment : comments) {
            CommentInfoDto commentInfoDto = CommentInfoDto.builder()
                    .memberId(comment.getAuthor().getId())
                    .postId(comment.getPost().getId())
                    .content(comment.getContent())
                    .depth(comment.getDepth())
                    .build();
            commentInfoDtoList.add(commentInfoDto);
        }
        return commentInfoDtoList;
    }

    /*
        U: Update
        댓글 수정
     */

    @Transactional
    public CommentInfoDto updateComment(UpdateCommentRequest updateCommentRequest) {
        Post post = postRepository.findById(updateCommentRequest.getId()).orElseThrow(NotFoundByIdException::new);
        Comment comment = commentRepository.findById(updateCommentRequest.getId()).orElseThrow(NotFoundByIdException::new);
        Comment commentInPost = commentRepository.findByPostIdAndId(post.getId(), comment.getId()).orElseThrow(NotFoundByIdException::new);
        if(!commentInPost.getId().equals(comment.getId())) {
            throw new NotMatchingCommentException("요청한 게시글에 수정하려는 댓글이 없습니다.");
        }
        commentInPost.updateContent(updateCommentRequest.getContent());
        CommentInfoDto commentInfoDto = CommentInfoDto.builder()
                .postId(comment.getPost().getId())
                .memberId(comment.getAuthor().getId())
                .content(comment.getContent())
                .depth(comment.getDepth())
                .build();
        return commentInfoDto;
    }

    @Transactional
    public String deleteComment(DeleteCommentRequest deleteCommentRequest) {
        Post post = postRepository.findById(deleteCommentRequest.getId()).orElseThrow(NotFoundByIdException::new);
        Member user = memberRepository.findById(deleteCommentRequest.getAuthor().getId()).orElseThrow(NotFoundByIdException::new);
        Comment comment = commentRepository.findById(deleteCommentRequest.getId()).orElseThrow(NotFoundByIdException::new);
        Comment commentInPost = commentRepository.findByPostIdAndId(post.getId(), comment.getId()).orElseThrow(NotFoundByIdException::new);
        if(commentInPost.getId().equals(comment.getId())) {
            throw new NotMatchingCommentException("요청한 게시글에 수정하려는 댓글이 없습니다.");
        }
        if(commentInPost.getAuthor().getId().equals(deleteCommentRequest.getAuthor().getId())) {
            throw new NotMatchingAuthorException("댓글 작성자가 아닙니다");
        }
        commentRepository.delete(commentInPost);
        return "DELETE";
    }
}
