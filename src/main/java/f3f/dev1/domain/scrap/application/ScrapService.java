package f3f.dev1.domain.scrap.application;

import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.post.model.ScrapPost;
import f3f.dev1.domain.scrap.dao.ScrapRepository;
import f3f.dev1.domain.scrap.dto.ScrapDTO.CreateScrapDTO;
import f3f.dev1.domain.scrap.exception.DuplicateScrapByUserIdException;
import f3f.dev1.domain.scrap.model.Scrap;
import f3f.dev1.global.common.constants.ResponseConstants;
import f3f.dev1.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;

    // 스크랩 생성 메서드
    @Transactional
    public Long createScrap(CreateScrapDTO createScrapDTO) {
        if (scrapRepository.existsByUserId(createScrapDTO.getUser().getId())) {
            throw new DuplicateScrapByUserIdException();
        }

        Scrap scrap = createScrapDTO.toEntity();
        scrapRepository.save(scrap);

        return scrap.getId();
    }

    // 스크랩 조회 메서드
    // TODO: 유저의 스크랩한 포스트 조회가 유저쪽에 있는게 나을까 스크랩에 있는게 나을까 고민해보고 수정예정
    @Transactional
    public List<Post> getUserScrapPosts(Long userId) {
        Scrap scrapByUserId = scrapRepository.findScrapByUserId(userId).orElseThrow(NotFoundByIdException::new);
        List<ScrapPost> scrapPosts = scrapByUserId.getScrapPosts();
        List<Post> posts = new ArrayList<>();
        for (ScrapPost scrapPost : scrapPosts) {
            posts.add(scrapPost.getPost());
        }

        return posts;
    }

    // 스크랩에 관심 포스트 추가 메소드
    // TODO: 포스트 조회 구현되면 추가예정
    @Transactional
    public Long addScrapPost(Long userId, Long postId) {

        return userId;
    }

    // 스크랩에 있는 포스트 삭제 메서드
    // TODO: scrap post 쪽 작성되면 구현예정
    @Transactional
    public ResponseEntity<String> deleteScrapPost(Long userId, Long postId) {
        Scrap scrap = scrapRepository.findScrapByUserId(userId).orElseThrow(NotFoundByIdException::new);

        return ResponseConstants.DELETE;
    }

}
