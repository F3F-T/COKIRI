package f3f.dev1.domain.tag.application;

import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.dto.PostDTO;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.tag.dao.PostTagRepository;
import f3f.dev1.domain.tag.dao.TagRepository;
import f3f.dev1.domain.tag.exception.DuplicateTagException;
import f3f.dev1.domain.tag.model.PostTag;
import f3f.dev1.domain.tag.model.Tag;
import f3f.dev1.global.error.exception.NotFoundByIdException;
import f3f.dev1.global.util.DeduplicationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;

import static f3f.dev1.domain.post.dto.PostDTO.*;
import static f3f.dev1.domain.tag.dto.TagDTO.*;
import static f3f.dev1.global.common.constants.ResponseConstants.DELETE;

@Service
@Validated
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final PostRepository postRepository;
    private final PostTagRepository postTagRepository;
    private final MemberRepository memberRepository;

    /*
        C : create
        태그 자체의 생성과 게시글에 태그를 추가하는 2가지 경우로 분리
     */

    @Transactional
    public Long createTag(CreateTagRequest createTagRequest) {
        if(tagRepository.existsByName(createTagRequest.getName())) {
            throw new DuplicateTagException("이미 존재하는 태그명입니다.");
        }
        if(!memberRepository.existsById(createTagRequest.getAuthorId())) {
            throw new NotFoundByIdException("요청자가 존재하지 않는 사용자입니다.");
        }
        Tag tag = createTagRequest.toEntity();
        tagRepository.save(tag);
        return tag.getId();
    }

    public Long addTagToPost(AddTagToPostRequest addTagToPostRequest) {
        Post post = postRepository.findById(addTagToPostRequest.getPostId()).orElseThrow(NotFoundByIdException::new);
        Tag tag = tagRepository.findById(addTagToPostRequest.getTagId()).orElseThrow(NotFoundByIdException::new);
        // TODO 예외를 던질 필요까지는 없나?
        if(postTagRepository.existsByPostAndTag(post, tag)) {
            throw new DuplicateTagException("게시글에 이미 해당 태그가 존재합니다");
        }
        PostTag postTag = PostTag.builder()
                .post(post)
                .tag(tag)
                .build();
        postTagRepository.save(postTag);
//        tag.getPostTags().add(postTag);
//        post.getPostTags().add(postTag);
//        tag.addToPostTags(postTag);
//        post.addToPostTags(postTag);

        return postTag.getId();
    }

    // TODO 테스트코드 필시 작성해봐야 함.
    public Long addTagsToPost(Long postId, List<String> tagNames) {
        Post post = postRepository.findById(postId).orElseThrow(NotFoundByIdException::new);
        if(tagNames.isEmpty()) {
            // 추가한 태그가 없다면 바로 게시글을 생성해도 된다.
            return post.getId();
        } else {
            List<Tag> tags = tagRepository.findByNameIn(tagNames);
            // 태그들을 일일이 게시글에 추가해주는 작업
            for (Tag tag : tags) {
                // 게시글을 처음 생성하는 시점에는 이미 존재하는 태그 자체가 없으니 예외처리는 따로 하지 않겠다.
                PostTag postTag = PostTag.builder()
                        .post(post)
                        .tag(tag)
                        .build();
                postTagRepository.save(postTag);
//                tag.getPostTags().add(postTag);
//                post.getPostTags().add(postTag);
            }
            return post.getId();
        }
    }

    /*
        R : read
        태그로 게시글을 조회하는 경우만 고려하겠다.
        태그 자체에 대한 조회는 필요 없을 것 같아서.
     */

    @Transactional(readOnly = true)
    public List<PostInfoDto> getPostsByTagId(GetPostListByTagIdRequest request) {
        // 먼저 postTag 리스트를 찾고 하나하나 포스트를 찾아서 추가해준다.
        if(!tagRepository.existsById(request.getId())) {
            throw new NotFoundByIdException();
        }
        List<PostTag> postTagList = postTagRepository.findByTagId(request.getId());
        List<Post> postList = new ArrayList<>();
        List<PostInfoDto> response = new ArrayList<>();
        for (PostTag postTagEach : postTagList) {
            postList.add(postTagEach.getPost());
        }
        for (Post post : postList) {
                PostInfoDto responseEach = PostInfoDto.builder()
                        .title(post.getTitle())
                        .content(post.getContent())
                        .tradeEachOther(post.getTradeEachOther())
                        .authorNickname(post.getAuthor().getNickname())
                        .wishCategory(post.getWishCategory().getName())
                        .productCategory(post.getProductCategory().getName())
                        // TODO trade가 지금은 null 이라서 여기서 이렇게 받아와버리면 nullPointerException이 떠버린다.
//                    .tradeStatus(post.getTrade().getTradeStatus())
                        .build();
                response.add(responseEach);
        }
        return response;
    }

    @Transactional(readOnly = true)
    public List<PostInfoDto> getPostsByTagName(String tagName) {
        // 먼저 postTag 리스트를 찾고 하나하나 포스트를 찾아서 추가해준다.
        if(!tagRepository.existsByName(tagName)) {
            throw new NotFoundByIdException();
        }
        List<PostTag> postTagList = postTagRepository.findByTagName(tagName);
        List<Post> postList = new ArrayList<>();
        List<PostInfoDto> response = new ArrayList<>();
        for (PostTag postTagEach : postTagList) {
            postList.add(postTagEach.getPost());
        }
        for (Post post : postList) {
            PostInfoDto responseEach = PostInfoDto.builder()
                    .title(post.getTitle())
                    .content(post.getContent())
                    .tradeEachOther(post.getTradeEachOther())
                    .authorNickname(post.getAuthor().getNickname())
                    .wishCategory(post.getWishCategory().getName())
                    .productCategory(post.getProductCategory().getName())
                    // TODO trade가 지금은 null 이라서 여기서 이렇게 받아와버리면 nullPointerException이 떠버린다.
//                    .tradeStatus(post.getTrade().getTradeStatus())
                    .build();
            response.add(responseEach);
        }
        return response;
    }

    // 고민 : 여러 개의 해시태그가 들어오면 걔네를 다 가지고 있는 게시글만 보여줘야하나? 하나라도 포함이면 보여줘야 하나?
    // ==> 다 가지고 있는 애만 보여주는 걸로
    @Transactional(readOnly = true)
    public List<PostInfoDto> getPostsByTagNames(List<String> names) {
        List<Post> resultPostList = new ArrayList<>();
        List<PostInfoDto> response = new ArrayList<>();

        if(names.isEmpty()) {
            List<Post> all = postRepository.findAll();
            for (Post post : all) {
                PostInfoDto responseEach = PostInfoDto.builder()
                        .title(post.getTitle())
                        .content(post.getContent())
                        .tradeEachOther(post.getTradeEachOther())
                        .authorNickname(post.getAuthor().getNickname())
                        .wishCategory(post.getWishCategory().getName())
                        .productCategory(post.getProductCategory().getName())
                        // TODO trade가 지금은 null 이라서 여기서 이렇게 받아와버리면 nullPointerException이 떠버린다.
//                    .tradeStatus(post.getTrade().getTradeStatus())
                        .build();
                response.add(responseEach);
            }
        } else {
            for(int i=0; i<names.size(); i++) {
                List<PostTag> postTags = postTagRepository.findByTagName(names.get(i));
                List<Post> posts = postRepository.findByPostTagsIn(postTags);
                // 첫번째 루프면 결과 리스트를 전부 저장하고,
                // 두번째 루프 이상부터는 결과 리스트와의 교집합만 저장한다.
                if(i == 0) {
                    resultPostList.addAll(posts);
                } else {
                    resultPostList.retainAll(posts);
                }
            }
//        List<Post> deduplicatedList = DeduplicationUtils.deduplication(resultPostList, Post::getId);
            for (Post post : resultPostList) {
                PostInfoDto responseEach = PostInfoDto.builder()
                        .title(post.getTitle())
                        .content(post.getContent())
                        .tradeEachOther(post.getTradeEachOther())
                        .authorNickname(post.getAuthor().getNickname())
                        .wishCategory(post.getWishCategory().getName())
                        .productCategory(post.getProductCategory().getName())
                        // TODO trade가 지금은 null 이라서 여기서 이렇게 받아와버리면 nullPointerException이 떠버린다.
//                    .tradeStatus(post.getTrade().getTradeStatus())
                        .build();
                response.add(responseEach);
            }
        }
        return response;
    }


    /*
        U : update
        태그는 업데이트가 필요 없어보인다.
        게시글에 들어가는 태그는 삭제 후 재생성이 일반적이기 때문
     */

    /*
        D : delete
        게시글에 포함된 태그를 지우는 경우와 태그 자체를 지우는 2가지 경우로 나눠야 하나?
        TODO 태그 삭제가 조금 애매하다. 관리자가 태그를 지우나? 일반인이 지우면 문제가 생길 것 같은데
     */

    // TODO 삭제는 피드백에서 일단 보류하라고 했다.
//    @Transactional
//    public ResponseEntity<String> deleteTagFromPost(DeleteTagFromPostRequest request) {
//        /*
//            확인해야할 것들
//            1. 요청으로 넘어온 태그가 존재하는 태그인가
//            2. 해당 태그가 요청으로 넘어온 게시글에 포함되어있나
//            3. 태그를 삭제하려는 요청자가 게시글 작성자 본인인가
//         */
//        return DELETE;
//    }
}
