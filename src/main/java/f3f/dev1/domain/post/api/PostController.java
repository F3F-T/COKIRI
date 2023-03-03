package f3f.dev1.domain.post.api;

import f3f.dev1.domain.member.dto.MemberDTO;
import f3f.dev1.domain.member.exception.NotAuthorizedException;
import f3f.dev1.domain.post.application.PostService;
import f3f.dev1.domain.postImage.application.PostImageService;
import f3f.dev1.domain.tag.application.PostTagService;
import f3f.dev1.domain.tag.application.TagService;
import f3f.dev1.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static f3f.dev1.domain.member.dto.MemberDTO.*;
import static f3f.dev1.domain.post.dto.PostDTO.*;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    private final TagService tagService;

    private final PostTagService postTagService;

    private final PostImageService postImageService;


    // 게시글 전체 조회 세분화 - 태그 제외 조건들 검색
    @GetMapping(value = "/post")
    public ResponseEntity<Page<PostSearchResponseDto>> getPostsWithConditionExcludeTags(
            @RequestParam(value= "productCategory", required = false, defaultValue = "") String productCategoryName,
            @RequestParam(value= "wishCategory", required = false, defaultValue = "") String wishCategoryName,
            @RequestParam(value = "minPrice", required = false, defaultValue = "") String minPrice,
            @RequestParam(value = "maxPrice", required = false, defaultValue = "") String maxPrice,
            @RequestParam(value = "tradable", required = true, defaultValue = "1") String tradable,
            Pageable pageable) {
        Long currentMemberId = SecurityUtil.getCurrentNullableMemberId();
            SearchPostRequestExcludeTag request = SearchPostRequestExcludeTag.builder()
                    .productCategory(productCategoryName)
                    .wishCategory(wishCategoryName)
                    .minPrice(minPrice)
                    .maxPrice(maxPrice)
                    .tradable(tradable)
                    .build();
            Page<PostSearchResponseDto> pageDto = postService.findPostsByCategoryAndPriceRange(request, currentMemberId, pageable);
            return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }

    /*
        쿼리 dsl을 활용한 한방 쿼리를 시험해보기 위한 테스트용 컨트롤러.
        총 조회 결과 성능을 기존의 쿼리랑 비교하기 위해 사용될 예정이다.
     */
//    @GetMapping(value = "/post/customQ")
//    public ResponseEntity<Page<PostSearchResponseDto>> getPostsWithConditionExcludeTagsByCustomQuery(
//            @RequestParam(value= "productCategory", required = false, defaultValue = "") String productCategoryName,
//            @RequestParam(value= "wishCategory", required = false, defaultValue = "") String wishCategoryName,
//            @RequestParam(value = "minPrice", required = false, defaultValue = "") String minPrice,
//            @RequestParam(value = "maxPrice", required = false, defaultValue = "") String maxPrice,
//            Pageable pageable) {
//        Long currentMemberId = SecurityUtil.getCurrentNullableMemberId();
//        SearchPostRequestExcludeTag request = SearchPostRequestExcludeTag.builder()
//                .productCategory(productCategoryName)
//                .wishCategory(wishCategoryName)
//                .minPrice(minPrice)
//                .maxPrice(maxPrice)
//                .build();
//        Page<PostSearchResponseDto> pageDto = postService.findPostsByCategoryAndPriceRangeWithCustomQuery(request, currentMemberId, pageable);
//        return new ResponseEntity<>(pageDto, HttpStatus.OK);
//    }

    @GetMapping(value = "/post/tagSearch")
    public ResponseEntity<Page<PostSearchResponseDto>> getPostsWithTagNames(
            @RequestParam(value = "tags", required = false, defaultValue = "") List<String> tagNames,
            Pageable pageable) {
        Page<PostSearchResponseDto> resultList;
        Long currentMemberId = SecurityUtil.getCurrentNullableMemberId();
        if(!tagNames.isEmpty()) {
            resultList = postService.findPostsWithTagNameList(tagNames, currentMemberId, pageable);
        } else {
            resultList = postService.findAll(currentMemberId, pageable);
        }
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    @GetMapping(value = "/post/user/{memberId}")
    public ResponseEntity<Page<GetUserPost>> getUserPostById(@PathVariable(name = "memberId") Long memberId, Pageable pageable) {
        return ResponseEntity.ok(postService.findPostByAuthorId(memberId, pageable));
    }

    // 게시글 작성
    @PostMapping(value = "/post")
    public ResponseEntity<Long> createPost(@RequestBody PostSaveRequest postSaveRequest) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        Long postId = postService.savePost(postSaveRequest, currentMemberId);
        tagService.addTagsToPost(postId, postSaveRequest.getTagNames());
        List<String> images = postSaveRequest.getImages();
        postImageService.savePostImages(images, postId);
        return new ResponseEntity<>(postId, HttpStatus.CREATED);
    }

    // 게시글 정보 조회
    // TODO 해당 게시글이 본인이 스크랩한 게시글인지 판단해줘야 함
    // scrapPost랑 scrap을 postId로 조인 걸고 userId로 존재하는 scrapPost 있는지 찾아보기 - 네이티브 쿼리로 짜야하나
    @GetMapping(value = "/post/{postId}")
    public ResponseEntity<SinglePostInfoDto> getPostInfo(@PathVariable(name = "postId") Long postId) {
        Long currentMemberId = SecurityUtil.getCurrentNullableMemberId();
        SinglePostInfoDto postInfoDto = postService.findPostById(postId, currentMemberId);
        return new ResponseEntity<>(postInfoDto, HttpStatus.OK);
    }

