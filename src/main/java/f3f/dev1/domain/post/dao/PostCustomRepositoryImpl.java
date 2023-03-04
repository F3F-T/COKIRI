package f3f.dev1.domain.post.dao;

import com.querydsl.core.QueryFactory;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import f3f.dev1.domain.message.model.QMessageRoom;
import f3f.dev1.domain.model.TradeStatus;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.post.model.QScrapPost;
import f3f.dev1.domain.scrap.model.QScrap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.querydsl.core.util.StringUtils.*;
import static f3f.dev1.domain.message.model.QMessageRoom.*;
import static f3f.dev1.domain.post.dto.PostDTO.*;
import static f3f.dev1.domain.post.model.QPost.*;
import static f3f.dev1.domain.post.model.QScrapPost.*;
import static f3f.dev1.domain.scrap.model.QScrap.*;
import static f3f.dev1.domain.tag.model.QPostTag.*;
import static f3f.dev1.domain.tag.model.QTag.*;

@Slf4j
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    /*
    public List<BookPageDto> getBookPage (int bookNo, int pageNo) {
    return queryFactory
            .select(Projections.fields(BookPageDto.class,
                    book.name,
                    Expressions.constantAs(bookNo, book.bookNo) // (1)
                )
            )
            .from(book)
            .where(book.bookNo.eq(bookNo))
            .offset(pageNo)
            .limit(10)
            .fetch();
        }

        위 예제 코드처럼 조건절에 사용된 값을 select에서는 제외할 수 있다.
        위 코드 참고해서 리팩토링 하기

        ->  안된다!!!!
        잘못 이해한듯 하다. Expressions.ConstantsAs는 위에서 말한 기능이 아니라, 이미 알고있는 값에 해당하는 필드를
        제외하고 나머지만 불러오는 기능이다.
        다른 방법을 찾던가 포기해야할 것 같다....
     */

