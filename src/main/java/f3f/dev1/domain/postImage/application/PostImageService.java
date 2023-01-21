package f3f.dev1.domain.postImage.application;

import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.postImage.dao.PostImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostImageService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;


}
