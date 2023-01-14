package f3f.dev1.domain.post.api;

import f3f.dev1.domain.category.application.CategoryService;
import f3f.dev1.domain.post.application.PostService;
import f3f.dev1.domain.post.dto.PostDTO;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.tag.application.PostTagService;
import f3f.dev1.domain.tag.application.TagService;
import f3f.dev1.domain.tag.dto.TagDTO;
import f3f.dev1.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    private final PostTagService postTagService;


    //게시글 전체 조회
//    @GetMapping(value = "/post")
//    public ResponseEntity<List<PostInfoDtoWithTag>> getAllPostInfo(
//            @RequestParam(value= "productCategory", required = false, defaultValue = "") String productCategoryName,
//            @RequestParam(value= "wishCategory", required = false, defaultValue = "") String wishCategoryName,
//            @RequestParam(value = "tags", required = false, defaultValue = "") List<String> tagNames,
//            @RequestParam(value = "minPrice", required = false, defaultValue = "") String minPrice,
//            @RequestParam(value = "maxPrice", required = false, defaultValue = "") String maxPrice) {
//
//        SearchPostRequest searchPostRequest = SearchPostRequest.builder()
//                .productCategory(productCategoryName)
//                .wishCategory(wishCategoryName)
//                .tagNames(tagNames)
//                .minPrice(minPrice)
//                .maxPrice(maxPrice)
//                .build();
//        List<PostInfoDtoWithTag> responseList = postService.findPostsWithConditions(searchPostRequest);
//        return new ResponseEntity<>(responseList, HttpStatus.OK);
//    }

    // 게시글 전체 조회 세분화 - 태그 제외 조건들 검색
    @GetMapping(value = "/post")
    public ResponseEntity<Page<PostSearchResponseDto>> getPostsWithConditionExcludeTags(
            @RequestParam(value= "productCategory", required = false, defaultValue = "") String productCategoryName,
            @RequestParam(value= "wishCategory", required = false, defaultValue = "") String wishCategoryName,
            @RequestParam(value = "minPrice", required = false, defaultValue = "") String minPrice,
            @RequestParam(value = "maxPrice", required = false, defaultValue = "") String maxPrice,
            Pageable pageable) {
            SearchPostRequestExcludeTag request = SearchPostRequestExcludeTag.builder()
                    .productCategory(productCategoryName)
                    .wishCategory(wishCategoryName)
                    .minPrice(minPrice)
                    .maxPrice(maxPrice)
                    .build();
            Page<PostSearchResponseDto> pageDto = postService.findPostsByCategoryAndPriceRange(request, pageable);
            return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }

    // TODO 임의 생성, 태그 검색이 별도의 컴포넌트로 생성될 수 있나? - 프론트랑 얘기해보기
    @GetMapping(value = "/post/tagSearch")
    public ResponseEntity<Page<PostSearchResponseDto>> getPostsWithTagNames(
            @RequestParam(value = "tags", required = false, defaultValue = "") List<String> tagNames,
            Pageable pageable) {
        Page<PostSearchResponseDto> resultList;
        if(!tagNames.isEmpty()) {
            resultList = postService.findPostsWithTagNameList(tagNames, pageable);
        } else {
            resultList = postService.findAll(pageable);
        }
        return new ResponseEntity<>(resultList, HttpStatus.OK);
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
    public ResponseEntity<SinglePostInfoDto> getPostInfo(@PathVariable(name = "postId") Long postId) {
        SinglePostInfoDto postInfoDto = postService.findPostById(postId);
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
    public ResponseEntity<PostInfoDtoWithTag> updatePostInfo(@PathVariable(name = "postId") Long postId, @RequestBody @Valid UpdatePostRequest updatePostRequest) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        postTagService.deletePostTagFromPost(postId);
        PostInfoDtoWithTag postInfoDto = postService.updatePost(updatePostRequest, currentMemberId);
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
