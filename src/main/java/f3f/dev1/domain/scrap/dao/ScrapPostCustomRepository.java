package f3f.dev1.domain.scrap.dao;

import f3f.dev1.domain.scrap.dto.ScrapPostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScrapPostCustomRepository {
    Page<ScrapPostDTO.GetUserScrapPost> findUserScrapPost(Long scrapId, Pageable pageable);

}
