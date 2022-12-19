package f3f.dev1.domain.scrap.application;

import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.exception.NotAuthorizedException;
import f3f.dev1.domain.member.exception.UserNotFoundByEmailException;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.dao.ScrapPostRepository;
import f3f.dev1.domain.post.dto.PostDTO;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.post.model.ScrapPost;
import f3f.dev1.domain.scrap.dao.ScrapRepository;
import f3f.dev1.domain.scrap.dto.ScrapDTO.CreateScrapDTO;
import f3f.dev1.domain.scrap.exception.DuplicateScrapByUserIdException;
import f3f.dev1.domain.scrap.exception.NotFoundPostInScrapException;
import f3f.dev1.domain.scrap.model.Scrap;
import f3f.dev1.domain.trade.dao.TradeRepository;
import f3f.dev1.domain.trade.model.Trade;
import f3f.dev1.global.error.exception.NotFoundByIdException;
import f3f.dev1.global.util.SecurityUtil;
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
    private final MemberRepository memberRepository;

    private final TradeRepository tradeRepository;

    private final ScrapPostRepository scrapPostRepository;

    private final SessionLoginService sessionLoginService;
    private final PostRepository postRepository;

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
    @Transactional(readOnly = true)
    public GetScrapPostDTO getUserScrapPosts(Long memberId) {
        Member user = memberRepository.findById(memberId).orElseThrow(NotFoundByIdException::new);

        Scrap scrapByUserId = scrapRepository.findScrapByUserId(user.getId()).orElseThrow(NotFoundByIdException::new);
        List<ScrapPost> scrapPosts = scrapByUserId.getScrapPosts();
        List<PostDTO.PostInfoDto> posts = new ArrayList<>();
        for (ScrapPost scrapPost : scrapPosts) {
            Trade trade = tradeRepository.findByPostId(scrapPost.getPost().getId()).orElseThrow(NotFoundByIdException::new);
            posts.add(scrapPost.getPost().toInfoDto(trade.getTradeStatus()));
        }


        return GetScrapPostDTO.builder()
                .scrapPosts(posts).build();
    }

    // 스크랩에 관심 포스트 추가 메소드
    // 세션에서 받아온 유저와 프론트에서 넘어온 유저가 다르면 예외 던지게 처리함
    @Transactional
    public String addScrapPost(AddScrapPostDTO addScrapPostDTO) {
        Member user = memberRepository.findById(addScrapPostDTO.getUserId()).orElseThrow(NotFoundByIdException::new);
        if (!user.getEmail().equals(sessionLoginService.getLoginUser())) {
            throw new NotAuthorizedException();
        }
        Scrap scrap = scrapRepository.findScrapByUserId(addScrapPostDTO.getUserId()).orElseThrow(NotFoundByIdException::new);
        Post post = postRepository.findById(addScrapPostDTO.getPostId()).orElseThrow(NotFoundByIdException::new);

        ScrapPost scrapPost = ScrapPost.builder().post(post).scrap(scrap).build();
        scrapPostRepository.save(scrapPost);
        return "OK";
    }

    // 스크랩에 있는 포스트 삭제 메서드
    // 세션에서 받아온 유저와 프론트에서 넘어온 유저가 다르면 예외 던지게 처리할 예정
    @Transactional
    public String deleteScrapPost(DeleteScrapPostDTO deleteScrapPostDTO) {
        Member user = memberRepository.findById(deleteScrapPostDTO.getUserId()).orElseThrow(NotFoundByIdException::new);
        if (!user.getEmail().equals(sessionLoginService.getLoginUser())) {
            throw new NotAuthorizedException();
        }
        Post post = postRepository.findById(deleteScrapPostDTO.getPostId()).orElseThrow(NotFoundByIdException::new);
        ScrapPost scrapPost = scrapPostRepository.findByScrapIdAndPostId(user.getScrap().getId(), post.getId()).orElseThrow(NotFoundPostInScrapException::new);
        scrapPostRepository.delete(scrapPost);


        return "DELETE";
    }

}
