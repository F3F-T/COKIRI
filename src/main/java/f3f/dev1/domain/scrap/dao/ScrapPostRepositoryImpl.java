package f3f.dev1.domain.scrap.dao;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import f3f.dev1.domain.post.model.QScrapPost;
import f3f.dev1.domain.scrap.dto.QScrapPostDTO_GetUserScrapPost;
import f3f.dev1.domain.scrap.dto.ScrapPostDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static f3f.dev1.domain.category.model.QCategory.category;
import static f3f.dev1.domain.member.model.QMember.member;
import static f3f.dev1.domain.post.model.QPost.post;
import static f3f.dev1.domain.post.model.QScrapPost.scrapPost;
import static f3f.dev1.domain.trade.model.QTrade.trade;

@RequiredArgsConstructor
@Slf4j
public class ScrapPostRepositoryImpl implements ScrapPostCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ScrapPostDTO.GetUserScrapPost> findUserScrapPost(Long scrapId, Pageable pageable) {
        QScrapPost newScrapPost = new QScrapPost("newScrapPost");
        log.info("유저 스크랩 포스트 조회 호출됌");
        QueryResults<ScrapPostDTO.GetUserScrapPost> getUserScrapPostQueryResults = queryFactory.select(new QScrapPostDTO_GetUserScrapPost(post.id, post.title, post.thumbnailImgPath, trade.tradeStatus, category.name, scrapPost.scrap.count()))
                .from(scrapPost)
                .join(scrapPost.post, post).on(scrapPost.post.id.eq(post.id))
                .join(post.trade, trade).on(trade.post.id.eq(scrapPost.post.id))
                .join(post.wishCategory, category).on(post.wishCategory.id.eq(category.id))
                .where(scrapPost.post.id.in(
                        JPAExpressions
                                .select(newScrapPost.post.id)
                                .from(newScrapPost)
                                .where(newScrapPost.scrap.id.eq(scrapId))
                ))
                .groupBy(scrapPost.post.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();


        return new PageImpl<>(getUserScrapPostQueryResults.getResults(), pageable, getUserScrapPostQueryResults.getTotal());
    }
}
