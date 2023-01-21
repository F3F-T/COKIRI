package f3f.dev1.domain.postImage.application;

import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.postImage.dao.PostImageRepository;
import f3f.dev1.domain.postImage.dto.PostImageDTO;
import f3f.dev1.domain.postImage.model.PostImage;
import f3f.dev1.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static f3f.dev1.domain.postImage.dto.PostImageDTO.*;

@Service
@RequiredArgsConstructor
public class PostImageService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;

    // 이미지들 PostController로부터 넘어오면 PostImage 엔티티에 모두 저장
    @Transactional
    public void savePostImages(List<String> images, Long postId) {
        // images가 비어있지 않다는 것은 프론트 측에서 보장을 해준다. 따라서 별도의 검증 로직을 추가하지 않겠다.
        Post post = postRepository.findById(postId).orElseThrow(NotFoundByIdException::new);
        for(int i=0; i<images.size(); i++) {
            PostImage postImage = PostImage.builder()
                    .imgPath(images.get(i))
                    .isThumbnail(i == 0)
                    .post(post)
                    .build();
            postImageRepository.save(postImage);
        }
    }


}