//    @Override
//    // scrapCount, messageCount 때문에 DTO로 바로 뱉을 수 없음
//    // 위 2개와 scrap 여부를 확인해야함.
//    public Page<Post> findPostDTOByConditions(SearchPostRequestExcludeTag requestExcludeTag, Pageable pageable) {
//        QueryResults<Post> results = jpaQueryFactory.
//                selectFrom(post)
//                .where(productCategoryNameFilter(requestExcludeTag.getProductCategory()),
//                        wishCategoryNameFilter(requestExcludeTag.getWishCategory()),
//                        priceFilter(requestExcludeTag.getMinPrice(), requestExcludeTag.getMaxPrice()))
//                .orderBy(dynamicSorting(pageable.getSort()).toArray(OrderSpecifier[]::new))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetchResults();
//
//        List<Post> responseList = results.getResults();
//        // 아래 주석은 기존에 오류를 유발하던 코드이다.
//         long total = results.getTotal();
//         return new PageImpl<>(responseList, pageable, total);
////        JPAQuery<Long> countQuery = getCount(requestExcludeTag);
////        return PageableExecutionUtils.getPage(responseList, pageable, countQuery::fetchOne);
//    }


    /**
     * queryDSL의 fetchResults는 쿼리를 자동으로 2번 보낸다.
     * 첫번째는 count 쿼리로 페이징을 위한 전체 개수를 보기 위한 쿼리이다.
     * 두번째는 실제 데이터 조회이다.
     * 이렇게 쿼리를 2번 보내는 것이 비효율적이라 현재는 deprecated 됐다.
     * 따라서 아래와 같은 구조로 변경되었다.
     * count 쿼리를 따로 보내주는데, 조건에 따라 쿼리를 하지 않을 수도 있다.
     * https://jddng.tistory.com/345 해당 링크를 참고하자.
     */
    @Override
    public Page<Post> findPostDTOByConditions(SearchPostRequestExcludeTag requestExcludeTag, Pageable pageable) {
        List<Post> results = findPostListWithConditionWithoutTag(requestExcludeTag, pageable);
        JPAQuery<Long> countQuery = getCount(requestExcludeTag);
        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    // 쿼리 DSL에서는 Enum 타입만 조회할 수 있고 세부 필드는 조회할 수 없다.
    private List<Post> findPostListWithConditionWithoutTag(SearchPostRequestExcludeTag requestExcludeTag, Pageable pageable) {
        List<Post> results = jpaQueryFactory.
                selectFrom(post)
                .where(productCategoryNameFilter(requestExcludeTag.getProductCategory()),
                        wishCategoryNameFilter(requestExcludeTag.getWishCategory()),
                        priceFilter(requestExcludeTag.getMinPrice(), requestExcludeTag.getMaxPrice()))
                // TradeStatus를 비교하는 아래 조건은 필수 조건이기 때문에 BooleanExpression을 활용한 동적 쿼리로 짜지는 않겠다.
                .where(post.trade.tradeStatus.eq(requestExcludeTag.getTradeStatus()))
                .orderBy(dynamicSorting(pageable.getSort()).toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return results;
    }

    private JPAQuery<Long> getCount(SearchPostRequestExcludeTag requestExcludeTag) {
        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(post.count())
                .from(post)
                .where(productCategoryNameFilter(requestExcludeTag.getProductCategory()),
                        wishCategoryNameFilter(requestExcludeTag.getWishCategory()),
                        priceFilter(requestExcludeTag.getMinPrice(), requestExcludeTag.getMaxPrice())
                );
        return countQuery;
    }


    @Override
    public Page<Post> findPostsByTags(List<String>tagNames, Pageable pageable) {
        QueryResults<Post> results = jpaQueryFactory
                .selectFrom(post)
                .leftJoin(post.postTags, postTag).fetchJoin()
                .where(postTag.tag.name.in(tagNames))
                .groupBy(post.id)
                .having(post.id.count().eq((long) tagNames.size()))
                .orderBy(dynamicSorting(pageable.getSort()).toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Post> responseList = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(responseList, pageable, total);
    }

    private BooleanExpression productCategoryNameFilter(String productCategoryName) {
        return StringUtils.hasText(productCategoryName) ? post.productCategory.name.eq(productCategoryName) : null;
    }

    private BooleanExpression wishCategoryNameFilter(String wishCategoryName) {
        return StringUtils.hasText(wishCategoryName) ? post.wishCategory.name.eq(wishCategoryName) : null;
    }

    private BooleanExpression priceFilter(String minPrice, String maxPrice) {
        if(isNullOrEmpty(minPrice) && isNullOrEmpty(maxPrice)) {
            return null;
        }
        // 둘중 하나만 값이 있는 경우를 처리해주겠음
        if(isNullOrEmpty(minPrice) && !isNullOrEmpty(maxPrice)) {
            return post.price.loe(Long.parseLong(maxPrice));
        } else if(!isNullOrEmpty(minPrice) && isNullOrEmpty(maxPrice)) {
            return post.price.goe(Long.parseLong(minPrice));
        }
        // 어느 조건문에도 걸리지 않으면 둘 다 존재하는 경우. between으로 처리함.
        return post.price.between(Long.parseLong(minPrice), Long.parseLong(maxPrice));
    }

    private BooleanExpression memberFilter(Long currentMemberId) {
        if(currentMemberId == null) {
            return null;
        }
        return scrap.member.id.eq(currentMemberId);
    }

    /*
        동적 정렬 테스트 완료
        아래와 같이 활용할 수 있다.
        최신순 정렬  :
            {{SPRING}}/post?page=1&size=5&sort=createDate,DESC

        메시지룸으로 정렬 :
            {{SPRING}}/post?page=1&size=3&sort=messageRooms.size,DESC

        아이디로 정렬 :
            {{SPRING}}/post?page=0&size=3&sort=scrapPosts.size,DESC

        그 외 제목, 본문, 작성자명 등으로도 조회가 가능하다.
     */
    private List<OrderSpecifier> dynamicSorting(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();
        sort.stream().forEach(order -> {
            Order direction= order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            // 대상이 되는 클래스를 지정하고, 그 클래스 내에서 세부
            PathBuilder orderByExpression = new PathBuilder(Post.class, "post");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });
        return orders;
    }
}
