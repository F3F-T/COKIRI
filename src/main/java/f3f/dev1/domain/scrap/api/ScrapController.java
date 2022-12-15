package f3f.dev1.domain.scrap.api;

import f3f.dev1.domain.member.application.SessionLoginService;
import f3f.dev1.domain.scrap.application.ScrapService;
import f3f.dev1.domain.scrap.dto.ScrapDTO.GetScrapPostDTO;
import f3f.dev1.global.common.annotation.LoginCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static f3f.dev1.domain.scrap.dto.ScrapDTO.AddScrapPostDTO;
import static f3f.dev1.domain.scrap.dto.ScrapDTO.DeleteScrapPostDTO;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;

    private final SessionLoginService sessionLoginService;
    // 스크랩 포스트 조회 요청
    @GetMapping(value = "/user/scrap")
    @LoginCheck
    public ResponseEntity<GetScrapPostDTO> getScrapPosts() {
        return new ResponseEntity<>(scrapService.getUserScrapPosts(sessionLoginService.getLoginUser()), HttpStatus.OK);
    }
    // 유저 스크랩에 포스트 추가 요청
    @PostMapping(value = "/user/scrap")
    @LoginCheck
    public ResponseEntity<String> addScrapPost(@RequestBody AddScrapPostDTO addScrapPostDTO) {

        return scrapService.addScrapPost(addScrapPostDTO);
    }
    // 유저 스크랩에 포스트 삭제 요청
    @DeleteMapping(value = "/user/scrap")
    @LoginCheck
    public ResponseEntity<String> deleteScrapPost(@RequestBody DeleteScrapPostDTO deleteScrapPostDTO) {
        return scrapService.deleteScrapPost(deleteScrapPostDTO);
    }
}
