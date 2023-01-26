package f3f.dev1.domain.post.dao;

import com.querydsl.core.QueryFactory;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import f3f.dev1.domain.message.model.QMessageRoom;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.post.model.QScrapPost;
import f3f.dev1.domain.scrap.model.QScrap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Override
    // scrapCount, messageCount 때문에 DTO로 바로 뱉을 수 없음
    // 위 2개와 scrap 여부를 확인해야함.
    public Page<Post> findPostDTOByConditions(SearchPostRequestExcludeTag requestExcludeTag, Pageable pageable) {
        QueryResults<Post> results = jpaQueryFactory.
                selectFrom(post)
                .where(productCategoryNameFilter(requestExcludeTag.getProductCategory()),
                        wishCategoryNameFilter(requestExcludeTag.getWishCategory()),
                        priceFilter(requestExcludeTag.getMinPrice(), requestExcludeTag.getMaxPrice()))
                .orderBy(dynamicSorting(pageable.getSort()).toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Post> responseList = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(responseList, pageable, total);
    }


    @Override
    // 조인때문에 (select 결과로 얻지 못한 필드는 조인에서 사용할 수 없음) DTO로 바로 뱉는건 힘들거같다.
    public Page<Post> findPostsByTags(List<String>tagNames, Pageable pageable) {
        QueryResults<Post> results = jpaQueryFactory
                .selectFrom(post)
                .leftJoin(post.postTags, postTag).fetchJoin()
                .where(postTag.tag.name.in(tagNames))
                .groupBy(post.id)
                .having(post.id.count().eq((long) tagNames.size()))
//                .orderBy(post.id.desc())
                // 동적 sorting, 테스트 미실시
                .orderBy(dynamicSorting(pageable.getSort()).toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Post> responseList = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(responseList, pageable, total);
    }

//    @Override
//    public Page<PostSearchResponseDto> findPostDTOByConditions(SearchPostRequestExcludeTag requestExcludeTag, Long currentMemberId, Pageable pageable) {
//
//        StringTemplate stringDate = Expressions.stringTemplate(
//                "DATE_FORMAT({0},{1})", post._super.createDate, ConstantImpl.create("%Y-%m-%d"));
//        QueryResults<PostSearchResponseDto> results = jpaQueryFactory.
//                select(Projections.constructor(PostSearchResponseDto.class,
//                        post.id,
//                        post.title,
//                        post.content,
//                        post.thumbnailImgPath,
//                        post.author.nickname,
//                        post.productCategory.name,
////                        post._super.createDate,
//                        stringDate,
//                        post.messageRooms.size().count(),
//                        post.wishCategory.name,
//                        new CaseBuilder().when(memberFilter(currentMemberId))
//                            .then(true)
//                            .otherwise(false),
//                        post.scrapPosts.size().count(),
//                        post.price
//                        ))
//                .from(post)
//                .leftJoin(post.scrapPosts, scrapPost)
//                .where(scrapPost.post.id.eq(post.id))
//                .leftJoin(scrapPost.scrap, scrap)
//                .where(productCategoryNameFilter(requestExcludeTag.getProductCategory()),
//                        wishCategoryNameFilter(requestExcludeTag.getWishCategory()),
//                        priceFilter(requestExcludeTag.getMinPrice(), requestExcludeTag.getMaxPrice()))
//                .orderBy(dynamicSorting(pageable.getSort()).toArray(OrderSpecifier[]::new))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetchResults();
//
//        List<PostSearchResponseDto> responseList = results.getResults();
//        long total = results.getTotal();
//        return new PageImpl<>(responseList, pageable, total);
//    }

//    @Override
//    public Page<PostSearchResponseDto> findPostsByTags(List<String> tagNames, Long currentMemberId, Pageable pageable) {
//        QueryResults<PostSearchResponseDto> results = jpaQueryFactory
//                .select(Projections.constructor(PostSearchResponseDto.class,
//                        post.id,
//                        post.title,
//                        post.content,
//                        post.thumbnailImgPath,
//                        post.author.nickname,
//                        post.productCategory,
//                        post._super.createDate,
//                        post.messageRooms.size(),
//                        post.wishCategory,
//                        post.scrapPosts.size(),
//                        post.price,
//                        new CaseBuilder().when(scrap.member.id.eq(currentMemberId))
//                            .then(true)
//                            .otherwise(false),
//                        Expressions.
//                        ))
//                .from(post)
//                // Join 때문에 잠시 보류.
//                .leftJoin(post.postTags, postTag).fetchJoin()
//                .where(postTag.tag.name.in(tagNames))
//                .groupBy(post.id)
//                .having(post.id.count().eq((long) tagNames.size()))
//                .orderBy(dynamicSorting(pageable.getSort()).toArray(OrderSpecifier[]::new))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetchResults();
//
//        List<PostSearchResponseDto> responseList = results.getResults();
//        long total = results.getTotal();
//        return new PageImpl<>(responseList, pageable, total);
//    }

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
