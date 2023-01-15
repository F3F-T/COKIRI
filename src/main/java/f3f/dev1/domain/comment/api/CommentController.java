package f3f.dev1.domain.comment.api;

import f3f.dev1.domain.comment.application.CommentService;
import f3f.dev1.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static f3f.dev1.domain.comment.dto.CommentDTO.*;

@RestController
@Validated
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping(value = "/post/{postId}/comments")
    public ResponseEntity<CommentInfoDto> createComment(@PathVariable(name = "postId") Long postId, @RequestBody @Valid CreateCommentRequest createCommentRequest) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        CommentInfoDto commentInfoDto = commentService.saveComment(createCommentRequest, currentMemberId);
        return new ResponseEntity<>(commentInfoDto, HttpStatus.CREATED);
    }

    // 댓글 조회
    // 단일 댓글 조회 기능은 x
    // 게시글 단위 댓글 조회 기능만 제공
    @GetMapping(value = "/post/{postId}/comments")
    public ResponseEntity<List<CommentInfoDto>> findCommentsByPostId(@PathVariable(name = "postId") Long postId) {
        List<CommentInfoDto> commentInfoDtoList = commentService.findCommentDtosByPostId(postId);
        return new ResponseEntity<>(commentInfoDtoList, HttpStatus.OK);
    }

    // 댓글 수정
    @PatchMapping(value = "/post/{postId}/comments/{commentId}")
    public ResponseEntity<CommentInfoDto> updateComment(@PathVariable(name = "postId") Long postId, @PathVariable(name = "commentId") Long commentId, @RequestBody UpdateCommentRequest updateCommentRequest) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        CommentInfoDto commentInfoDto = commentService.updateComment(updateCommentRequest, currentMemberId);
        return new ResponseEntity<>(commentInfoDto, HttpStatus.OK);
    }


    // 댓글 삭제
    // TODO requestHeader로 전달하던 DeleteCommentRequest를 body로 변경
    @DeleteMapping(value = "/post/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable(name = "postId") Long postId, @PathVariable(name = "commentId") Long commentsId, @RequestBody @Valid DeleteCommentRequest deleteCommentRequest) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        String result = commentService.deleteComment(deleteCommentRequest, currentMemberId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
