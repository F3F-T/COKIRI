package f3f.dev1.domain.post.api;

import f3f.dev1.domain.post.application.PostService;
import f3f.dev1.domain.post.dto.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static f3f.dev1.domain.post.dto.PostDTO.*;

@RestController
@Validated
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    //게시글 전체 조회
    @GetMapping(value = "/post")
    public ResponseEntity<List<PostInfoDto>> getAllPostInfo() {
        List<PostInfoDto> allPosts = postService.findAllPosts();
        return new ResponseEntity<>(allPosts, HttpStatus.OK);
    }

    // 게시글 작성
    @PostMapping(value = "/post")
    public ResponseEntity<Long> createPost(@RequestBody @Valid PostSaveRequest postSaveRequest) {
        Long postId = postService.savePost(postSaveRequest);
        return new ResponseEntity<>(postId, HttpStatus.CREATED);
    }

    // 게시글 정보 조회
    @GetMapping(value = "/post/{postId}")
    public ResponseEntity<PostInfoDto> getPostInfo(@PathVariable Long postId) {
        PostInfoDto postInfoDto = postService.findPostById(postId);
        return new ResponseEntity<>(postInfoDto, HttpStatus.OK);
    }

    // 게시글 정보 조회 - 작성자로
    @GetMapping(value = "/post/{memberId}")
    public ResponseEntity<List<PostInfoDto>> getPostInfoByAuthorName(@PathVariable Long memberId) {
        List<PostInfoDto> postInfoDtoList = postService.findPostByAuthor(memberId);
        return new ResponseEntity<>(postInfoDtoList, HttpStatus.OK);
    }

    // 게시글 정보 수정
    // 기존 PathVariable 에서 RequestBody로 변경
    @PatchMapping(value = "/post")
    public ResponseEntity<PostInfoDto> updatePostInfo(@RequestBody @Valid UpdatePostRequest updatePostRequest) {
        PostInfoDto postInfoDto = postService.updatePost(updatePostRequest);
        return new ResponseEntity<>(postInfoDto, HttpStatus.OK);
    }

    // 게시글 삭제
    // TODO delete method의 경우 body를 거절하는 서버가 많다고 한다. 그리고 query string 보다는 보안이 좋은 requestHeader의 사용을 추천한다고 한다.
    // pathVariable로 하기 위해 일단 매개변수를 2개 받는 형식으로 갔는데, 이게 맞을지 모르겠네..?
    @DeleteMapping(value = "/post/{postId}")
    public ResponseEntity<String> deletePost(@RequestHeader @Valid DeletePostRequest deletePostRequest,@PathVariable Long postId) {
        String result = postService.deletePost(deletePostRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
