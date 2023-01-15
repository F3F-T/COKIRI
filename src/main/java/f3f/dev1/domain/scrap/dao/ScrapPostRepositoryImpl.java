package f3f.dev1.domain.scrap.dao;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import f3f.dev1.domain.scrap.dto.QScrapPostDTO_GetUserScrapPost;
import f3f.dev1.domain.scrap.dto.ScrapPostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static f3f.dev1.domain.category.model.QCategory.category;
import static f3f.dev1.domain.member.model.QMember.member;
import static f3f.dev1.domain.post.model.QPost.post;
import static f3f.dev1.domain.post.model.QScrapPost.scrapPost;
import static f3f.dev1.domain.trade.model.QTrade.trade;

@RequiredArgsConstructor
public class ScrapPostRepositoryImpl implements ScrapPostCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ScrapPostDTO.GetUserScrapPost> findUserScrapPost(Long scrapId, Pageable pageable) {
        QueryResults<ScrapPostDTO.GetUserScrapPost> getUserScrapPostQueryResults = queryFactory.select(new QScrapPostDTO_GetUserScrapPost(post.id, post.title, trade.tradeStatus, member.nickname, post.price, category.name, category.name))
                .from(post)
                .join(post.scrapPosts, scrapPost).on(post.id.eq(scrapPost.post.id))
                .join(post.trade, trade).on(post.id.eq(trade.post.id))
                .join(post.author, member).on(post.author.id.eq(member.id))
                .join(post.productCategory, category).on(post.productCategory.id.eq(category.id))
                .join(post.wishCategory, category).on(post.wishCategory.id.eq(category.id))
                .where(scrapPost.scrap.id.eq(scrapId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();


        return new PageImpl<>(getUserScrapPostQueryResults.getResults(), pageable, getUserScrapPostQueryResults.getTotal());
    }
}
