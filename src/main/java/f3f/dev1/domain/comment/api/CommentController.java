package f3f.dev1.domain.comment.api;

import f3f.dev1.domain.comment.application.CommentService;
import f3f.dev1.domain.comment.dto.CommentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static f3f.dev1.domain.comment.dto.CommentDTO.*;

@RestController
@Validated
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
//    @PostMapping(value = "/post/{postId}/comments")
//    public ResponseEntity<CommentInfoDto> createComment(@PathVariable Long postId, @RequestBody @Valid CreateCommentRequest createCommentRequest) {
//        Long commentId = commentService.createComment(createCommentRequest);
//        // TODO createComment쪽 수정하기, DTO로 뱉어주게
//    }

    // 댓글 조회
    // 단일 댓글 조회 기능은 x
    // 게시글 단위 댓글 조회 기능만 제공
    @GetMapping(value = "/post/")



    // 댓글 수정

    // 댓글 삭제
    @DeleteMapping(value = "/post/{postId}/comments/{commentsId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentsId, @RequestHeader @Valid DeleteCommentRequest deleteCommentRequest) {
        String result = commentService.deleteComment(deleteCommentRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
