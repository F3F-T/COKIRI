package f3f.dev1.domain.tag.application;

import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.tag.dao.PostTagRepository;
import f3f.dev1.domain.tag.dao.TagRepository;
import f3f.dev1.domain.tag.exception.DuplicateTagException;
import f3f.dev1.domain.tag.model.PostTag;
import f3f.dev1.domain.tag.model.Tag;
import f3f.dev1.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;

import static f3f.dev1.domain.tag.dto.TagDTO.*;

@Service
@Validated
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final PostRepository postRepository;
    private final PostTagRepository postTagRepository;

    /*
        C : create
        태그 자체의 생성과 게시글에 태그를 추가하는 2가지 경우로 분리
     */

    @Transactional
    public Long createTag(CreateTagRequest createTagRequest) {
        if(tagRepository.existsById(createTagRequest.getId())) {
            throw new DuplicateTagException("이미 존재하는 태그입니다.");
        }
        if(tagRepository.existsByName(createTagRequest.getName())) {
            throw new DuplicateTagException("이미 존재하는 태그명입니다.");
        }
        Tag tag = createTagRequest.toEntity();
        tagRepository.save(tag);
        return tag.getId();
    }

    public Long addTagToPost(AddTagToPostRequest addTagToPostRequest) {
        Post post = postRepository.findById(addTagToPostRequest.getPost().getId()).orElseThrow(NotFoundByIdException::new);
        Tag tag = tagRepository.findById(addTagToPostRequest.getId()).orElseThrow(NotFoundByIdException::new);
        if(postTagRepository.existsByPostAndTag(post, tag)) {
            throw new DuplicateTagException("게시글에 이미 해당 태그가 존재합니다");
        }
        PostTag postTag = PostTag.builder()
                .post(post)
                .tag(tag)
                .build();
        postTagRepository.save(postTag);
        tag.getPostTags().add(postTag);
        post.getPostTags().add(postTag);

        return postTag.getId();
    }

    /*
        R : read
        태그로 게시글을 조회하는 경우만 고려하겠다.
        태그 자체에 대한 조회는 필요 없을 것 같아서.
     */

    @Transactional(readOnly = true)
    public GetPostListByTagResponse getPosts(GetPostListByTagRequest request) {
        // 먼저 postTag 리스트를 찾고 하나하나 포스트를 찾아서 추가해준다.
        if(!tagRepository.existsById(request.getId())) {
            throw new NotFoundByIdException();
        }
        List<PostTag> postTagList = postTagRepository.findByTagId(request.getId());
        List<Post> postList = new ArrayList<>();
        for (PostTag postTagEach : postTagList) {
            postList.add(postTagEach.getPost());
        }
        GetPostListByTagResponse response = new GetPostListByTagResponse(postList);
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

    @Transactional
    public String deleteTagFromPost(DeleteTagFromPostRequest request) {
        /*
            확인해야할 것들
            1. 요청으로 넘어온 태그가 존재하는 태그인가
            2. 해당 태그가 요청으로 넘어온 게시글에 포함되어있나
            3. 태그를 삭제하려는 요청자가 게시글 작성자 본인인가
         */
        return "TEMP";
    }
}
