package f3f.dev1.domain.post.api;

import f3f.dev1.domain.category.application.CategoryService;
import f3f.dev1.domain.post.application.PostService;
import f3f.dev1.domain.post.dto.PostDTO;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.tag.application.TagService;
import f3f.dev1.domain.tag.dto.TagDTO;
import f3f.dev1.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static f3f.dev1.domain.post.dto.PostDTO.*;
import static f3f.dev1.domain.tag.dto.TagDTO.*;

@RestController
@Validated
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    private final TagService tagService;

//    private final CategoryService categoryService;

//    private final TagService tagService;

    // TODO 조회 필터링은 post에서 하는 걸로
    // => 안되겠다. 컨트롤러같은 웹 계층이 없어도 애플리케이션이 동작할 수 있어야 한다.

    //게시글 전체 조회
    // TODO 쿼리스트링 추가 - price 추가 예정
    @GetMapping(value = "/post")
    public ResponseEntity<List<PostInfoDto>> getAllPostInfo(
            @RequestParam(value= "productCategory", required = false, defaultValue = "") String productCategoryName,
            @RequestParam(value= "wishCategory", required = false, defaultValue = "") String wishCategoryName,
            @RequestParam(value = "tags", required = false, defaultValue = "") List<String> tagNames) {

        List<PostInfoDto> responseList = new ArrayList<>();

        if(productCategoryName.equals("") && wishCategoryName.equals("") && !tagNames.isEmpty()) {
            List<PostInfoDto> postInfoDtoList = tagService.getPostsByTagNames(tagNames);
            responseList.addAll(postInfoDtoList);
        } else {
            List<PostInfoDto> allPosts = postService.findAllPosts();
            responseList.addAll(allPosts);
        }
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    // 게시글 작성
    @PostMapping(value = "/post")
    public ResponseEntity<Long> createPost(@RequestBody @Valid PostSaveRequest postSaveRequest) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        Long postId = postService.savePost(postSaveRequest, currentMemberId);
        tagService.addTagsToPost(postId, postSaveRequest.getTagNames());
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
    // TODO 태그가 업데이트DTO로 들어오면 생성때랑 마찬가지로 추가를 해줘야 한다. 그리고 원래 있던 PostTag는 지워줘야 한다...
    @PatchMapping(value = "/post/{postId}")
    public ResponseEntity<PostInfoDto> updatePostInfo(@PathVariable(name = "postId") Long postId, @RequestBody @Valid UpdatePostRequest updatePostRequest) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();

        PostInfoDto postInfoDto = postService.updatePost(updatePostRequest, currentMemberId);
        return new ResponseEntity<>(postInfoDto, HttpStatus.OK);
    }

    // 게시글 삭제
    // TODO delete method의 경우 body를 거절하는 서버가 많다고 한다. 그리고 query string 보다는 보안이 좋은 requestHeader의 사용을 추천한다고 한다.
    // TODO @RequestHeader로 했는데 테스트 방법을 모르겠다. 나중에 물어봐야겠다.
    @DeleteMapping(value = "/post/{postId}")
    public ResponseEntity<String> deletePost(@RequestBody @Valid DeletePostRequest deletePostRequest,@PathVariable(name = "postId") Long postId) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        String result = postService.deletePost(deletePostRequest, currentMemberId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
