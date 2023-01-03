package f3f.dev1.domain.tag.application;

import f3f.dev1.domain.tag.dao.PostTagRepository;
import f3f.dev1.domain.tag.dao.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
public class PostTagService {

    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;


}
