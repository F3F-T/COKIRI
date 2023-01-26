package f3f.dev1.domain.tag.application;

import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.dto.PostDTO;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.tag.dao.PostTagRepository;
import f3f.dev1.domain.tag.dao.TagRepository;
import f3f.dev1.domain.tag.exception.NotFoundByTagNameException;
import f3f.dev1.domain.tag.model.PostTag;
import f3f.dev1.domain.tag.model.Tag;
import f3f.dev1.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

import static f3f.dev1.domain.post.dto.PostDTO.*;

@Service
@Validated
@RequiredArgsConstructor
public class PostTagService {

    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;
    private final PostRepository postRepository;

    // 포스트 업데이트 할때 태그를 다시 설정해주기 위해 postTag를 날리는 로직
    @Transactional
    public String deletePostTagFromPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(NotFoundByIdException::new);
        postTagRepository.deletePostTagByPost(post);
//        postTagRepository.deleteByPost(post);
        return "DELETE";
    }

    @Transactional
    public String updatePostTagWithPatch(Long postId, UpdatePostRequest updatePostRequest) {
        Post post = postRepository.findById(postId).orElseThrow(NotFoundByIdException::new);
        List<String> originalList = new ArrayList<>();
            /*
                추가된 태그, 삭제된 태그를 식별하고 반영해주기 위해 필요한 리스트
                연산이 끝나고 결과에서
                    originalFlagList의 값이 false인 인덱스의 태그는 삭제되며
                    changedFlagList의 값이 false인 인덱스의 태그는 새로 추가된다.
             */
        boolean originalFlagList[] = new boolean[post.getPostTags().size()];
        boolean changedFlagList[] = new boolean[updatePostRequest.getTagNames().size()];
        for (PostTag postTag : post.getPostTags()) {
            originalList.add(postTag.getTag().getName());
        }
        if(!originalList.containsAll(updatePostRequest.getTagNames())) {
            // 태그가 달라졌다면, 어떤 태그가 달라졌는지 루프를 돌면서 찾고, 추가해준다.
            // 또한 수정하면서 삭제된 태그가 있다면, boolean 배열을 통해 체크하고 직접 지워준다.
            for(int i=0; i<originalList.size(); i++) {
                // 어쩔 수 없이 2중 for문을 돌기로 하겠다.
                for(int k=0; k<updatePostRequest.getTagNames().size(); k++) {
                    if(originalList.get(i).equals(updatePostRequest.getTagNames().get(k))) {
                        originalFlagList[i] = true;
                        changedFlagList[k] = true;
                    }
                }
            }

            for(int i=0; i<originalFlagList.length; i++) {
                if(!originalFlagList[i]) {
                    postTagRepository.delete(post.getPostTags().get(i));
                }
            }

            for(int k=0; k< changedFlagList.length; k++) {
                if(!changedFlagList[k]) {
                    // TODO 새로 들어온 태그이름 중 태그 엔티티가 없는 경우는 어떡하지
                    // 어쩔 수 없이 조회 쿼리를 한번 날리고 태그가 없으면 새로 만들어서 추가해주자.
                    if(!tagRepository.existsByName(updatePostRequest.getTagNames().get(k))) {
                        Tag tag = Tag.builder()
                                .name(updatePostRequest.getTagNames().get(k))
                                .build();
                        tagRepository.save(tag);
                        PostTag postTag = PostTag.builder()
                                .post(post)
                                .tag(tag)
                                .build();
                        postTagRepository.save(postTag);
                    } else {
                        //  태그는 존재하는데 게시글에 없었을 경우
                        Tag tag = tagRepository.findByName(updatePostRequest.getTagNames().get(k)).orElseThrow(NotFoundByTagNameException::new);
                        PostTag postTag = PostTag.builder()
                                .post(post)
                                .tag(tag)
                                .build();
                        postTagRepository.save(postTag);
                    }
                }
            }
        }
        return "UPDATED";
    }

}
