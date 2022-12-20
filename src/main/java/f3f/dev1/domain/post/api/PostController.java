package f3f.dev1.domain.post.api;

import f3f.dev1.domain.post.application.PostService;
import f3f.dev1.domain.post.dto.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static f3f.dev1.domain.post.dto.PostDTO.*;

@RestController
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
    public ResponseEntity<Long> createPost(@RequestBody PostSaveRequest postSaveRequest) {
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
    public ResponseEntity<PostInfoDto> updatePostInfo(@RequestBody UpdatePostRequest updatePostRequest) {
        PostInfoDto postInfoDto = postService.updatePost(updatePostRequest);
        return new ResponseEntity<>(postInfoDto, HttpStatus.OK);
    }

    // 게시글 삭제


}
