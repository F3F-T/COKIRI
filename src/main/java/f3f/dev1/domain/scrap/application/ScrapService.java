package f3f.dev1.domain.scrap.application;

import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.post.model.ScrapPost;
import f3f.dev1.domain.scrap.dao.ScrapRepository;
import f3f.dev1.domain.scrap.exception.DuplicateScrapByUserIdException;
import f3f.dev1.domain.scrap.model.Scrap;
import f3f.dev1.domain.user.application.SessionLoginService;
import f3f.dev1.domain.user.dao.UserRepository;
import f3f.dev1.domain.user.exception.NotAuthorizedException;
import f3f.dev1.domain.user.exception.UserNotFoundByEmailException;
import f3f.dev1.domain.user.model.User;
import f3f.dev1.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static f3f.dev1.domain.scrap.dto.ScrapDTO.*;
import static f3f.dev1.global.common.constants.ResponseConstants.DELETE;
import static f3f.dev1.global.common.constants.ResponseConstants.OK;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;

    private final SessionLoginService sessionLoginService;

    // 스크랩 생성 메서드
    @Transactional
    public void createScrap(CreateScrapDTO createScrapDTO) {
        if (scrapRepository.existsByUserId(createScrapDTO.getUser().getId())) {
            throw new DuplicateScrapByUserIdException();
        }

        Scrap scrap = createScrapDTO.toEntity();
        scrapRepository.save(scrap);

    }

    // 스크랩에 있는 포스트조회 메서드
    // TODO: 유저의 스크랩한 포스트 조회가 유저쪽에 있는게 나을까 스크랩에 있는게 나을까 고민해보고 수정예정
    // TODO: 현재 유저 정보를 sessionLoginService에서 받아와서 처리하는데, 추가로 유저 검증이 필요하지는 않을까
    @Transactional
    public GetScrapPostDTO getUserScrapPosts(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundByEmailException::new);

        Scrap scrapByUserId = scrapRepository.findScrapByUserId(user.getId()).orElseThrow(NotFoundByIdException::new);
        List<ScrapPost> scrapPosts = scrapByUserId.getScrapPosts();
        List<Post> posts = new ArrayList<>();
        for (ScrapPost scrapPost : scrapPosts) {
            posts.add(scrapPost.getPost());
        }


        return GetScrapPostDTO.builder()
                .scrapPosts(posts).build();
    }

    // 스크랩에 관심 포스트 추가 메소드
    // TODO: 스크랩 포스트 repository 코드 작성 되면 구현예정
    // TODO: 현재 dto에 유저 아이디와 포스트아이디 모두 존재하는데, 프론트와 상의 후 api 확정되면 확정 사항에 맞게 변경 예정
    // 세션에서 받아온 유저와 프론트에서 넘어온 유저가 다르면 예외 던지게 처리함
    @Transactional
    public ResponseEntity<String> addScrapPost(AddScrapPostDTO addScrapPostDTO) {
        User user = userRepository.findById(addScrapPostDTO.getUserId()).orElseThrow(NotFoundByIdException::new);
        if (!user.getEmail().equals(sessionLoginService.getLoginUser())) {
            throw new NotAuthorizedException();
        }


        return OK;
    }

    // 스크랩에 있는 포스트 삭제 메서드
    // TODO: scrap post 쪽 작성되면 구현예정
    // TODO: 현재 dto에 유저 아이디와 포스트아이디 모두 존재하는데, 프론트와 상의 후 api 확정되면 확정 사항에 맞게 변경 예정
    // 세션에서 받아온 유저와 프론트에서 넘어온 유저가 다르면 예외 던지게 처리할 예정
    @Transactional
    public ResponseEntity<String> deleteScrapPost(DeleteScrapPostDTO deleteScrapPostDTO) {


        return DELETE;
    }

}
