package f3f.dev1.domain.post.api;

import f3f.dev1.domain.category.application.CategoryService;
import f3f.dev1.domain.post.application.PostService;
import f3f.dev1.domain.post.dto.PostDTO;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.tag.application.TagService;
import f3f.dev1.domain.tag.dto.TagDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static f3f.dev1.domain.post.dto.PostDTO.*;
import static f3f.dev1.domain.tag.dto.TagDTO.*;

@RestController
@Validated
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    private final CategoryService categoryService;

    private final TagService tagService;

    // TODO 조회 필터링은 post에서 하는 걸로

    //게시글 전체 조회
    // TODO 쿼리스트링 추가 -
    @GetMapping(value = "/post")
    public ResponseEntity<List<PostInfoDto>> getAllPostInfo(
            @RequestParam(value= "productCategory", required = false, defaultValue = "") String productCategoryName,
            @RequestParam(value= "wishCategory", required = false, defaultValue = "") String wishCategoryName,
            @RequestParam(value = "tags", required = false, defaultValue = "") List<String> tagNames) {

        // TODO 태그로 조회 서비스 로직 추가하기
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
    public ResponseEntity<PostInfoDto> getPostInfo(@PathVariable(name = "postId") Long postId) {
        PostInfoDto postInfoDto = postService.findPostById(postId);
        return new ResponseEntity<>(postInfoDto, HttpStatus.OK);
    }

    // 게시글 정보 조회 - 작성자로
    // TODO 게시글 정보 조회와 URL 형식이 똑같아 모호하다고 함. 일단 두고 나중에 필요하면 URL을 변경하겠다.
//    @GetMapping(value = "/post/{memberId}")
//    public ResponseEntity<List<PostInfoDto>> getPostInfoByAuthorName(@PathVariable(name = "memberId") Long memberId) {
//        List<PostInfoDto> postInfoDtoList = postService.findPostByAuthor(memberId);
//        return new ResponseEntity<>(postInfoDtoList, HttpStatus.OK);
//    }

    // 게시글 정보 수정
    // 기존 PathVariable 에서 RequestBody로 변경
    @PatchMapping(value = "/post/{postId}")
    public ResponseEntity<PostInfoDto> updatePostInfo(@PathVariable(name = "postId") Long postId, @RequestBody @Valid UpdatePostRequest updatePostRequest) {
        PostInfoDto postInfoDto = postService.updatePost(updatePostRequest);
        return new ResponseEntity<>(postInfoDto, HttpStatus.OK);
    }

    // 게시글 삭제
    // TODO delete method의 경우 body를 거절하는 서버가 많다고 한다. 그리고 query string 보다는 보안이 좋은 requestHeader의 사용을 추천한다고 한다.
    // TODO @RequestHeader로 했는데 테스트 방법을 모르겠다. 나중에 물어봐야겠다.
    // pathVariable로 하기 위해 일단 매개변수를 2개 받는 형식으로 갔는데, 이게 맞을지 모르겠네..?
    @DeleteMapping(value = "/post/{postId}")
    public ResponseEntity<String> deletePost(@RequestBody @Valid DeletePostRequest deletePostRequest,@PathVariable(name = "postId") Long postId) {
        String result = postService.deletePost(deletePostRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