//    // 게시글 정보 수정
//    // 기존 PathVariable 에서 RequestBody로 변경
//    // TODO 태그가 업데이트DTO로 들어오면 생성때랑 마찬가지로 추가를 해줘야 한다. 그리고 원래 있던 PostTag는 지워줘야 한다...
//    @PatchMapping(value = "/post/{postId}")
//    public ResponseEntity<PostInfoDtoWithTag> updatePostInfo(@PathVariable(name = "postId") Long postId, @RequestBody @Valid UpdatePostRequest updatePostRequest) {
//        Long currentMemberId = SecurityUtil.getCurrentMemberId();
//        postTagService.deletePostTagFromPost(postId);
//        PostInfoDtoWithTag postInfoDto = postService.updatePost(updatePostRequest, postId, currentMemberId);
//        return new ResponseEntity<>(postInfoDto, HttpStatus.OK);
//    }

    // 게시글 정보 수정
    @PatchMapping(value = "/post/{postId}")
    public ResponseEntity<String> updatePostInfo(@PathVariable(name = "postId") Long postId, @RequestBody @Valid UpdatePostRequest updatePostRequest) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();

        // currentMemberId 관련 예외는 여기서 일괄 처리하겠다.
        if(!updatePostRequest.getAuthorId().equals(currentMemberId)) {
            throw new NotAuthorizedException("요청자가 현재 로그인한 유저가 아닙니다");
        }

        // 아래 두 항목은 각각 코드가 길기도 하고 담당하는 서비스가 나눠지기 때문에 별도로 처리해준다.
        // 두개를 제외한 나머지 로직은 postService에서 기존처럼 update한다.
        if(updatePostRequest.getTagNames() != null) {
            postTagService.updatePostTagWithPatch(postId, updatePostRequest);
        }
        if(updatePostRequest.getImages() != null) {
            postImageService.updatePostImagesWithPatch(postId, updatePostRequest.getImages());
        }
        postService.updatePostWithPatch(updatePostRequest, postId);
//        return new ResponseEntity<>(postInfoDto, HttpStatus.OK);
        return new ResponseEntity<>("UPDATED", HttpStatus.OK);
    }

    // 게시글 삭제
    @DeleteMapping(value = "/post/{postId}")
    public ResponseEntity<String> deletePost(@RequestBody @Valid DeletePostRequest deletePostRequest,@PathVariable(name = "postId") Long postId) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        String result = postService.deletePost(deletePostRequest, currentMemberId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
