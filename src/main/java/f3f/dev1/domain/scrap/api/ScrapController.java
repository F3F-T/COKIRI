package f3f.dev1.domain.scrap.api;

import f3f.dev1.domain.scrap.dto.ScrapPostDTO;
import f3f.dev1.domain.scrap.application.ScrapService;
import f3f.dev1.domain.scrap.dto.ScrapDTO.CreateScrapPostDTO;
import f3f.dev1.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static f3f.dev1.domain.scrap.dto.ScrapDTO.AddScrapPostDTO;
import static f3f.dev1.domain.scrap.dto.ScrapDTO.DeleteScrapPostDTO;
import static f3f.dev1.domain.scrap.dto.ScrapPostDTO.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;

    // 스크랩 포스트 조회 요청
    @GetMapping(value = "/user/scrap")
    public ResponseEntity<Page<GetUserScrapPost>> getScrapPosts(Pageable pageable) {
        return new ResponseEntity<>(scrapService.getUserScrapPosts(SecurityUtil.getCurrentMemberId(), pageable), HttpStatus.OK);
    }
    // 유저 스크랩에 포스트 추가 요청
    @PostMapping(value = "/user/scrap")
    public ResponseEntity<CreateScrapPostDTO> addScrapPost(@RequestBody AddScrapPostDTO addScrapPostDTO) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(scrapService.addScrapPost(addScrapPostDTO, memberId));
    }
    // 유저 스크랩에 포스트 삭제 요청
    @DeleteMapping(value = "/user/scrap")
    public ResponseEntity<String> deleteScrapPost(@RequestBody DeleteScrapPostDTO deleteScrapPostDTO) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(scrapService.deleteScrapPost(deleteScrapPostDTO, memberId));
    }
}
